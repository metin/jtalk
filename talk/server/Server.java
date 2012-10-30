package talk.server;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import talk.common.*;

public class Server {
  public static int uniqueId;
  public ArrayList<Delegate> al;
  public DateFormatter formatter;
  private int port;
  private boolean keepGoing;

  public Server(int port) {
    this.port = port;
    formatter = new DateFormatter();
    al = new ArrayList<Delegate>();
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
        al.add(t);
        t.start();
      }
      try {
        serverSocket.close();
        for(int i = 0; i < al.size(); ++i) {
          Delegate tc = al.get(i);
          try {
            tc.in.close();
            tc.out.close();
            tc.socket.close();
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
    System.out.print(messageLf);
    
    // we loop in reverse order in case we would have to remove a Client
    // because it has disconnected
    for(int i = al.size(); --i >= 0;) {
      Delegate ct = al.get(i);
      // try to write to the Client if it fails remove it from the list
      if(!ct.send(messageLf)) {
        al.remove(i);
        display("Disconnected Client " + ct.username + " removed from list.");
      }
    }
  }

  // for a client who logoff using the LOGOUT message
  synchronized void remove(int id) {
    // scan the array list until we found the Id
    for(int i = 0; i < al.size(); ++i) {
      Delegate ct = al.get(i);
      // found it
      if(ct.id == id) {
        al.remove(i);
        return;
      }
    }
  }

  public Delegate find(String name){
    for(int i = 0; i < al.size(); ++i) {
      Delegate ct = al.get(i);
      // found it
      if(ct.username.equals(name)) {
        return ct;
      }
    }
    return null;
  }

  public ArrayList<User> serializedUsers(){
    ArrayList<User> users = new ArrayList<User>();
    for(int i = 0; i < al.size(); i++) {
      Delegate ct = al.get(i);
      users.add(new User(ct.id, ct.username));
    }
    return users;
  }

}

