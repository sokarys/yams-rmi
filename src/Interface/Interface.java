/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Client.Client;
import Client.IClient;
import Client.Tchat;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class Interface implements Runnable{
    private boolean quitter;
    private IClient client;
    private Thread thread;
    private Tchat tchat;
    
    public Interface(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Login");
        String name = sc.nextLine().replace("\n", "");
        System.out.println("Mot de passe : not implemented Eyt");
        String mdp = sc.nextLine().replace("\n", "");
        this.client = new Client(name,mdp);
    }
    
    
    private void afficherMenu() {
        System.out.println("1 - se Connecter");
        System.out.println("2 - Se Deconnecter");
        System.out.println("3 - creer Partie");
        System.out.println("4 - rejoindre Partie");
        System.out.println("5 - liste des parties");
        System.out.println("6 - lancer partie");
        System.out.println("7 - Envoyer un message");
        System.out.println("0 - Quitter");
    }

    public void lancer() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while(!quitter){
            try {
                if(!client.getClient().enPartie()){
                    afficherMenu();
                    choix();
                }else{
                    System.out.println("En partie");
                    choix();
                }
            } catch (RemoteException ex) {
                Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void choix() {
        try {
            Scanner sc = new Scanner(System.in);
            int choix = Integer.valueOf(sc.nextLine().replace("\n", ""));
            int nb;
            System.out.println(client.getClient().getEtatJoueur());
            switch(choix){
                case 1 :
                    client = client.getClient().seConnecter();
                    tchat = new Tchat("Tchat", client);
                    tchat.setClient(client);
                    client.getClient().setTchat(tchat);
                    break;
                case 2 :
                    client.getClient().seDeconnecter();
                    break;
                case 0 :
                    quitter = true;
                    break;
                case 3 :
                    System.out.print("Nom partie : ");
                    String nom = sc.nextLine().replace("\n", "");
                    System.out.print("Nb Joueur");
                    nb = Integer.valueOf(sc.nextLine().replace("\n", ""));
                    client.getClient().creerPartie(nom, nb);
                    break;
                case 4 : 
                    System.out.println("Num Partie");
                    nb = Integer.valueOf(sc.nextLine().replace("\n", ""));
                    client.getClient().rejoindrepartie(nb);
                    break;
                case 5 :                    
                    System.out.println(client.getClient().getListePartie());
                    break;
                case 6 :
                    client.getClient().lancerPartie();
                    break;
                case 7 :
                    System.out.print("Message : ");
                    String message = sc.nextLine().replace("\n", "");
                    client.getClient().envoieMessage(message);
                    break;
            }
        } catch (RemoteException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
