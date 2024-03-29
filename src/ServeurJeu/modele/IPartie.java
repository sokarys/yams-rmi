/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServeurJeu.modele;

import Client.IClient;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author root
 */
public interface IPartie extends Remote{
    public void addClient(IClient c) throws RemoteException;
    public void dellClient(IClient c) throws RemoteException;
    public void lancerPartie(IClient c) throws RemoteException;
    public String getAffichageScore() throws RemoteException;
    public String getAffichageScoreClient(IClient c) throws RemoteException;
    public Partie partie()throws RemoteException;
    public int getNbUserMax() throws RemoteException;
    public int getNbUser() throws RemoteException;
    public void nextPlayer() throws RemoteException;
    public String getName() throws RemoteException;
    public boolean addScore(IClient c,Regles.TYPESCORE type, Main m) throws RemoteException;
    public ArrayList<IClient> getListClient() throws RemoteException;
}
