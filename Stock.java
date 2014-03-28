import java.util.ArrayList;
import java.io.Serializable;
public class Stock implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	public String name;
	public String symbol;
	public int shares;
	public int price;
	public ArrayList<Order> buyList;
	public ArrayList<Order> sellList;
	public Stock(String name,String symbol){
		this.name=name;
		this.symbol=symbol;
		this.shares=10000;
		this.price=100;
		this.buyList=new ArrayList<Order>();
		this.sellList=new ArrayList<Order>();
	}
	public Stock(String symbol,int shares){
		this.symbol=symbol;
		this.shares=shares;
		this.buyList=new ArrayList<Order>();
		this.sellList=new ArrayList<Order>();
	}

	//order list
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
