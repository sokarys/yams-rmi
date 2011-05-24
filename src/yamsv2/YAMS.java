/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yamsv2;

import ServeurJeu.ServeurJeu;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class YAMS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            ServeurJeu j = new ServeurJeu(2000);
        } catch (RemoteException ex) {
            Logger.getLogger(YAMS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AlreadyBoundException ex) {
            Logger.getLogger(YAMS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
