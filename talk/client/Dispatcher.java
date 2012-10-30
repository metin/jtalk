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
      switch(message.getType()) {
        case Message.MESSAGE:
          display("from server:" + message.getMessage());
          break;
        case Message.USER:
          OneToOneMessage oms = (OneToOneMessage) message;
          display("from" + oms.from + " :" + oms.getMessage());
          break;
        case Message.WHOISIN:
          UserList ul = (UserList) message;
          ArrayList<User> users = ul.getUsers();
          for(int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            display(""+i+")" + u.getId() + " :" + u.getName());
          }
          
      }
      if(message.isError()) break;
    }
  }

  public void display(String msg){
    System.out.println(msg);
  }

}
