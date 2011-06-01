/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import ServeurJeu.modele.IPartie;
import ServeurJeu.modele.Partie;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author root
 */
public interface IClient extends Remote{
    //mise a jour liste partie via le serveur
    public void setListePartie(ArrayList<IPartie> listeP) throws RemoteException;
    public void setPartie(IPartie p) throws RemoteException;
    //Message Reçus via le serveur
    public void setMessage(IClient c, String message ) throws RemoteException;
    public void setMessage(IClient c, String message,IPartie p) throws RemoteException;
    
    //savoir si le client recherche une partie, est dans un salon, ou en partie
    public void setEtatClient(Client.ETAT_CLIENT etat) throws RemoteException;
    //savoir si le client doit jouer ou pas
    public void setEtatJoueur(Client.ETAT_JOUEUR etat) throws RemoteException;

    //general
    public Client getClient() throws RemoteException;
    
    public int jouer() throws RemoteException;
    public boolean estConnecter() throws RemoteException;
}
