import java.util.*;
import java.io.Serializable;
public class Order implements Comparable<Order>,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4L;
	public int clientId;
	public String op;
	public String symbol;
	public int price;
	public int shares;
	public int orderId;
	public Order(int orderId, int clientID,String op,String symbol,int price,int shares){
		this.orderId=orderId;
		this.clientId=clientID;
		this.op=op;
		this.symbol=symbol;
		this.price=price;
		this.shares=shares;
	}
	public Order(int price){
		this.price=price;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Order> orders=new ArrayList<Order>();
		orders.add(new Order(5));
		orders.add(new Order(15));
		orders.add(new Order(10));
		Collections.sort(orders,Collections.reverseOrder());
		for(int i=0;i<orders.size();i++){
			System.out.println(orders.get(i).price);
		}

	}
	public String toString(){
		return "C"+clientId+" "+op+" "+symbol+" "+price+" "+"10";
	}
	@Override
	public int compareTo(Order order) {
		// TODO Auto-generated method stub
		if(this.price>order.price)
			return 1;
		else if(this.price<order.price)
			return -1;
		return 0;
	}

}
