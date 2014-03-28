import java.util.Collections;
import java.util.ArrayList;
public class Worker implements Runnable {
public Order order;
public Worker(Order order){
	this.order=order;
}
	
	public void run(){

			Stock stock=null;
			synchronized(VSynchrony.stockList){
			for(int i=0;i<VSynchrony.stockList.size();i++){
				if(VSynchrony.stockList.get(i).symbol.equals(this.order.symbol)){
					stock=VSynchrony.stockList.get(i);
					break;
				}
					
			}
			}
			
			synchronized(stock){
			if(this.order.op.equals("BUY")){//buy order
				if(stock.sellList.isEmpty()){
					stock.buyList.add(this.order);
					Collections.sort(stock.buyList,Collections.reverseOrder());
					//System.out.println("add buy "+this.order.symbol+","+this.order.price);
					//log
					ArrayList<Order> totalBuy=new ArrayList<Order>();
					ArrayList<Order> totalSell=new ArrayList<Order>();
					for(int i=0;i<VSynchrony.stockList.size();i++){
						totalBuy.addAll(VSynchrony.stockList.get(i).buyList);
						totalSell.addAll(VSynchrony.stockList.get(i).sellList);
					}
					VSynchrony.log(this.order.orderId,this.order,stock.price,totalBuy,totalSell);
					VSynchrony.orderId++;				
				}
				else if(stock.sellList.get(0).price<order.price||stock.sellList.get(0).price==order.price){
					/**
					 * change shares in the two clients
					 * update price
					 * delete order in the sellList
					 */
					synchronized(VSynchrony.clients){
						Account seller=VSynchrony.clients.get(stock.sellList.get(0).clientId-1);
						Account buyer=VSynchrony.clients.get(this.order.clientId-1);
						int sellerShare=seller.stockshares.get(order.symbol);
						sellerShare=sellerShare-order.shares;
						seller.stockshares.put(order.symbol, sellerShare);
						seller.balance=seller.balance+stock.sellList.get(0).price*order.shares;
						int buyerShare=buyer.stockshares.get(order.symbol);						
						buyerShare=buyerShare+order.shares;						
						buyer.stockshares.put(order.symbol, buyerShare);						
						buyer.balance=buyer.balance-stock.sellList.get(0).price*order.shares;
					}
					stock.price=stock.sellList.get(0).price;
					int orderId=stock.sellList.get(0).orderId;
					stock.sellList.remove(0);

					//System.out.println(" Commit made: Buy "+this.order.symbol+" "+this.order.price+"Current price: "+stock.price);
					//log
					ArrayList<Order> totalBuy=new ArrayList<Order>();
					ArrayList<Order> totalSell=new ArrayList<Order>();
					for(int i=0;i<VSynchrony.stockList.size();i++){
						totalBuy.addAll(VSynchrony.stockList.get(i).buyList);
						totalSell.addAll(VSynchrony.stockList.get(i).sellList);
					}
					VSynchrony.log(orderId,this.order,stock.price,totalBuy,totalSell);
				}
				else{
					stock.buyList.add(this.order);
					Collections.sort(stock.buyList,Collections.reverseOrder());
				//	System.out.println("add buy "+this.order.symbol+","+this.order.price);
					//log
					ArrayList<Order> totalBuy=new ArrayList<Order>();
					ArrayList<Order> totalSell=new ArrayList<Order>();
					for(int i=0;i<VSynchrony.stockList.size();i++){
						totalBuy.addAll(VSynchrony.stockList.get(i).buyList);
						totalSell.addAll(VSynchrony.stockList.get(i).sellList);
					}
					VSynchrony.log(this.order.orderId,this.order,stock.price,totalBuy,totalSell);
					VSynchrony.orderId++;
				}
			}
			else{//sell order
				if(stock.buyList.isEmpty()){
					stock.sellList.add(this.order);
					Collections.sort(stock.sellList);
				//	System.out.println("add sell "+this.order.symbol+","+this.order.price);
					//log
					ArrayList<Order> totalBuy=new ArrayList<Order>();
					ArrayList<Order> totalSell=new ArrayList<Order>();
					for(int i=0;i<VSynchrony.stockList.size();i++){
						totalBuy.addAll(VSynchrony.stockList.get(i).buyList);
						totalSell.addAll(VSynchrony.stockList.get(i).sellList);
					}
					VSynchrony.log(this.order.orderId,this.order,stock.price,totalBuy,totalSell);
					VSynchrony.orderId++;
				}
				else if(stock.buyList.get(0).price>order.price||stock.buyList.get(0).price==order.price){
					/**
					 * change shares in the two clients
					 * update price
					 * delete order in the buyList
					 */
					synchronized(VSynchrony.clients){
						Account buyer=VSynchrony.clients.get(stock.buyList.get(0).clientId-1);
						Account seller=VSynchrony.clients.get(this.order.clientId-1);
						int sellerShare=seller.stockshares.get(order.symbol);
						sellerShare=sellerShare-order.shares;
						seller.stockshares.put(order.symbol, sellerShare);
						seller.balance=seller.balance+order.price*order.shares;
						int buyerShare=buyer.stockshares.get(order.symbol);			
						buyerShare=buyerShare+order.shares;
						buyer.stockshares.put(order.symbol, buyerShare);
						buyer.balance=buyer.balance-order.price*order.shares;
					}
					stock.price=order.price;
					int orderId=stock.buyList.get(0).orderId;
					stock.buyList.remove(0);
					
					//System.out.println(" Commit made: Sell "+this.order.symbol+" "+this.order.price+"Current price: "+stock.price);
					//log
					ArrayList<Order> totalBuy=new ArrayList<Order>();
					ArrayList<Order> totalSell=new ArrayList<Order>();
					for(int i=0;i<VSynchrony.stockList.size();i++){
						totalBuy.addAll(VSynchrony.stockList.get(i).buyList);
						totalSell.addAll(VSynchrony.stockList.get(i).sellList);
					}
					VSynchrony.log(orderId,this.order,stock.price,totalBuy,totalSell);
				}
				else{
					stock.sellList.add(this.order);
					Collections.sort(stock.sellList);
				//	System.out.println("add sell "+this.order.symbol+","+this.order.price);
					//log
					ArrayList<Order> totalBuy=new ArrayList<Order>();
					ArrayList<Order> totalSell=new ArrayList<Order>();
					for(int i=0;i<VSynchrony.stockList.size();i++){
						totalBuy.addAll(VSynchrony.stockList.get(i).buyList);
						totalSell.addAll(VSynchrony.stockList.get(i).sellList);
					}
					VSynchrony.log(this.order.orderId,this.order,stock.price,totalBuy,totalSell);
					VSynchrony.orderId++;
				}
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
