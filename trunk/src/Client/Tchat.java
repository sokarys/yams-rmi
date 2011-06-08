/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import ServeurJeu.modele.Partie;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.MenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

/**
 *
 * @author root
 */
public class Tchat extends JFrame  implements Serializable{
    private JTextPane tchat;
    private JTextField message;
    private JButton envoieMessage;
    private IClient client;
    private String donne = "";
    private JMenuBar menuBar;
    private JMenu file;
    private JMenuItem quitter,connect,disconnect;
    
    public Tchat(String title, IClient c){
        this.client = c;
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setTitle(title);
        initComponent();
    }
    
    private void initComponent(){
        this.setLayout(new FlowLayout());
        this.setPreferredSize(new Dimension(800,600));
        this.setSize(new Dimension(800,600));
        tchat = new JTextPane();
	tchat.setContentType("text/html");
	tchat.setEditable(false);
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
	this.menuBar = new JMenuBar();
	this.file = new JMenu("File");
	this.quitter = new JMenuItem("Quitter");
	
	menuBar.add(file);
	file.add(quitter);
	
	this.setJMenuBar(menuBar);
        
        envoieMessage.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    sendMessage(message.getText());
                } catch (RemoteException ex) {
                    Logger.getLogger(Tchat.class.getName()).log(Level.SEVERE, null, ex);
                }
                message.setText("");
            }
        });
	
	message.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent ke) {
				
			}

			@Override
			public void keyPressed(KeyEvent ke) {
				if(ke.getKeyCode() == KeyEvent.VK_ENTER && !Tchat.this.message.getText().isEmpty()){
					try {
						Tchat.this.sendMessage(Tchat.this.message.getText());
						message.setText("");
					} catch (RemoteException ex) {
						Logger.getLogger(Tchat.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent ke) {
				
			}
		});
    }
    
    public void addMesage(String message) throws RemoteException{
	    donne += "<br/>" + message;
            tchat.setText(donne);
    }
    
    
    private void sendMessage(String message) throws RemoteException{
            client.envoieMessage(client.getName()+ " : " + message);
    }

    public void setClient(IClient client) {
        this.client = client;
    }
    
    
    
    
}
