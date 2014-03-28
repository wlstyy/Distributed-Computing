import java.io.Serializable;
import java.util.*;
public class StateObj implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public List<Account> clients=new ArrayList<Account>();
	public List<Stock> stockList=new ArrayList<Stock>();
	public int orderId;
	public StateObj(int orderId,List<Account> clients,List<Stock> stockList){
		this.orderId=orderId;
		this.clients=clients;
		this.stockList=stockList;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
