
package talk.server;
import java.net.*;
import java.io.*;
import java.util.*;
import talk.common.*;

public class Start  {
  public static void main(String[] args) {

    int portNumber = 1500;
    switch(args.length) {
      case 1:
        try {
          portNumber = Integer.parseInt(args[0]);
        }
        catch(Exception e) {
          System.out.println("Invalid port number.");
          System.out.println("Usage is: > java Server [portNumber]");
          return;
        }
      case 0:
        break;
      default:
        System.out.println("Usage is: > java Server [portNumber]");
        return;
    }
    Server server = new Server(portNumber);
    server.bootUp();
  }
}
