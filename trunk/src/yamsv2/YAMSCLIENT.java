/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yamsv2;

import Client.Client;
import java.rmi.RemoteException;


/**
 *
 * @author root
 */
public class YAMSCLIENT {
    public static void main(String[] args) throws RemoteException {
        Client c = new Client("","");
        Thread t = new Thread(c);
        t.start();
    }
}
