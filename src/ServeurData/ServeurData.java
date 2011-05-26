/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServeurData;

import Client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.io.StringReader;
import java.net.URI;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;


/**
 *
 * @author root
 */
public class ServeurData  extends UnicastRemoteObject{
    
        private ClientConfig config;       
        private WebResource service;

    public ServeurData() throws RemoteException {
        config = new DefaultClientConfig();
        com.sun.jersey.api.client.Client client = com.sun.jersey.api.client.Client.create(config);
        service = client.resource(getBaseURI());
    }    
    
    //ajouter user
    public void setUser(Client c) throws RemoteException{
        
    }
    //recupérer si client si login et mdp bon sinon return null
    public Client getUser(String login,String password) throws RemoteException, JAXBException{
       String repUser = service.path("User").path(login).path(password).accept(MediaType.APPLICATION_XML).get(String.class); 
        JAXBContext mar = JAXBContext.newInstance(Client.class);
        javax.xml.bind.Unmarshaller un = mar.createUnmarshaller();
        StringBuilder xmlstr = new StringBuilder(repUser);
        JAXBElement<Client> r = (JAXBElement<Client>) un.unmarshal(new StreamSource(new StringReader(xmlstr.toString())),Client.class);
        
        return r.getValue();
    }
        
    public ArrayList<Client> getListUser() throws RemoteException{
        return new ArrayList<Client>();
    }
            
     public String Test() throws RemoteException{
        //LIre liste fichier renvoyer le client correspondant au nom est password sinon null
        return "helloTest";
    }
     

    public static URI getBaseURI(){
        return UriBuilder.fromUri("http://localhost:8080/testWS").build();
    }
    
}
