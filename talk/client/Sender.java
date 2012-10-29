package talk.client;
import java.net.*;
import java.io.*;
import java.util.*;
import talk.common.*;

public class Sender {
  private final Client client;

  public Sender(Client cl){
    client = cl;
  }

  public void send(Message message) {
    client.send(message);
  }

}

