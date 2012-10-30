package talk.common;

import java.io.*;

public class User implements Serializable {

  static final long serialVersionUID = 5219383205614652852L;

  public int _id;
  public String _name;

  public User(int id, String name) {
    _id = id;
    _name = name;
  }

  public String getName() {
    return _name;
  }

  public int getId() {
    return _id;
  }

}
