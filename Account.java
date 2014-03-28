import java.util.Hashtable;
import java.io.Serializable;
public class Account implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;
	public int id;
	public int balance=10000;
	public Hashtable<String,Integer> stockshares=new Hashtable<String,Integer>();
	public Account(int id){
		this.id=id;
	}
	public int getBalance(){
		int total=this.balance;
		synchronized(VSynchrony.stockList){
		for(int i=0;i<VSynchrony.stockList.size();i++){
			if(stockshares.containsKey(VSynchrony.stockList.get(i).symbol)){
				total=total+stockshares.get(VSynchrony.stockList.get(i).symbol)*VSynchrony.stockList.get(i).price;
			}
		}
		}
		return total;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
