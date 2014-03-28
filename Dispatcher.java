import org.jgroups.JChannel;
import org.jgroups.Message;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectInputStream;
public class Dispatcher implements Runnable{
	public ServerSocket serversocket;
	public JChannel channel;
	public Dispatcher(ServerSocket serversocket,JChannel channel){
		this.serversocket=serversocket;
		this.channel=channel;
	}
	public void run(){
		Socket clientsock=null;
		while(true){
			try{
				clientsock=serversocket.accept();
				ObjectInputStream ois = new ObjectInputStream(clientsock.getInputStream());
				String request = (String)ois.readObject();
				//handle request
				Message msg=new Message(null,null,request);
				channel.send(msg);
				channel.startFlush(true);
				ois.close();
				clientsock.close();
			}catch(Exception e){
				
			}
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
