package talk.client;

import java.net.*;
import java.io.*;
import java.util.*;
import talk.common.*;

public class Dispatcher extends Thread {
  private final Client client;

  Message message;

  public Dispatcher(Client client) {
    this.client = client;
  }

  public void run() {
    while (true) {
      try {
        message = (Message)client.in.readObject();
        System.out.println("read from server:"+message.getMessage());
      } catch (IOException e) {
        client.display("Server has close the connection: " + e);
        System.out.println(e);
        break;
      } catch (ClassNotFoundException e2) { }
    }
  }

  public String stackTraceToString(Throwable e) {
    StringBuilder sb = new StringBuilder();
    for (StackTraceElement element : e.getStackTrace()) {
      sb.append(element.toString());
      sb.append("\n");
    }
    return sb.toString();
  }

}
