/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import ServeurJeu.IServeurJeu;
import ServeurJeu.modele.IPartie;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class Client implements IClient,Serializable{

    public static enum ETAT_CLIENT {DECONNECTE,RECHERCHE_PARTIE, EN_PARTIE, EN_JEU};
    public static enum ETAT_JOUEUR {JOUE, JOUE_PAS}
    
    private ETAT_CLIENT etatClient;
    private ETAT_JOUEUR etatJoueur;
    
    private String login;
    private String password;
    private IPartie partie;
    private IServeurJeu serveurJeu;
    private Tchat tchat;
    
    public Client(String login, String password){
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
        return this;
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
    
    public String getLogin(){
        return this.login;
    }

    @Override
    public String toString() {
        return getLogin();
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
    
    public IClient seConnecter() throws RemoteException{
        return serveurJeu.seConnecter(this);
    }
    
    public void seDeconnecter() throws RemoteException{
        serveurJeu.seDeconnecter(this);
    }
    
    public void lancerPartie() throws RemoteException {
        serveurJeu.lancerPartie(this, partie);
        //partie.lancerPartie(this);
    }
    public void creerPartie(String nomPartie, int nombreDeJoueurMax){
        try {
            IPartie p = this.serveurJeu.creerPartie(this,nomPartie,nombreDeJoueurMax);
            p.addClient(this);
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

    public ArrayList<IPartie> getListePartie() throws RemoteException {
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
}
