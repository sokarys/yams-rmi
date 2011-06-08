/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServeurJeu;

import Client.Client;
import Client.Client.ETAT_CLIENT;
import Client.ClientSave;
import Client.Historique;
import Client.IClient;
import ServeurData.ServeurData;
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
import javax.xml.bind.JAXBException;
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
    private transient ServeurData serverData;

    public ServeurJeu() throws RemoteException{
        thread = new Thread(this);
	serverData = new ServeurData();
    }
    
    public ServeurJeu(int port) throws RemoteException, AlreadyBoundException{
        super(port);
        initServeurJeu();
	serverData = new ServeurData();
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
    public synchronized void seConnecter(IClient c) throws RemoteException {
        //USE SERVER DATA
	ClientSave cs = null;
	//System.out.println(serverData.getListUser_XML(ArrayList.class));
	cs = serverData.getUser(ClientSave.class,c.getName(), c.getPassword());
	System.out.println(cs);
	if(!cs.getLogin().equals("")){
		listeClient.add(c);
		c.setEtatClient(Client.ETAT_CLIENT.RECHERCHE_PARTIE);
		c.setHistorique(cs.getHistorique());
		c.setMessage("<span style='color:blue;'>Systeme : Vous etes connecte</span>");
		for(IClient cl : listeClient){
			if(!cl.equals(c)){
				cl.setMessage("<span style='color:blue;'>Systeme : " + c.getName() + " s'est connecte.</span>");
			}
		}
		
	}else{
		c.setMessage("<span style='color:blue;'>Systeme : Login introuvable</span>");
	}
    }

    @Override
    public synchronized void seDeconnecter(IClient c) throws RemoteException {
        //if(!c.getClient().enPartie()){
            listeClient.remove(c);
	    ClientSave cs = new ClientSave(c.getName(),c.getPassword(),c.getHistorique());
	    serverData.setUser(cs);
	    c.setEtatClient(Client.ETAT_CLIENT.DECONNECTE);
	    c.setEtatJoueur(Client.ETAT_JOUEUR.JOUE_PAS);
        //}
    }

    @Override
    public void creerClient(String login, String password) throws RemoteException {
            ClientSave cs = new ClientSave(login,password,new Historique());
	    serverData.setUser(cs);
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
                cl.setMessage("<span style='color:blue;'>Systeme : une nouvelle partie a ete cree : " + nomPartie + " 1/" + nbJoueur + "</span>");
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
    public void envoyerMessage(IClient c,String message) throws RemoteException {
	if(message.startsWith("/p") && c.enPartie()){
		for(IClient cl : c.getPartie().getListClient()){
		    cl.setMessage("<span style='color:#339900;'>[Partie] " + message + "</span>");
		}
	}else if(!message.contains("Systeme : ")){
		for(IClient cl : listeClient){
		    cl.setMessage("<span style='color:#FF9933;'>" + message + "</span>");
		}
	}else{
		for(IClient cl : listeClient){
		    cl.setMessage(message);
		}
	}
        
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

    //DEPRECIE
    @Override
    public Main jouer(IPartie p, IClient c) throws RemoteException {
        c.setEtatJoueur(Client.ETAT_JOUEUR.JOUE);
        return null;
    }

	@Override
	public Main genererMain() throws RemoteException {
		return new Main();
	}
	
	@Override
	public void sauvegarderClient(IClient c) throws RemoteException{
		ClientSave cs = new ClientSave(c.getName(),c.getPassword(),c.getHistorique());
		serverData.setUser(cs);
	}

    
    
}
