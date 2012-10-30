package talk.client;
import java.net.*;
import java.io.*;
import java.util.*;
import talk.common.*;

public class RequestParser {
  private String request;
  private String[] request_arr;

  public RequestParser(){
    this("");
  }

  public RequestParser(String str){
    request = str;
    request_arr = request.trim().split(":");
  }

  public Message parse() {
    Message m = new Message(Message.ERROR);
    String rtype = request_arr[0].trim().toUpperCase();
    if(rtype.equals("LOGOUT")) {
      m = new Message(Message.LOGOUT);
    }
    else if(rtype.equals("WHOISIN")) {
      m = new Message(Message.WHOISIN);
    }
    else if(rtype.equals("TO")) {
      m = new OneToOneMessage(request_arr[1], request_arr[2]);
    }
    else if(rtype.equals("ALL")) {
      m = new Message(Message.MESSAGE, request_arr[1]);
    }
    return m;
  }

}
