package talk.common;

import java.io.*;
import java.util.*;

public class UserList extends Message {

  public ArrayList<User> _users;

  public UserList(String receiver, ArrayList<User> users) {
    super(WHOISIN);
    _users = users;
  }

  public ArrayList<User> getUsers(){
    return  _users;
  }

}
