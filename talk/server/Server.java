package talk.server;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import talk.common.*;

public class Server {
  public static int uniqueId;
  public ArrayList<Delegate> delegates;
  public DateFormatter formatter;
  private int port;
  private boolean keepGoing;

  public Server(int port) {
    this.port = port;
    formatter = new DateFormatter();
    delegates = new ArrayList<Delegate>();
  }
  
  public void bootUp() {
    keepGoing = true;
    try {
      ServerSocket serverSocket = new ServerSocket(port);
      while(keepGoing) 
      {
        display("Server waiting for Clients on port " + port + ".");
        Socket socket = serverSocket.accept();
        if(!keepGoing)
          break;
        Delegate t = new Delegate(socket, this);
        delegates.add(t);
        t.start();
      }
      try {
        serverSocket.close();
        for (Delegate dg : delegates) {
          try {
            dg.in.close();
            dg.out.close();
            dg.socket.close();
          } catch(IOException ex) { }
        }
      } catch(Exception e) {
        display("Exception closing the server and clients: " + e);
      }
    } catch (IOException e) {
      String msg = formatter.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
      display(msg);
    }
  }   

  protected void stop() {
    keepGoing = false;
    try {
      new Socket("localhost", port);
    }
    catch(Exception e) { }
  }

  public void display(String msg) {
    String time = formatter.format(new Date()) + " " + msg;
    System.out.println(time);
  }

  public synchronized void broadcast(String message) {
    String time = formatter.format(new Date());
    String messageLf = time + " " + message + "\n";
    for (Delegate dg : delegates) {
      if(!dg.send(messageLf)) {
        remove(dg);
        display("Disconnected Client " + dg.username + " removed from list.");
      }
    }
  }

  synchronized void remove(Delegate dg) {
    delegates.remove(dg);
  }

  public Delegate find(String name){
    for (Delegate dg : delegates) {
      if(dg.username.equals(name)) return dg;
    }
    return null;
  }

  public ArrayList<User> serializedUsers(){
    ArrayList<User> users = new ArrayList<User>();
    for (Delegate dg : delegates) {
      users.add(new User(dg.id, dg.username));
    }
    return users;
  }

}

