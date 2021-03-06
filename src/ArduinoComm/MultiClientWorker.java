package ArduinoComm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import sun.awt.windows.ThemeReader;
import constants.Constants;

public class MultiClientWorker implements Runnable {
  private Socket client;
  private boolean stop= false;

//Constructor
  MultiClientWorker(Socket client) {
    this.client = client;
  }
  
  
  public void stopClient(){
	  stop = true;
	  try {
		client.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
  }

  public void run(){
    String line;
    BufferedReader in = null;
    PrintWriter out = null;
    try{
      in = new BufferedReader(new 
        InputStreamReader(client.getInputStream()));
      out = new 
        PrintWriter(client.getOutputStream(), true);
    } catch (IOException e) {
      System.out.println("in or out failed");
    }

  out.println(Constants.ACK_NEW_CONNECTION);

    while(!stop){
      try{
        line = in.readLine();
        if(line == null){
        	stopClient();
        } else if(! TalkWithArduino.wirteDirectly(line, (int)Thread.currentThread().getId())){
        	//TODO busy message
        	//out.write("server busy try later");
        }
        
       }catch (IOException e) {
        System.out.println("Read failed");
       }
    }
  }
}
