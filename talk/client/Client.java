package talk.client;

import java.net.*;
import java.io.*;
import java.util.*;
import talk.common.*;

public class Client {

  public ObjectInputStream in;
  public ObjectOutputStream out;
  private Socket socket;

  private String server, username;
  private int port;
  private String uid;

  Client(String server, int port, String username) {
    this.server = server;
    this.port = port;
    this.username = username;
  }
  
  public boolean connect() {
    try {
      socket = new Socket(server, port);
    } catch(Exception ec) {
      display(Helpers.stackTraceToString(ec));
      return false;
    }
    
    String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
    display(msg);
  
    try {
      in = new ObjectInputStream(socket.getInputStream());
      out = new ObjectOutputStream(socket.getOutputStream());
    } catch (IOException eIO) {
      display("Exception creating new Input/output Streams: " + eIO);
      return false;
    }
    try {
      out.writeObject(username);
      Message message = receive();
      setUID(message.getMessage());
      display("Conn:" + getUID());
    } catch (IOException eIO) {
      display("Exception doing login : " + eIO);
      disconnect();
      return false;
    }
    new Dispatcher(this).start();

    return true;
  }

  public void display(String msg) {
    System.out.println(msg);
  }
  
  public void send(Message msg) {
    try {
      out.writeObject(msg);
    } catch(IOException e) {
      display("Exception writing to server: " + e);
    }
  }

  public void disconnect() {
    try { 
      if(in != null) in.close();
    } catch(Exception e) {}
    try {
      if(out != null) out.close();
    } catch(Exception e) {}
    try {
      if(socket != null) socket.close();
    }catch(Exception e) {}
  }

  public Message receive(){
    try {
      return (Message)in.readObject();
    } catch(Exception e) { 
      display(Helpers.stackTraceToString(e));
      return new Message(Message.ERROR);
    } 
  }

  public void setUID(String str){
    this.uid = str;
  }

  public String getUID(){
    return this.uid;
  }

}
