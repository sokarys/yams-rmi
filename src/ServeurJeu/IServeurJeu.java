/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServeurJeu;

import Client.Client;
import Client.IClient;
import ServeurJeu.modele.IPartie;
import ServeurJeu.modele.Main;
import ServeurJeu.modele.Partie;
import ServeurJeu.modele.Regles;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author root
 */
public interface IServeurJeu extends Remote{
    //Client-Serveur
    public void seConnecter(IClient c) throws RemoteException;
    public void seDeconnecter(IClient c) throws RemoteException;    
    public void creerClient(String login, String password) throws RemoteException;
    
    
    //Client-Partie
    public void lancerPartie(IClient c, IPartie p) throws RemoteException;
    public void deconnecterPartie(IClient c, IPartie p) throws RemoteException;
    public IPartie creerPartie(IClient c, String nomPartie, int nbJoueur) throws RemoteException;
    public void rejoindrePartie(int numPartie, IClient c) throws RemoteException;
    public void dellPartie(IPartie p) throws RemoteException;
    public Main jouer(IPartie p, IClient c) throws RemoteException;
    public Main genererMain() throws RemoteException;
    public ArrayList<String> getListepartie() throws RemoteException;
    public void sauvegarderClient(IClient c) throws RemoteException;
    
    
    //Client-Chat
    public void envoyerMessage(IClient c,String message) throws RemoteException;
    
}
