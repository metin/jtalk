package talk.server;
import java.net.*;
import java.io.*;
import java.util.*;
import talk.common.*;

public class Delegate extends Thread implements Comparable {
  Socket socket;
  ObjectInputStream in;
  ObjectOutputStream out;
  int id;
  String username;
  Message cm;
  String date;
  Server server;
  public DateFormatter formatter = new DateFormatter();

  Delegate(Socket socket, Server server, int uid) {
    id = uid;
    this.socket = socket;
    this.server = server;
    System.out.println("Thread trying to create Object Input/Output Streams");
    try {
      out = new ObjectOutputStream(socket.getOutputStream());
      in  = new ObjectInputStream(socket.getInputStream());
      username = (String) in.readObject();
      display("User:"+username);
      send(Message.ACK, Integer.toString(id));
    } catch (IOException e) {
      display("Exception creating new Input/output Streams: " + e);
      return;
    } catch (ClassNotFoundException e) {
      display(Helpers.stackTraceToString(e));
    }
    date = new Date().toString() + "\n";
  }

  public void display(String msg) {
    server.display(msg);
  }

  public void broadcast(String message) {
    server.broadcast(message);
  }

  public void run() {
    boolean keepGoing = true;
    while(keepGoing) {
      try {
        cm = (Message) in.readObject();
      }
      catch (Exception e) {
        display(username + " Exception reading Streams: " + e);
        break;
      }

      String message = cm.getMessage();
      display("Type:"+cm.getType());
      switch(cm.getType()) {
        case Message.MESSAGE:
          broadcast(username + ": " + message);
          break;
        case Message.LOGOUT:
          display(username + " disconnected with a LOGOUT message.");
          keepGoing = false;
          break;
        case Message.WHOISIN:
          send("List of the users connected at " + formatter.format(new Date()) + "\n");
          UserList m = new UserList(username, server.serializedUsers());
          send(m);
          break;
        case Message.USER:
          OneToOneMessage oms = (OneToOneMessage) cm;
          Delegate dl = server.find(oms.to);
          oms.from = username;
          dl.send(oms);
          break;
      }
    }
    server.remove(this);
    close();
  }

  private void close() {
    try {
      if(out != null) out.close();
    }
    catch(Exception e) {}
    try {
      if(in != null) in.close();
    }
    catch(Exception e) {};
    try {
      if(socket != null) socket.close();
    }
    catch (Exception e) {}
  }

  public boolean send(String msg) {
    return send(Message.MESSAGE, msg);
  }

  public boolean send(int type, String msg) {
    Message m = new Message(type, msg);
    return send(m);
  }

  public boolean send(Message msg) {
    if(!socket.isConnected()) {
      close();
      return false;
    }
    try {
      out.writeObject(msg);
    }
    catch(IOException e) {
      display("Error sending message to " + username);
      display(e.toString());
    }
    return true;
  }
  @Override
  public int compareTo(Object o) {
    Delegate dg = (Delegate) o;
    return this.id - dg.id ;
  }

}
