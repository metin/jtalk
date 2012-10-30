package talk.common;

import java.io.*;

public class OneToOneMessage extends Message {

  public String to;

  public OneToOneMessage(String receiver, String message) {
    super(USER, message);
    to = receiver;
  }

}
