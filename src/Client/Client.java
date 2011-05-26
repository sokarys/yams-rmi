/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import ServeurJeu.IServeurJeu;
import ServeurJeu.ServeurJeu;
import ServeurJeu.modele.IPartie;
import ServeurJeu.modele.Partie;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;


/**
 *
 * @author root
 */

@XmlRootElement(name = "Client")
public class Client extends UnicastRemoteObject implements IClient,Serializable,Runnable{
    

    public static enum ETAT_CLIENT {DECONNECTE,RECHERCHE_PARTIE, EN_PARTIE, EN_JEU};
    public static enum ETAT_JOUEUR {JOUE, JOUE_PAS}
    
    //@XmlElement()
    private transient ETAT_CLIENT etatClient;
    //@XmlElement()
    private transient ETAT_JOUEUR etatJoueur;
    //@XmlElement()
    private String login;
    //@XmlElement()
    private String password;
    private transient IPartie partie;
    private transient IServeurJeu serveurJeu;
    private transient Tchat tchat;
    //@XmlElement()
    private transient boolean quitter;
    
    
    public Client() throws RemoteException{       
        //this("","");
    }
    
    public Client(String login, String password) throws RemoteException{
        super();
        this.login = login;
        this.password = password;
        etatClient = ETAT_CLIENT.DECONNECTE;
        etatJoueur = ETAT_JOUEUR.JOUE_PAS;
        //listePartie = new ArrayList<IPartie>();
        partie = null;
      //  tchat = new Tchat("Tchat " + login, this);
        //tchat.setVisible(true);
        try {
            serveurJeu = (IServeurJeu) Naming.lookup("rmi://127.0.0.1:2000/IServeurJeu");
        } catch (NotBoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void setListePartie(ArrayList<IPartie> listeP) throws RemoteException {
        //listePartie = listeP;
        System.out.println("mISE AJOUR LISTE PARTIE");
    }

    @Override
    public void setMessage(IClient c, String message) throws RemoteException {
       // tchat.addMesage(c, message);
        System.out.println(message);
    }

    @Override
    public void setMessage(IClient c, String message, IPartie p) throws RemoteException {
        tchat.addMesage(c, message);
    }

    @Override
    public void setEtatClient(ETAT_CLIENT etat) throws RemoteException {
        this.etatClient = etat;
    }

    @Override
    public void setEtatJoueur(ETAT_JOUEUR etat) throws RemoteException {
        this.etatJoueur = etat;
    }
    
    @Override
    public void setPartie(IPartie p) throws RemoteException {
        this.partie = p;
    }

    @Override
    public Client getClient() throws RemoteException {
        return ((Client) this);
    }
    
    public void envoieMessage(String message){
        try {
                serveurJeu.envoyerMessage(this,message);
            } catch (RemoteException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    public void envoieMessagePartie(String message){
        if(this.partie != null){
            try {
                serveurJeu.envoyerMessage(this,this.partie,message);
            } catch (RemoteException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
   

    @Override
    public String toString() {
        return getLogin();
    }
    
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
    
    public ETAT_CLIENT getEtatClient() {
        return etatClient;
    }
    
    
    public ETAT_JOUEUR getEtatJoueur() {
        return etatJoueur;
    }
    
    public boolean enPartie(){
        return getEtatClient() == Client.ETAT_CLIENT.EN_PARTIE || getEtatClient() == Client.ETAT_CLIENT.EN_JEU;
    }
    
    public void seConnecter() throws RemoteException{
        serveurJeu.seConnecter(this);
    }
    
    public void seDeconnecter() throws RemoteException{
        serveurJeu.seDeconnecter(this);
    }
    
    public void lancerPartie() throws RemoteException {
        //serveurJeu.lancerPartie(this, partie);
        partie.lancerPartie(this);
    }
    public void creerPartie(String nomPartie, int nombreDeJoueurMax){
        try {
            IPartie p = this.serveurJeu.creerPartie(this,nomPartie,nombreDeJoueurMax);
            //p.addClient(this);
            this.setPartie(p);
            this.etatClient = ETAT_CLIENT.EN_PARTIE;
        } catch (RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void rejoindrepartie(int numPartie) throws RemoteException{
        
            if(numPartie < getListePartie().size()){
                //this.serveurJeu.rejoindrePartie(this.listePartie.get(numPartie), this);
                this.getListePartie().get(numPartie).addClient(this);
                this.setPartie(this.getListePartie().get(numPartie));
                this.etatClient = ETAT_CLIENT.EN_PARTIE;
            }
        
    }

    public ArrayList<Partie> getListePartie() throws RemoteException {
        return serveurJeu.getListepartie();
    }
    
    @Override
    public int jouer() throws RemoteException{
        Scanner sc = new Scanner(System.in);
        System.out.println("Nb : ");
        sc.nextLine();
        System.out.println("Jouer");
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return this.login.equals(((Client) obj).login);
    }
    
    public void setTchat(Tchat t){
        this.tchat = t;
    }

    public IServeurJeu getServeurJeu() {
        return serveurJeu;
    }

    public void setServeurJeu(ServeurJeu serveurJeu) throws RemoteException {
        this.serveurJeu = serveurJeu;
    }
    
    public boolean enJeu(){
        return this.getEtatJoueur() == Client.ETAT_JOUEUR.JOUE;
    }
    
    @Override
    public void run() {
       while (!quitter) {
                if (!this.enPartie()) {
                    afficherMenu();
                    choix();
                }else if(this.enPartie() && !this.enJeu()){
                    afficherMenu();
                    choix();
                }
        }
    }
    
     private void choix() {
        try {
            Scanner sc    = new Scanner(System.in);
            int     choix = Integer.valueOf(sc.nextLine().replace("\n", ""));
            int     nb;

            System.out.println(this.getEtatJoueur());

            switch (choix) {
            case 1 :
                System.out.println("Login");
                login = sc.nextLine().replace("\n", "");
                System.out.println("Mot de passe : ");
                password = sc.nextLine().replace("\n", "");
                this.seConnecter();
    //            tchat  = new Tchat("Tchat", client);
  //              tchat.setClient(client);
//                client.getClient().setTchat(tchat);

                break;

            case 2 :
                this.seDeconnecter();

                break;

            case 0 :
                quitter = true;

                break;

            case 3 :
                System.out.print("Nom partie : ");

                String nom = sc.nextLine().replace("\n", "");

                System.out.print("Nb Joueur");
                nb = Integer.valueOf(sc.nextLine().replace("\n", ""));
                this.creerPartie(nom, nb);

                break;

            case 4 :
                System.out.println("Num Partie");
                nb = Integer.valueOf(sc.nextLine().replace("\n", ""));
                this.rejoindrepartie(nb);

                break;

            case 5 :
                System.out.println(this.getListePartie());

                break;

            case 6 :
                this.lancerPartie();

                break;

            case 7 :
                System.out.print("Message : ");

                String message = sc.nextLine().replace("\n", "");

                this.envoieMessage(message);

                break;
            }
        } catch (RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public void setPartie(Partie partie) {
        this.partie = partie;
    }

    
    public boolean isQuitter() {
        return quitter;
    }

    public void setQuitter(boolean quitter) {
        this.quitter = quitter;
    }
    
    
}
