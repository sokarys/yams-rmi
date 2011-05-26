/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServeurJeu.modele;

import Client.IClient;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author root
 */
public interface IPartie extends Remote{
    public void addClient(IClient c) throws RemoteException;
    public void dellClient(IClient c) throws RemoteException;
    public void lancerPartie(IClient c) throws RemoteException;
    public void afficherScore() throws RemoteException;
    public Partie partie()throws RemoteException;
    
}
