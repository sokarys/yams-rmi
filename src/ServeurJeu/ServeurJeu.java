/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServeurJeu;

import Client.Client;
import Client.Client.ETAT_CLIENT;
import Client.IClient;
import ServeurJeu.modele.IPartie;
import ServeurJeu.modele.Main;
import ServeurJeu.modele.Partie;
import ServeurJeu.modele.Regles.TYPESCORE;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author root
 */
@XmlRootElement(name = "ServeurJeu")
public class ServeurJeu extends UnicastRemoteObject implements IServeurJeu,Runnable{
    private  final Thread thread;
    private  ArrayList<IClient> listeClient;
    private  ArrayList<IPartie> listPartie;

    public ServeurJeu() throws RemoteException{
        thread = new Thread(this);
    }
    
    public ServeurJeu(int port) throws RemoteException, AlreadyBoundException{
        super(port);
        initServeurJeu();
        thread = new Thread(this);
        thread.start();
    }
    
            
    private void initServeurJeu() throws RemoteException, AlreadyBoundException{
        listeClient = new ArrayList<IClient>();
        listPartie = new ArrayList<IPartie>();
        Registry registry = LocateRegistry.createRegistry(2000);
        registry.bind("IServeurJeu", this);
    }

    
    @Override
    public void seConnecter(IClient c) throws RemoteException {
        //USE SERVER DATA
        listeClient.add(c);
        c.setEtatClient(Client.ETAT_CLIENT.RECHERCHE_PARTIE);
        c.setListePartie(listPartie);
    }

    @Override
    public void seDeconnecter(IClient c) throws RemoteException {
        //if(!c.getClient().enPartie()){
            listeClient.remove(c);
        //}
    }

    @Override
    public void creerClient(String login, String password) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void lancerPartie(IClient c, IPartie p) throws RemoteException {
        p.lancerPartie(c);
    }

    @Override
    public void deconnecterPartie(IClient c, IPartie p) throws RemoteException {
        p.dellClient(c);
    }

    @Override
    public IPartie creerPartie(IClient c, String nomPartie, int nbJoueur) throws RemoteException {
        IPartie p = null;
        try {
            p = new Partie(nomPartie,nbJoueur);
	    p.addClient(c);
            this.listPartie.add(p);
            for(IClient cl : listeClient){
                cl.setListePartie(listPartie);
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(ServeurJeu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return p;
    }

    @Override
    public void rejoindrePartie(int numPartie, IClient c) throws RemoteException {
        this.listPartie.get(numPartie).addClient(c);
        c.setPartie(this.listPartie.get(numPartie));
        c.setEtatClient(ETAT_CLIENT.EN_PARTIE);
    }

    @Override
    public void envoyerMessage(IClient c, String message) throws RemoteException {
        for(IClient cl : listeClient){
            cl.setMessage(c, message);
        }
    }

    @Override
    public void envoyerMessage(IClient c, IPartie p, String message) throws RemoteException {
        for(IClient cl : listeClient){
            cl.setMessage(c, message, p);
        }
    }

    @Override
    public void envoyerMainPartie(TYPESCORE type, Main m, IPartie p, IClient c) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void run() {
     System.out.println("Serveur Lancer");
        while(true){
            System.out.println("Clients : " +  listeClient.size());
            System.out.println("Parties : " + listPartie.size());
            try {
                thread.sleep(5000);
		for(int i=0; i<listeClient.size(); i++){
			try{
				listeClient.get(i).estConnecter();
			}catch(Exception e){
				System.out.println("Serveur : joueur deco");
				for(IPartie p : listPartie){
					try {
						p.dellClient(listeClient.get(i));
					} catch (RemoteException ex) {
						Logger.getLogger(ServeurJeu.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
				listeClient.remove(i);
				i--;
			}
		}
            } catch (InterruptedException ex) {
                Logger.getLogger(ServeurJeu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void dellPartie(IPartie p) throws RemoteException {
	for(int i=0; i<listPartie.size(); i++){
		if(p.getName().equals(listPartie.get(i).getName())){
			listPartie.remove(i);
		}
	}
    }

    @Override
    public ArrayList<String> getListepartie() throws RemoteException {
        ArrayList<String> l = new ArrayList<String>();
        int i = 0;
        for(IPartie p : listPartie){
            l.add(i +  " : " + p.partie().getNomPartie() + " - " + p.getNbUser() + "/" + p.getNbUserMax());
        }
        return l;
    }

    @Override
    public Main jouer(IPartie p, IClient c) throws RemoteException {
        c.setEtatJoueur(Client.ETAT_JOUEUR.JOUE);
        return null;
    }

    
    
}
