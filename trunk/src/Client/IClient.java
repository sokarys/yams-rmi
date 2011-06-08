/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import ServeurJeu.modele.IPartie;
import ServeurJeu.modele.Partie;
import ServeurJeu.modele.ScoreClient;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author root
 */
public interface IClient extends Remote{
    //mise a jour liste partie via le serveur
    public void setPartie(IPartie p) throws RemoteException;
    public IPartie getPartie() throws RemoteException;
    //Message Reçus via le serveur
    public void setMessage(String message ) throws RemoteException;
    public void envoieMessage(String message) throws RemoteException;
    
    //savoir si le client recherche une partie, est dans un salon, ou en partie
    public void setEtatClient(Client.ETAT_CLIENT etat) throws RemoteException;
    //savoir si le client doit jouer ou pas
    public void setEtatJoueur(Client.ETAT_JOUEUR etat) throws RemoteException;

    //general
    public Client getClient() throws RemoteException;
    public boolean enPartie() throws RemoteException;
    
    public int jouer() throws RemoteException;
    public String getName() throws RemoteException;
    public String getPassword() throws RemoteException;
    public Historique getHistorique() throws RemoteException;
    public boolean estConnecter() throws RemoteException;
    public void addScoreToHistorique(ScoreClient score) throws RemoteException;
    public void setHistorique(Historique h) throws RemoteException;
}
