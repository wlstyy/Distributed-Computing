This program consists of 8 classes:
1. Account is an abstract data type which is to store account information of the clients.
	getBalance(): return the total balance of this account.
2. Order is an abstract data type to store information of each order.
3. Stock is an abstract data type to store information of each stock.
4. StateObj is an object to be transmitted through the network.
5. VSynchrony is the main classes.
	method in this class are mostly overridden methods like receive(), viewAccepted(), getState(), setState().
	Another method is start() which initiate the channel and begins a Dispatcher thread
6. Dispatcher is the thread to receive request from the clients and multicast the request to all the servers.
	run() from the runnable interface: receive the request from the clients and multicast the request to all the servers in the group.
7. Worker is the thread to deal with the order 
	run() from the runnable interface: deal with the received order.
8. Client which is provided by the TA is to test the program.

In this program, I changed the original protocol to protocol.xml. In this protocol, I turned on the flush protocol. Initially, I wanted to turn on the message ordering as well. However, if I turn on the sequencer or message ordering, there is a single point of failure that if the first server fails, the other servers won't be able to detect the change of the view. I tried my program if I turned on the sequencer, if the first server failed, the entire system failed. However, if I turn off the sequencer, this problem disappears. This may be due to the reason that the first server is the server of message ordering in this system. I am not sure about why this happened. Therefore, to make my program more robust, my protocol.xml just turns on the flush protocol rather than both the flush protocol and sequencer protocol.