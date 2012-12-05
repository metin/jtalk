package talk.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import talk.common.*;
import java.util.*;

public class ContactsPanel extends JPanel implements ActionListener {
  public ClientGUI clientGUI;
  private JTextField tfServer, tfPort, uname;
  private JButton login, logout;
  private ContactsList contacts;
  DefaultListModel contactsListModel = new DefaultListModel();

  ContactsPanel(ClientGUI clientGUI) {
    this.clientGUI = clientGUI;
    setLayout (new BorderLayout());
    JPanel connection = new JPanel();
    connection.setLayout(new GridLayout(4, 2));
    tfServer = new JTextField();
    tfPort = new JTextField();
    connection.add(new JLabel("Server:") );
    tfServer = new JTextField("localhost");
    connection.add(tfServer);
    connection.add(new JLabel("Port:") );
    tfPort = new JTextField("1500");
    connection.add(tfPort);
    connection.add(new JLabel("Name:") );
    uname = new JTextField("Anonymous");
    connection.add(uname);

    login = new JButton("Login");
    login.addActionListener(this);
    connection.add(login);
    add(connection, BorderLayout.NORTH);

    contacts = new ContactsList(contactsListModel, this);
    contacts.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    contacts.setLayoutOrientation(JList.VERTICAL);
    contacts.setVisibleRowCount(-1);
    JScrollPane listScroller = new JScrollPane(contacts);
    listScroller.setPreferredSize(new Dimension(250, 80));
    add(listScroller, BorderLayout.CENTER);

  }

  public void loadContacts(Message message){
    contactsListModel.clear();
    UserList ul = (UserList) message;
    ArrayList<User> users = ul.getUsers();
    for(int i = 0; i < users.size(); i++) {
      User u = users.get(i);
      contactsListModel.addElement(u.getId() + " :" + u.getName());
      System.out.println("-----" + u.getId() + " :" + u.getName());
    }
  }

  public void actionPerformed(ActionEvent e) {
    Object o = e.getSource();
    if(o == logout) {
      //client.send(new Message(Message.LOGOUT));
      return;
    }

    if(o == login) {
      String username = uname.getText().trim();
      if(username.length() == 0)
        return;
      String server = tfServer.getText().trim();
      if(server.length() == 0)
        return;
      String portNumber = tfPort.getText().trim();
      if(portNumber.length() == 0)
        return;
      int port = 0;
      try {
        port = Integer.parseInt(portNumber);
      }
      catch(Exception en) {
        return;
      }

      clientGUI.client = new Client(server, port, username, clientGUI);
      clientGUI.client.connect();

      Sender sender = new Sender(clientGUI.client);
      RequestParser parser = new RequestParser("whoisin");
      Message to_send = parser.parse();
      sender.send(parser.parse());

      // if(!client.connect())
      //   return;
      // tf.setText("");
      // label.setText("Enter your message below");
      // connected = true;
      // login.setEnabled(false);
      // logout.setEnabled(true);
      // whoIsIn.setEnabled(true);
      // tfServer.setEditable(false);
      // tfPort.setEditable(false);
      // tf.addActionListener(this);
    }


  }



}

