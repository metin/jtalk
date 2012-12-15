
package talk.client;
import java.net.*;
import java.io.*;
import java.util.*;
import talk.common.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Start {

  public static void main(String[] args) {

    for(String s : args) {
      System.out.println("arg:" + s);
    }
    String uiType = args[1];
    System.out.println("utiype:" + uiType);
    if(uiType.equals("GUI")) {
      try {
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
      } catch(Exception e) {
        System.out.println("Error setting Motif LAF: " + e);
      }
      JFrame cl = new ClientGUI("localhost", 1500);
      cl.setSize(680, 480);
      cl.setVisible(true);
    } else {
      String userName = args[2];
      String serverAddress = args[3];
      int portNumber = 1500;
      try {
          portNumber = Integer.parseInt(args[4]);
      } catch (NumberFormatException e) {
          System.err.println("Argument" + " must be an integer");
          System.exit(1);
      }

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
}

