package talk.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import talk.common.*;
import java.util.*;
import javax.swing.event.*;

public class ContactsList extends JList {
  private ContactsPanel contactsPanel;
  ContactsList(DefaultListModel contactsListModel, ContactsPanel contactsPanel) {
    super(contactsListModel);
    this.contactsPanel = contactsPanel;
    this.addMouseListener(new ConatacsListMouseAdapter());
  }

  class ConatacsListMouseAdapter implements MouseListener {

    public void mouseClicked(MouseEvent e) {
      ContactsList cl = (ContactsList) e.getSource();
      if (e.getClickCount() == 2) {
        int index = cl.locationToIndex(e.getPoint());
        try {
          String us = (String)cl.getModel().getElementAt(index);
          System.out.println("Double clicked on Item " + index + ": us:" + us);
          String[] uname = us.split(":");
          contactsPanel.clientGUI.findOrCreateTab(uname[uname.length-1]);
        } catch (Exception ex) { }
      }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

  }

}


