package talk.client;

import java.net.*;
import java.io.*;
import java.util.*;
import talk.common.*;

public class Dispatcher extends Thread {
  private final Client client;
  private Message message;

  public Dispatcher(Client client) {
    this.client = client;
  }

  public void run() {
    while (true) {
      message = client.receive();
      System.out.println("read from server:" + message.getMessage());
      if(message.isError()) break;
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
