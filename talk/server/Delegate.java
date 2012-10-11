package talk.server;
import java.net.*;
import java.io.*;
import java.util.*;
import talk.common.*;

public class Delegate extends Thread {
  Socket socket;
  ObjectInputStream in;
  ObjectOutputStream out;
  int id;
  String username;
  Message cm;
  String date;
  Server server;

  Delegate(Socket socket, Server server) {
    id = ++server.uniqueId;
    this.socket = socket;
    this.server = server;
    System.out.println("Thread trying to create Object Input/Output Streams");
    try {
      out = new ObjectOutputStream(socket.getOutputStream());
      in  = new ObjectInputStream(socket.getInputStream());
      username = (String) in.readObject();
      display(username + " just connected.");
    } catch (IOException e) {
      display("Exception creating new Input/output Streams: " + e);
      return;
    } catch (ClassNotFoundException e) { }
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
      catch (IOException e) {
        display(username + " Exception reading Streams: " + e);
        break;
      } catch(ClassNotFoundException e2) {
        break;
      }

      String message = cm.getMessage();
      switch(cm.getType()) {
        case Message.MESSAGE:
          broadcast(username + ": " + message);
          break;
        case Message.LOGOUT:
          display(username + " disconnected with a LOGOUT message.");
          keepGoing = false;
          break;
        case Message.WHOISIN:
          writeMsg("List of the users connected at " + server.sdf.format(new Date()) + "\n");
          for(int i = 0; i < server.al.size(); ++i) {
            Delegate ct = server.al.get(i);
            writeMsg((i+1) + ") " + ct.username + " since " + ct.date);
          }
          break;
      }
    }
    server.remove(id);
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

  public boolean writeMsg(String msg) {
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
}
