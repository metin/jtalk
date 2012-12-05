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
    ta.append(message.getMessage()+"\n");
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
    // if(o == logout) {
    //   client.send(new Message(Message.LOGOUT));
    //   return;
    // }
    // if(o == whoIsIn) {
    //   client.send(new Message(Message.WHOISIN));
    //   return;
    // }

    // if(connected) {
    //   client.send(new Message(Message.MESSAGE, tf.getText()));
    //   tf.setText("");
    //   return;
    // }
  }

}
