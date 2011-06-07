/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yamsv2;

import Client.Client;
import ServeurJeu.ServeurJeu;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class YAMSV2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        if(args.length == 1){
		if(args[0].toUpperCase().equals("SERVEUR")){
			 try {
			    ServeurJeu j = new ServeurJeu(2000);
			} catch (RemoteException ex) {
			    Logger.getLogger(YAMS.class.getName()).log(Level.SEVERE, null, ex);
			} catch (AlreadyBoundException ex) {
			    Logger.getLogger(YAMS.class.getName()).log(Level.SEVERE, null, ex);
			}
		}else if(args[0].toUpperCase().equals("CLIENT")){
			Client c = new Client("","");
			Thread t = new Thread(c);
			t.start();
		}else{
			System.out.println("Erreur ARGUMENT");
		}
	}else{
		Client c = new Client("","");
		Thread t = new Thread(c);
		t.start();
	}
    }
}
