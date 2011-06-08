/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import ServeurJeu.IServeurJeu;
import ServeurJeu.ServeurJeu;
import ServeurJeu.modele.IPartie;
import ServeurJeu.modele.Main;
import ServeurJeu.modele.Partie;
import ServeurJeu.modele.Regles;
import ServeurJeu.modele.Regles.TYPESCORE;
import ServeurJeu.modele.ScoreClient;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
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
    private ETAT_CLIENT etatClient;
    //@XmlElement()
    private ETAT_JOUEUR etatJoueur;
    //@XmlElement()
    private String login;
    //@XmlElement()
    private String password;
    private IPartie partie;
    private IServeurJeu serveurJeu;
    private Tchat tchat;
    private final Thread tread = new Thread(this);
    private Historique historique;
    //@XmlElement()
    private boolean quitter;
    private String ipServeur;
    
    public Client() throws RemoteException{       
         super();
        this.login = "";
        this.password = "";
	this.ipServeur = "127.0.0.1";
        etatClient = ETAT_CLIENT.DECONNECTE;
        etatJoueur = ETAT_JOUEUR.JOUE_PAS;
        //listePartie = new ArrayList<IPartie>();
        partie = null;
        tchat = new Tchat("Tchat " + login, this);
        tchat.setVisible(true);
        try {
            serveurJeu = (IServeurJeu) Naming.lookup("rmi://127.0.0.1:2000/IServeurJeu");
	    historique = new Historique();
        } catch (NotBoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Client(String login, String password,String ipServeur) throws RemoteException{
        super();
	this.ipServeur = ipServeur;
        this.login = login;
        this.password = password;
        etatClient = ETAT_CLIENT.DECONNECTE;
        etatJoueur = ETAT_JOUEUR.JOUE_PAS;
        //listePartie = new ArrayList<IPartie>();
        partie = null;
        tchat = new Tchat("Tchat " + login, this);
        tchat.setVisible(true);
        try {
            serveurJeu = (IServeurJeu) Naming.lookup("rmi://"+ipServeur+":2000/IServeurJeu");
	    historique = new Historique();
        } catch (NotBoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
	tread.start();
    }
   
    @Override
    public void setMessage(String message) throws RemoteException {
        tchat.addMesage(message);
        //System.out.println(message);
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
    
    @Override
    public void envoieMessage(String message) throws RemoteException{
        try {
                serveurJeu.envoyerMessage(this,message);
            } catch (RemoteException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
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
    
    @Override
    public String getPassword() throws RemoteException{
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
    
    @Override
    public boolean enPartie() throws RemoteException{
        return getEtatClient() == Client.ETAT_CLIENT.EN_PARTIE || getEtatClient() == Client.ETAT_CLIENT.EN_JEU;
    }
    
    public void seConnecter() throws RemoteException{
        serveurJeu.seConnecter(this);
    }
    
    public void seDeconnecter() throws RemoteException{
        serveurJeu.seDeconnecter(this);
    }
    
    public void quitterPartie() throws RemoteException{
        partie.dellClient(this);
	this.setEtatClient(Client.ETAT_CLIENT.RECHERCHE_PARTIE);
	this.setEtatJoueur(Client.ETAT_JOUEUR.JOUE_PAS);
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
                this.serveurJeu.rejoindrePartie(numPartie, this);
            }
    }

    public ArrayList<String> getListePartie() throws RemoteException {
        return serveurJeu.getListepartie();
    }
    
    @Override
    public int jouer() throws RemoteException{
        this.setEtatClient(Client.ETAT_CLIENT.EN_JEU);
        this.setEtatJoueur(Client.ETAT_JOUEUR.JOUE);
        return 0;
    }

    private void jeJoue(Scanner sc) throws RemoteException{
	    boolean encours = true;
	    //génération d'une main grace au serveur
	    Main m = serveurJeu.genererMain();
	    String choix = "";
	    int de = -1;
	    while(encours){
		    System.out.println(m);
		    System.out.println("1 - Ajouter un de a relancer\n2 - Enlever un de à relancer\n3 - Relancer les des\n4 - Rajouter le score\n5 - Voir mes scores\n6 - Voir de a relancer");
		    choix = sc.nextLine();
		    if(choix.equals("1")){
			    System.out.println("Choisissez un numero de De : ");
			    de = sc.nextInt();
			    m.choixDeARelancer(de);
		    }else if(choix.equals("2")){
			    System.out.println("Choisissez un numero de De : ");
			    de = sc.nextInt();
			    m.dellDeARelancer(de);
		    }else if(choix.equals("3")){
			    m.relancerLesDes();
		    }else if(choix.equals("4")){
			    System.out.println(partie.getAffichageScoreClient(this));
			    System.out.println("Choissisez la ligne : ");
			    de = sc.nextInt();
			    if(partie.addScore(this, TYPESCORE.values()[de], m)){
				    encours = false;
				    partie.nextPlayer();
				    this.setEtatJoueur(ETAT_JOUEUR.JOUE_PAS);
			    }else{
				    System.out.println("Un score existe deja, choisissez autre chose.");
			    }
			    
		    }else if(choix.equals("5")){
			    System.out.println(partie.getAffichageScoreClient(this));
		    }else if(choix.equals("6")){
			    System.out.println(m.getListeDesARelancer());
		    }
	    }
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
            try {
			if(!quitter){
				selectionMenu();
				tread.sleep(500);
			}
		}catch (InterruptedException ex) {
				Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
		} catch (RemoteException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       System.exit(0);
    }
   
     
    public void selectionMenu() throws RemoteException {
       Integer selection;
       System.out.println("\n");
       Scanner sc = new Scanner(System.in);
       switch(this.etatClient){
            case DECONNECTE :                
                System.out.println("┌----------------------------------------------------------------------------------------------------┐");
                System.out.println("│ 1:Se connecter | 2:Créer un compte | 3:Quitter                                                     │");
                System.out.println("└----------------------------------------------------------------------------------------------------┘");
                System.out.print("Votre sélection : ");
                selection = 0;
                while(selection == 0){
                    selection = Integer.valueOf(sc.nextLine());
                    switch(selection){
                        case 1:
                            System.out.println("Votre Login : ");
                            login = sc.nextLine().replace("\n", "");
                            System.out.println("Mot de passe : ");
                            password = sc.nextLine().replace("\n", "");
                            this.seConnecter();
                            break;
                        case 2:
			    System.out.println("Votre Login : ");
                            login = sc.nextLine().replace("\n", "");
                            System.out.println("Mot de passe : ");
                            password = sc.nextLine().replace("\n", "");
                            serveurJeu.creerClient(login, password);
			    this.seConnecter();
                            break;
                        case 3:
                            quitter = true;
                            break;
                        default:
                            System.out.print("Saisie incorrecte, recommencez svp : ");
                            selection = 0;
                            break;
                    }
                }
                break;
            case RECHERCHE_PARTIE :
                System.out.println("┌----------------------------------------------------------------------------------------------------┐");
                System.out.println("│ 1:Créer une partie | 2:Rejoindre une partie | 3:Voir les parties | 4:Mes scores | 5:Se déconnecter │");
                System.out.println("└----------------------------------------------------------------------------------------------------┘");
                System.out.print("Votre sélection : ");
                selection = 0;
                while(selection == 0){
                    selection = Integer.valueOf(sc.nextLine());
                    switch(selection){
                        case 1:
                            System.out.print("Entrez un nom pour la partie : ");
                            String nom = sc.nextLine().replace("\n", "");
                            System.out.print("Nombre de joueurs maximum : ");
                            int nb = Integer.valueOf(sc.nextLine().replace("\n", ""));
                            this.creerPartie(nom, nb);
                            break;
                        case 2:
                            System.out.println("Nom de la partie : ");
                            nb = Integer.valueOf(sc.nextLine().replace("\n", ""));
                            this.rejoindrepartie(nb);
                            break;
                        case 3:
                            System.out.println(this.getListePartie());
                            break;
                        case 4:
                            System.out.println(this.getHistorique());
                            break;
                        case 5:
                            this.seDeconnecter();
                            break;
                        default:
                            System.out.print("Saisie incorrecte, recommencez svp : ");
                            selection = 0;
                            break;
                    }
                }
                break;
            case EN_PARTIE :
                System.out.println("┌----------------------------------------------------------------------------------------------------┐");
                System.out.println("│ 1:Lancer la partie | 2:Quitter la partie |                                                         │");
                System.out.println("└----------------------------------------------------------------------------------------------------┘");
                System.out.print("Votre sélection : ");
                selection = 0;
                while(selection == 0){
                    try{
                    selection = Integer.valueOf(sc.nextLine());
                    switch(selection){
                        case 1:
                            this.lancerPartie();
                            break;
                        case 2:
                            this.quitterPartie();
                            break;
                        default:
                            System.out.print("Saisie incorrecte, recommencez svp : ");
                            selection = 0;
                            break;
                    }
                    }catch(Exception e){
                        System.out.println("Error EN PARITE");
                    }
                }
                break;
            case EN_JEU :
                switch(this.etatJoueur){
                    case JOUE :
                        System.out.println("┌----------------------------------------------------------------------------------------------------┐");
                        System.out.println("│ 1:Jouer | 2:Voir les scores                                                                        │");
                        System.out.println("└----------------------------------------------------------------------------------------------------┘");
                        System.out.print("Votre sélection : ");
                        selection = 0;
                        while(selection == 0){
                            try{
                            selection = Integer.valueOf(sc.nextLine());
                            switch(selection){
                                case 1:
					if(this.getEtatJoueur() == ETAT_JOUEUR.JOUE){
						this.jeJoue(sc);
					}
					break;
                                case 2:
					System.out.println(partie.getAffichageScore());
                                    break;
                                default:
                                    System.out.print("Saisie incorrecte, recommencez svp : ");
                                    selection = 0;
                                    break;
                            }
                            }catch(Exception e){
                                System.out.println("Error JOU");
                            }
                        }
                        break;
                    case JOUE_PAS :
                        System.out.println("┌----------------------------------------------------------------------------------------------------┐");
                        System.out.println("│ 1:Voir les scores                                                                                  │");
                        System.out.println("└----------------------------------------------------------------------------------------------------┘");
                        System.out.print("Votre sélection : ");
                        selection = 0;
                        while(selection == 0){
                            selection = Integer.valueOf(sc.nextLine());
                            switch(selection){
                                case 1:
                                    System.out.println(partie.getAffichageScore());
                                    break;
                                default:
                                    System.out.print("Saisie incorrecte, recommencez svp : ");
                                    selection = 0;
                                    break;
                            }
                        }
                        break;
                }
                break;
        }
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
    
	@Override
	public boolean estConnecter() throws RemoteException {
		return true;
	}
    
    	@Override
	public String getName() throws RemoteException {
		return this.login;
	}
	
	@Override
	public void addScoreToHistorique(ScoreClient score) throws RemoteException{
		this.historique.addHistorique(score);
		serveurJeu.sauvegarderClient(this);
	}
	
	@Override
	public void setHistorique(Historique h) throws RemoteException{
		this.historique = h;
	}
	@Override
	public Historique getHistorique() throws RemoteException {
		return this.historique;
	}
	
	@Override
	public IPartie getPartie() throws RemoteException{
		return this.partie;
	}
}
