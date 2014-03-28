import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.util.Util;


import java.io.*;
import java.net.ServerSocket;
import java.util.*;
public class VSynchrony extends ReceiverAdapter {
	public static List<Account> clients=new ArrayList<Account>();
	public static List<Stock> stockList=new ArrayList<Stock>();
	public JChannel channel;
	public int pid, clientNumber,portNumber;
	public static int orderId=0;
	public ServerSocket server;
//	public static File logFile;
	public VSynchrony(int pid, int clientNumber, int portNumber){
		this.pid=pid;
		this.clientNumber=clientNumber;
		this.portNumber=portNumber;
		//logFile=new File("log"+pid);
	}
	public void receive(Message msg){
		/**
		 * handle the message
		 */
		String message=""+msg.getObject();
		String[] results=message.split("\\s");
		results[0]=""+results[0].charAt(1);
		int clientId=Integer.valueOf(results[0]);
		String op=results[1];
		String symbol=results[2];
		int price=Integer.valueOf(results[3]);
		int shares=Integer.valueOf(results[4]);
		new Thread(new Worker(new Order(orderId,clientId,op,symbol,price,shares))).start();
		
	}
    public void getState(OutputStream output) throws Exception {
    	StateObj myState=new StateObj(orderId,clients,stockList);
    	
    	   synchronized(myState){
    		   Util.objectToStream(myState,new DataOutputStream(output));
    	   }     
    	     
    	/**
    	synchronized(state) {
    		//final List<Account> state=new ArrayList<Account>(clients);
    		//state.addAll(clients);
            Util.objectToStream(state, new DataOutputStream(output));
        }
        */
        
    }
    public void setState(InputStream input) throws Exception {
    	
    	StateObj rState=(StateObj)Util.objectFromStream(new DataInputStream(input));
    	synchronized(clients){
    		synchronized(stockList){
    			stockList.clear();
    			stockList.addAll(rState.stockList);
    		}
    		clients.clear();
    		clients.addAll(rState.clients);
    	}
    	VSynchrony.orderId=rState.orderId;
    	
    	// List<String> list=(List<String>)Util.objectFromStream(new DataInputStream(input));
         /**
    	 synchronized(state) {
             state.clear();
             state.addAll(list);
         }
         */

    }
	public void viewAccepted(View new_view){
		System.out.println("** view: " + new_view);		
	}
	
	public void start() throws Exception{
		channel=new JChannel("protocol.xml");
		//channel=new JChannel();
		server=new ServerSocket(this.portNumber);
		channel.setReceiver(this);
		channel.connect("Tao_Yu");
		if(channel.getView().size()==1){
			if(VSynchrony.clients.size()==0){
				synchronized(VSynchrony.stockList){
				//initialize the stocks
				Properties prop=new Properties();	
				try{
					FileInputStream in=new FileInputStream("index.properties");
					prop.load(in);
					in.close();
					while(prop.elements().hasMoreElements()){
						String symbol=(String)prop.propertyNames().nextElement();
						String name=(String)prop.getProperty(symbol);
						VSynchrony.stockList.add(new Stock(name,symbol));
						prop.remove(symbol);
					}
					//prop.
				}catch(Exception e){
					
				}
				//initialize the clients
					synchronized(VSynchrony.clients){
						for(int i=0;i<this.clientNumber;i++){
							Account account=new Account(i+1);
							for(int j=0;j<VSynchrony.stockList.size();j++){
								account.stockshares.put(VSynchrony.stockList.get(j).symbol, 0);
							}
						VSynchrony.clients.add(account);
						}
					}
				}				
			}
		}
		channel.getState(null, 10000);
		new Thread(new Dispatcher(this.server,this.channel)).start();
		
	}
	public static synchronized void log(int orderId,Order order,int price, ArrayList<Order> buyList, ArrayList<Order> sellList){
		try{
		//	PrintWriter logStream=new PrintWriter(logFile);
			System.out.println("Order "+orderId+": "+order.toString());
			System.out.println("Price: "+price);
			System.out.print("Buys:");
			for(int i=0;i<buyList.size();i++){
				System.out.print(" ["+buyList.get(i).toString()+"]");
			}
			System.out.println();
			System.out.print("Sell:");
			for(int i=0;i<sellList.size();i++){
				System.out.print(" ["+sellList.get(i).toString()+"]");
			}
			System.out.println();
			System.out.print("[");
			for(int i=0;i<VSynchrony.clients.size();i++){
				System.out.print("C"+VSynchrony.clients.get(i).id+" "+VSynchrony.clients.get(i).getBalance());
				if(i!=VSynchrony.clients.size()-1){
					System.out.print(", ");
				}
			}
			System.out.println("]");
			for(int i=0;i<67;i++){
				System.out.print("-");
			}
			System.out.println();
		}catch(Exception e){
			
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		int pid=Integer.valueOf(args[0]);
		int clientNumber=Integer.valueOf(args[1]);
		int portNumber=Integer.valueOf(args[2]);
		new VSynchrony(pid,clientNumber,portNumber).start();
		
	}

}
