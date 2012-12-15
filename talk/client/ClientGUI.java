package talk.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import talk.common.*;
import java.util.*;

public class ClientGUI extends JFrame implements ActionListener {

  private JButton send;
  private ConversationTab generalRoom;
  private boolean connected;
  public Client client;
  private int defaultPort;
  private String defaultHost;
  private ContactsPanel contacts;
  public ArrayList<ConversationTab> _userTabs;
  private JTabbedPane tabbedPane;
  private JTextArea messageArea;

  ClientGUI(String host, int port) {

    super("Chat Client");
    defaultPort = port;
    defaultHost = host;
    setLayout (new GridBagLayout());
    JPanel conversations = new JPanel();
    conversations.setLayout(new GridLayout(1, 1));
    conversations.setPreferredSize(new Dimension(450, 300));
    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 4;
    c.gridheight = 3;
    c.fill = GridBagConstraints.BOTH;
    tabbedPane = new JTabbedPane();
    generalRoom = new ConversationTab();
    tabbedPane.addTab("General", generalRoom);
    _userTabs = new ArrayList<ConversationTab>();

    conversations.add(tabbedPane);
    add(conversations, c);

    JPanel textarea = new JPanel();
    setPreferredSize(new Dimension(450, 110));

    textarea.setLayout(new GridLayout(1,1));
    messageArea = new JTextArea();
    JScrollPane scrollPane = new JScrollPane(messageArea);
    textarea.add(scrollPane);

    send = new JButton("Send");
    send.addActionListener(this);
    textarea.add(send);

    c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 3;
    c.gridwidth = 4;
    c.gridheight = 1;
    c.fill = GridBagConstraints.BOTH;
    add(textarea, c);

    contacts = new ContactsPanel(this);
    c = new GridBagConstraints();
    c.gridx = 4;
    c.gridy = 0;
    c.gridwidth = 1;
    c.gridheight = 4;
    c.fill = GridBagConstraints.BOTH;
    contacts.setPreferredSize(new Dimension(150, 400));
    add(contacts, c);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  public void messageReceived(Message message){
    switch(message.getType()) {
      case Message.MESSAGE:
        generalRoom.add(message);
        break;
      case Message.USER:
        OneToOneMessage oms = (OneToOneMessage) message;
        ConversationTab ct = findOrCreateTab(oms.from);
        ct.add(message);
        break;
      case Message.WHOISIN:
        contacts.loadContacts(message);
        break;
    }
  }

  public ConversationTab findOrCreateTab(String from){
    ConversationTab toCmp = new ConversationTab(from);
    ConversationTab found = findUserTab(toCmp);
    if(found == null){
      _userTabs.add(toCmp);
      tabbedPane.addTab(from, toCmp);
    }
    found = findUserTab(toCmp);
    tabbedPane.setSelectedComponent(found);
    return found;
  }

  public ConversationTab findUserTab(ConversationTab ct){
    for (ConversationTab ut : _userTabs) {
      System.out.println(ut.getUser()+"--" + ct.getUser());
      if(ut.getUser().equals(ct.getUser())) return ut;
    }
    return null;
  }


  public void actionPerformed(ActionEvent e) {
    ConversationTab curChat = (ConversationTab) tabbedPane.getSelectedComponent();
    String msg = curChat.getMessage(messageArea.getText());
    Sender sender = new Sender(client);
    RequestParser parser = new RequestParser(msg);
    Message to_send = parser.parse();
    sender.send(parser.parse());
    if(!curChat.isGeneral())
      curChat.add(client.getUsername() + ": "+ messageArea.getText());
    messageArea.setText("");
  }

//   public static void main(String[] args) {
//     try {
//       //UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
//       UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//     } catch(Exception e) {
//       System.out.println("Error setting Motif LAF: " + e);
//     }
//     JFrame cl = new ClientGUI("localhost", 1500);
//     cl.setSize(700, 500);
// //    cl.addWindowListener(new ExitListener());
//     cl.setVisible(true);

//   }

}

