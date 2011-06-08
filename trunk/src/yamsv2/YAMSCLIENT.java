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
        Client c = new Client("","","127.0.0.1");
        //Thread t = new Thread(c);
        //t.start();
    }
}
