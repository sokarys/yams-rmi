/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServeurJeu.modele;

import Client.Client;
import Client.IClient;
import ServeurJeu.IServeurJeu;
import ServeurJeu.ServeurJeu;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author root
 */
@XmlRootElement(name = "Partie")
public class Partie extends UnicastRemoteObject implements IPartie,Serializable,Runnable{
    private ArrayList<IClient> listeClient;
    private HashMap<IClient,ScoreClient> listeScoreClient;
    
    private int nombreJoueurMax;
    private String nomPartie;
    private Thread thread;
    private IClient admin = null;
    private IServeurJeu serveurJeu;
    private boolean nextPlayer;
    
    public Partie() throws RemoteException {
          
    }
    
    public Partie(String nomPartie, int nombreJoueurMax) throws MalformedURLException, RemoteException{
        super();
        this.nomPartie = nomPartie;
        this.nombreJoueurMax = nombreJoueurMax;
        this.listeClient = new ArrayList<IClient>();
        this.listeScoreClient = new HashMap<IClient, ScoreClient>();
	this.nextPlayer = false;
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
    public boolean addScore(IClient c,Regles.TYPESCORE type, Main m) throws RemoteException{
	System.out.println(m);
	if(listeScoreClient.get(c).getScore(type)==-1){
		int inf = listeScoreClient.get(c).getScore(Regles.TYPESCORE.INFERIEUR);
		int sup = listeScoreClient.get(c).getScore(Regles.TYPESCORE.SUPERIEUR);
		System.out.println(m.getScore(type,sup,inf));
		listeScoreClient.get(c).addScore(type, m.getScore(type,sup,inf));
		return true;
	}
	return false;
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
    public void nextPlayer() throws RemoteException{
	    nextPlayer = true;
    }
    
    @Override
    public synchronized void run() {
        System.out.println("lancer");
       for(int i=0; i<Regles.TYPESCORE.values().length; i++){
	       System.out.println("tour : " + i);
           for(int j=0; j<listeClient.size(); j++){
				try {
					verifierJoueurDeco();
				} catch (RemoteException ex) {
					Logger.getLogger(Partie.class.getName()).log(Level.SEVERE, null, ex);
				}
                try {
                    //serveurJeu.jouer(this, listeClient.get(j));
			System.out.println("client : " + j);
		    nextPlayer = false;
                    listeClient.get(j).jouer();
		    while(!nextPlayer){
			    verifierJoueurDeco();
		    }
		    
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
    private void verifierJoueurDeco() throws RemoteException{
	    for(int i=0; i<this.listeClient.size(); i++){
		    try{
			    this.listeClient.get(i).estConnecter();
		    }catch(Exception e){
			    System.out.println("Partie : joueur deco");
			    this.listeScoreClient.remove(this.listeClient.get(i));
			    this.listeClient.remove(i);
			    i--;
		    }
	    }
	     if(	listeClient.isEmpty()){
				    finPartie();
	}
    }
    private void finPartie() throws RemoteException{   
        for(IClient c : listeClient){
            c.setEtatJoueur(Client.ETAT_JOUEUR.JOUE_PAS);
            c.setEtatClient(Client.ETAT_CLIENT.RECHERCHE_PARTIE);
        }
	this.listeClient.clear();
        this.listeScoreClient.clear();
        this.serveurJeu.dellPartie((IPartie)this);
    }

    @Override
    public void addClient(IClient c) throws RemoteException {
     //   try {
//            System.out.println("Client " + c.getClient() + " a rejoint la partie " + this.nomPartie);
           if(!listeClient.contains(c) && listeClient.size() < nombreJoueurMax){
               listeClient.add(c);
               listeScoreClient.put(c,new ScoreClient());
               if(listeClient.size() == 1){
                   admin = c;
               }
       //        System.out.println("Client " + c.getClient() + " a rejoint la partie " + this.nomPartie);
               if(listeClient.size() == nombreJoueurMax){
		   for(IClient cc : listeClient){
			cc.setMessage(cc, "Partie remplie");
		   }
               }
           }
       /* } catch (RemoteException ex) {
            Logger.getLogger(Partie.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

    @Override
    public void dellClient(IClient c) throws RemoteException {
	verifierJoueurDeco();
        listeClient.remove(c);
        if(c.equals(admin) && !listeClient.isEmpty()){
            admin = listeClient.get(0).getClient();
            c.setEtatJoueur(Client.ETAT_JOUEUR.JOUE_PAS);
            c.setEtatClient(Client.ETAT_CLIENT.RECHERCHE_PARTIE);
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
        return this.toString().equals(((Partie)obj).toString());
    }

    public void afficherScore() throws RemoteException {
        for(Entry<IClient, ScoreClient> entry : listeScoreClient.entrySet()) {
	    IClient cle = entry.getKey();
	    ScoreClient valeur = entry.getValue();
	    // traitements
	    System.out.println(cle.getName() + " : \n");
	    System.out.println(valeur);
		
	}
    }

    @Override
    public Partie partie() throws RemoteException {
        return this;
    }

    public IClient getAdmin() {
        return admin;
    }

    public void setAdmin(Client admin) {
        this.admin = admin;
    }

    public ArrayList<IClient> getListeClient() {
        return listeClient;
    }

    public void setListeClient(ArrayList<IClient> listeClient) {
        this.listeClient = listeClient;
    }

    public HashMap<IClient, ScoreClient> getListeScoreClient() {
        return listeScoreClient;
    }

    public void setListeScoreClient(HashMap<IClient, ScoreClient> listeScoreClient) {
        this.listeScoreClient = listeScoreClient;
    }
    

    public String getNomPartie() {
        return nomPartie;
    }

    public void setNomPartie(String nomPartie) {
        this.nomPartie = nomPartie;
    }

   
    public int getNombreJoueurMax() {
        return nombreJoueurMax;
    }

    public void setNombreJoueurMax(int nombreJoueurMax) {
        this.nombreJoueurMax = nombreJoueurMax;
    }

    public void setServeurJeu(ServeurJeu serveurJeu) {
        this.serveurJeu = serveurJeu;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    @Override
    public int getNbUserMax() throws RemoteException {
        return this.getNombreJoueurMax();
    }

    @Override
    public int getNbUser() throws RemoteException {
        return this.listeClient.size();
    }

	@Override
	public String getName() throws RemoteException {
		return nomPartie;
	}

	@Override
	public String getAffichageScore() throws RemoteException {
		String str = "";
		for(Entry<IClient, ScoreClient> entry : listeScoreClient.entrySet()) {
		    IClient cle = entry.getKey();
		    ScoreClient valeur = entry.getValue();
		    // traitements
		    str += cle.getName() + " : \n";
		    str += valeur;

		}
		return str;
	}

	@Override
	public String getAffichageScoreClient(IClient c) throws RemoteException {
		return this.listeScoreClient.get(c).toString();
	}

	public boolean isNextPlayer() {
		return nextPlayer;
	}

	public void setNextPlayer(boolean nextPlayer) {
		this.nextPlayer = nextPlayer;
	}

	public IServeurJeu getServeurJeu() {
		return serveurJeu;
	}

	public void setServeurJeu(IServeurJeu serveurJeu) {
		this.serveurJeu = serveurJeu;
	}
    
}
