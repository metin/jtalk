package talk.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import talk.common.*;


public class ClientGUI extends JFrame implements ActionListener {

  private static final long serialVersionUID = 1L;
  private JLabel label;
  private JTextField tf;
  private JTextField tfServer, tfPort;
  private JButton login, logout, whoIsIn;
  private JTextArea ta;
  private boolean connected;
  public Client client;
  private int defaultPort;
  private String defaultHost;
  private ContactsPanel contacts;

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
    conversations.setBackground(Color.black);
    JTabbedPane tabbedPane = new JTabbedPane();
    JComponent panel1 = makeTextPanel("General");
    tabbedPane.addTab("General", panel1);

    conversations.add(tabbedPane);
    add(conversations, c);

    JPanel textarea = new JPanel();
    textarea.setPreferredSize(new Dimension(450, 100));
    textarea.setLayout(new GridLayout(1, 1));
    c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 3;
    c.gridwidth = 4;
    c.gridheight = 1;
    c.fill = GridBagConstraints.BOTH;
    textarea.setBackground(Color.blue);
    textarea.add(new JTextArea());
    add(textarea, c);

    contacts = new ContactsPanel(this);
    c = new GridBagConstraints();
    c.gridx = 4;
    c.gridy = 0;
    c.gridwidth = 1;
    c.gridheight = 4;
    c.fill = GridBagConstraints.BOTH;
    contacts.setBackground(Color.red);
    contacts.setPreferredSize(new Dimension(150, 400));
    add(contacts, c);


    // // The NorthPanel with:
    // JPanel northPanel = new JPanel(new GridLayout(3,1));
    // // the client name anmd the port number
    // JPanel serverAndPort = new JPanel(new GridLayout(1,5, 1, 3));
    // // the two JTextField with default value for client address and port number
    // tfServer = new JTextField(host);
    // tfPort = new JTextField("" + port);
    // tfPort.setHorizontalAlignment(SwingConstants.RIGHT);

    // serverAndPort.add(new JLabel("Server Address:  "));
    // serverAndPort.add(tfServer);
    // serverAndPort.add(new JLabel("Port Number:  "));
    // serverAndPort.add(tfPort);
    // serverAndPort.add(new JLabel(""));
    // // adds the Server an port field to the GUI
    // northPanel.add(serverAndPort);

    // // the Label and the TextField
    // label = new JLabel("Enter your username below", SwingConstants.CENTER);
    // northPanel.add(label);
    // tf = new JTextField("Anonymous");
    // tf.setBackground(Color.WHITE);
    // northPanel.add(tf);
    // add(northPanel, BorderLayout.NORTH);

    // // The CenterPanel which is the chat room
    // ta = new JTextArea("Welcome to the Chat room\n", 80, 80);
    // JPanel centerPanel = new JPanel(new GridLayout(1,1));
    // centerPanel.add(new JScrollPane(ta));
    // ta.setEditable(false);
    // add(centerPanel, BorderLayout.CENTER);

    // // the 3 buttons
    // login = new JButton("Login");
    // login.addActionListener(this);
    // logout = new JButton("Logout");
    // logout.addActionListener(this);
    // logout.setEnabled(false);
    // whoIsIn = new JButton("Who is in");
    // whoIsIn.addActionListener(this);
    // whoIsIn.setEnabled(false);

    // JPanel southPanel = new JPanel();
    // southPanel.add(login);
    // southPanel.add(logout);
    // southPanel.add(whoIsIn);
    // add(southPanel, BorderLayout.SOUTH);

    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(600, 600);
    setVisible(true);
    //tf.requestFocus();
    int portNumber = 1500;
    String serverAddress = "localhost";
    String userName = "MetinGUI";
    client = new Client(serverAddress, portNumber, userName, this);
    client.connect();
  }

  public void messageReceived(Message message){
    switch(message.getType()) {
      case Message.MESSAGE:
        ta.append(message.getMessage());
        break;
      case Message.USER:
        OneToOneMessage oms = (OneToOneMessage) message;
        ta.append("[" + oms.from + "]: " + oms.getMessage());
        break;
      case Message.WHOISIN:
        contacts.loadContacts(message);
        break;
    }
  }

  protected JComponent makeTextPanel(String text) {
    JPanel panel = new JPanel(false);
    ta = new JTextArea(80, 80);
    JScrollPane scrollPane = new JScrollPane(ta);
    setPreferredSize(new Dimension(450, 110));
    panel.setLayout(new GridLayout(1, 1));
    panel.add(scrollPane);
    return panel;
  }

  public void actionPerformed(ActionEvent e) {
    Object o = e.getSource();
    if(o == logout) {
      client.send(new Message(Message.LOGOUT));
      return;
    }
    if(o == whoIsIn) {
      client.send(new Message(Message.WHOISIN));
      return;
    }

    if(connected) {
      client.send(new Message(Message.MESSAGE, tf.getText()));
      tf.setText("");
      return;
    }


  }

  // to start the whole thing the client
  public static void main(String[] args) {
    new ClientGUI("localhost", 1500);
  }

}

