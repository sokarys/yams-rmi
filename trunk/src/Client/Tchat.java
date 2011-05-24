/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import ServeurJeu.modele.Partie;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author root
 */
public class Tchat extends JFrame  implements Serializable{
    private JTextArea tchat;
    private JTextField message;
    private JButton envoieMessage;
    private IClient client;
    
    public Tchat(String title, IClient c){
        this.client = c;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle(title);
        initComponent();
    }
    
    private void initComponent(){
        this.setLayout(new FlowLayout());
        this.setPreferredSize(new Dimension(800,600));
        this.setSize(new Dimension(800,600));
        tchat = new JTextArea();
        message = new JTextField();
        message.setColumns(50);
        envoieMessage = new JButton("Envoie");
        this.setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));
        this.add(new JScrollPane(tchat));
        JPanel pane = new JPanel();
        pane.setLayout(new FlowLayout());
        pane.add(message);
        pane.add(envoieMessage);
        this.add(pane);
        
        envoieMessage.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    envoieMessage(message.getText());
                } catch (RemoteException ex) {
                    Logger.getLogger(Tchat.class.getName()).log(Level.SEVERE, null, ex);
                }
                message.setText("");
            }
        });
       // this.setVisible(true);
    }
    
    public void addMesage(IClient c, String message) throws RemoteException{
        if(this.client.getClient().enPartie()){
            tchat.setText(tchat.getText() + "\n(Partie) " + c.getClient() + " : " +message);
        }else {
            tchat.setText(tchat.getText() + "\n" + c.getClient() + " : " +message);
        }
    }
    
    private void envoieMessage(String message) throws RemoteException{
        if(this.client.getClient().enPartie()){
            client.getClient().envoieMessagePartie(message);
        }else{
            client.getClient().envoieMessage(message);
        }
    }

    public void setClient(IClient client) {
        this.client = client;
    }
    
    
    
    
}
