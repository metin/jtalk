package talk.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import talk.common.*;
import java.util.*;
import javax.swing.event.*;

public class ConversationTab extends JPanel implements Comparable, ActionListener {
  private JTextArea ta = new JTextArea(80, 80);
  private String user;

  ConversationTab() {
    super();
    ta.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(ta);
    setPreferredSize(new Dimension(450, 110));
    setLayout(new GridLayout(1, 1));
    add(scrollPane);
    //addActionListener(this);
  }

  ConversationTab(String user) {
    this();
    this.user = user;
  }

  public void add(Message message){
    ta.append(message.from + ": " + message.getMessage() + "\n");
  }

  public void add(String message){
    ta.append(message + "\n");
  }

  public String getUser(){
    return user;
  }

  @Override
  public int compareTo(Object o) {
    ConversationTab dg = (ConversationTab) o;
    return this.user.compareToIgnoreCase(dg.user);
  }

  public void actionPerformed(ActionEvent e) {
    Object o = e.getSource();
  }

  public String getMessage(String toSend){
    if(user == null)
      return "ALL:"+toSend;
    else
      return "TO:"+user+":"+toSend;
  }

  public boolean isGeneral(){
    return user == null;
  }
}
