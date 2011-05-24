/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServeurJeu.modele;

import Client.Client;
import Client.IClient;
import ServeurJeu.IServeurJeu;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class Partie implements IPartie,Serializable,Runnable{
    private ArrayList<IClient> listeClient;
    private HashMap<IClient,ScoreClient> listeScoreClient;
    
    private int nombreJoueurMax;
    private String nomPartie;
    private transient Thread thread;
    private IClient admin = null;
    private IServeurJeu serveurJeu;
    
    public Partie(String nomPartie, int nombreJoueurMax) throws MalformedURLException{
        this.nomPartie = nomPartie;
        this.nombreJoueurMax = nombreJoueurMax;
        this.listeClient = new ArrayList<IClient>();
        this.listeScoreClient = new HashMap<IClient, ScoreClient>();
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
    
    public void addScore(IClient c,Regles.TYPESCORE type, Main m){
        listeScoreClient.get(c).addScore(type, m.getScrore(type));
    }
    
    @Override
    public void lancerPartie(IClient c) throws RemoteException{
        System.out.println(c + "   " + admin);
        if(c.equals(admin)){
            thread = new Thread(this);
            thread.start();
            for(IClient cc : listeClient){
                cc.setMessage(c, "PartieLancer");
            }
        }
    }

    @Override
    public synchronized void run() {
        System.out.println("lancer");
       for(int i=0; i<Regles.TYPESCORE.values().length; i++){
           for(int j=0; j<listeClient.size(); j++){
                try {
                    //serveurJeu.jouer(this, listeClient.get(j));
                    listeClient.get(j).jouer();
                } catch (RemoteException ex) {
                    Logger.getLogger(Partie.class.getName()).log(Level.SEVERE, null, ex);
                }
           }
       }
        System.out.println("fin lancer");
        
        try {
            finPartie();
        } catch (RemoteException ex) {
            Logger.getLogger(Partie.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void finPartie() throws RemoteException{
        this.listeClient.clear();
        this.listeScoreClient.clear();
        this.serveurJeu.dellPartie(this);
    }

    @Override
    public void addClient(IClient c) throws RemoteException {
        try {
            System.out.println("Client " + c.getClient() + " a rejoint la partie " + this.nomPartie);
           if(!listeClient.contains(c) && listeClient.size() < nombreJoueurMax){
               listeClient.add(c);
               listeScoreClient.put(c,new ScoreClient());
               if(listeClient.size() == 1){
                   admin = c;
               }
               System.out.println("Client " + c.getClient() + " a rejoint la partie " + this.nomPartie);
               if(listeClient.size() == nombreJoueurMax){
                   System.out.println("Partie Remplie");
               }
           }
        } catch (RemoteException ex) {
            Logger.getLogger(Partie.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void dellClient(IClient c) throws RemoteException {
        listeClient.remove(c);
        if(c.equals(admin) && !listeClient.isEmpty()){
            admin = listeClient.get(0);
            listeScoreClient.remove(c);
            admin.setMessage(admin, "Vous etes le nouvel admin.", this);
        }
        if(listeClient.isEmpty()){
            this.serveurJeu.dellPartie(this);
        }
    }

    @Override
    public String toString() {
        return this.nomPartie + " " + this.nombreJoueurMax;
    }

    

    @Override
    public boolean equals(Object obj) {
        return this.nomPartie.equals(((Partie)obj).nomPartie);
    }

    @Override
    public void afficherScore() throws RemoteException {
        
    }
    
}
