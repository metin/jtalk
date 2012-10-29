package talk.client;
import java.net.*;
import java.io.*;
import java.util.*;
import talk.common.*;

public class RequestParser {
  private String request;

  public RequestParser(){
    this("");
  }

  public RequestParser(String str){
    request = str;
  }

  public Message parse() {
    Message m = new Message(Message.ERROR);
    if(request.equalsIgnoreCase("LOGOUT")) {
      m = new Message(Message.LOGOUT);
    }
    else if(request.equalsIgnoreCase("WHOISIN")) {
      m = new Message(Message.WHOISIN);
    }
    else {
      m = new Message(Message.MESSAGE, request);
    }
    return m;
  }

}

