
package talk.client;
import java.net.*;
import java.io.*;
import java.util.*;
import talk.common.*;

public class Start {

  public static void main(String[] args) {

    int portNumber = 1500;
    String serverAddress = "localhost";
    String userName = args[0];

    Client client = new Client(serverAddress, portNumber, userName);

    if(!client.connect())
      return;

    Scanner scan = new Scanner(System.in);
    while(true) {
      System.out.print("> ");
      String msg = scan.nextLine();
      if(msg.equalsIgnoreCase("LOGOUT")) {
        client.sendMessage(new Message(Message.LOGOUT, ""));
        break;
      }
      else if(msg.equalsIgnoreCase("WHOISIN")) {
        client.sendMessage(new Message(Message.WHOISIN, ""));
      }
      else {
        client.sendMessage(new Message(Message.MESSAGE, msg));
      }
    }
    client.disconnect();
  }

}

