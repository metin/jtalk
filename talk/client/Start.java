
package talk.client;
import java.net.*;
import java.io.*;
import java.util.*;
import talk.common.*;

public class Start {

  public static void main(String[] args) {

    for(String s : args) {
      System.out.print("arg:" + s);
    }
    int portNumber = 1500;
    String serverAddress = "localhost";
    String userName = args[args.length-1];
    Client client = new Client(serverAddress, portNumber, userName);

    if(!client.connect())
      return;

    Sender sender = new Sender(client);
    Scanner scan = new Scanner(System.in);
    RequestParser parser;
    while(true) {
      String msg = scan.nextLine();
      parser = new RequestParser(msg);
      Message to_send = parser.parse();
      sender.send(parser.parse());
      if(to_send.isLogout()) break;
    }
    client.disconnect();
  }
}

