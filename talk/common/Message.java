package talk.common;

import java.io.*;

public class Message implements Serializable {

  static final long serialVersionUID = 5219383205614652851L;

  static public final int WHOISIN = 0, MESSAGE = 1, LOGOUT = 2;
  private int type;
  private String message;

  public Message(int type) {
    this(type, "");
  }

  public Message(int type, String message) {
    this.type = type;
    this.message = message;
  }

  public int getType() {
    return type;
  }

  public String getMessage() {
    return message;
  }
}
