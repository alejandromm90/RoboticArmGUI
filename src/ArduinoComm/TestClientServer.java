package ArduinoComm;

public class TestClientServer {
public static void main(String[] args) {
	//setup server: 
	Server s = new Server(1234);     
     Thread t = new Thread(s);
     t.start();
     
     
     Client c1 = new Client(null, 1234);
     Client c2 = new Client(null, 1234);
     Client c3 = new Client(null, 1234);
     
     try {
		Thread.sleep(1000);
		c1.sendMessage("m1");
		Thread.sleep(500);
		c1.sendMessage("m1");
		c2.sendMessage("m2");
		Thread.sleep(500);
		c3.sendMessage("m3");
		
		
		s.stopServer();
		c1.closeConnection();
		c2.closeConnection();
		c3.closeConnection();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
     
     //
}
}
