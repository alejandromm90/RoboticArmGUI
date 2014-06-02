package ArduinoComm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
private Socket socket;
private PrintWriter out;
private BufferedReader in;




public Client( String adress, int port){
   try{
     socket = new Socket(adress, port);
     out = new PrintWriter(socket.getOutputStream(), 
                 true);
     in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
   } catch (UnknownHostException e) {
     System.out.println("Unknown host");
   } catch  (IOException e) {
     System.out.println("No I/O");
   }
}


public void closeConnection(){
	try {
		socket.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
}

public void sendMessage(String message){
      out.println(message);
  	System.out.println("client sent: "+message);
  	//TODO check if busy or not
}
public String ReceiveMessage(){
	String line = null;
	try{
	     line = in.readLine();
	     System.out.println("Text received: " + line);
	   } catch (IOException e){
	     System.out.println("Read failed");
	   }
	
	return line;
}

}
