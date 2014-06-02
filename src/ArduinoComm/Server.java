package ArduinoComm;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server implements Runnable{

	

private ServerSocket server;
private ArrayList<MultiClientWorker> clients = new ArrayList<MultiClientWorker>();
private boolean stop = false;
private int port =0;

public Server (int port){
	this.port = port;
}


public void stopServer(){
	  stop  = true;
	  for (MultiClientWorker client : clients) {
		client.stopClient();
	}
	  try {
		server.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
}

private void listenSocket(){
  try{
    server = new ServerSocket(port);
  } catch (IOException e) {
    System.out.println("Could not listen on port "+port);
    System.exit(-1);
  }
  while(!stop){
    MultiClientWorker w;
    try{
//server.accept returns a client connection
      w = new MultiClientWorker(server.accept());
      if(stop) return;
      System.out.println("new connection");
      Thread t = new Thread(w);
      t.start();
      clients.add(w);
    } catch (IOException e) {
      System.out.println("Accept failed: "+port);
    }
  }
}

@Override
public void run() {
	listenSocket();
}

}
