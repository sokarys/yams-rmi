/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServeurData;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;


/**
 *
 * @author root
 */


//public class ServeurData  extends UnicastRemoteObject{
//    
//        private ClientConfig config;       
//        private WebResource service;
//
//    public ServeurData() throws RemoteException {
//        config = new DefaultClientConfig();
//        com.sun.jersey.api.client.Client client = com.sun.jersey.api.client.Client.create(config);
//        service = client.resource(getBaseURI());
//    }    
//    
//    //ajouter user
//    public void setUser(ClientSave c) throws RemoteException{
//	service.path("setUser").accept(MediaType.APPLICATION_XML).post(new JAXBElement<ClientSave>(new QName("ClientSave"),ClientSave.class,c));
//    }
//    //recupérer si client si login et mdp bon sinon return null
//    public ClientSave getUser(String login,String password) throws RemoteException, JAXBException{
//	    
//	GenericType<JAXBElement<ClientSave>> ClientSaveType = new GenericType<JAXBElement<ClientSave>>() {};
//	ClientSave cs = service.path("User").path(login).path(password).accept(MediaType.APPLICATION_XML).get(ClientSaveType).getValue();
//	
//	return cs;
//        /*String repUser = service.path("User").path(login).path(password).accept(MediaType.APPLICATION_XML).get(String.class);
//        JAXBContext mar = JAXBContext.newInstance(ClientSave.class);
//        javax.xml.bind.Unmarshaller un = mar.createUnmarshaller();
//        StringBuilder xmlstr = new StringBuilder(repUser);
//        JAXBElement<ClientSave> r = (JAXBElement<ClientSave>) un.unmarshal(new StreamSource(new StringReader(xmlstr.toString())),ClientSave.class);
//        */
//        //return r.getValue();
//    }
//        
//    public ArrayList<ClientSave> getListUser() throws RemoteException, JAXBException{
//        String repList = service.path("List").accept(MediaType.APPLICATION_XML).get(String.class);
//        
//        JAXBContext mar = JAXBContext.newInstance(ArrayList.class);
//        javax.xml.bind.Unmarshaller un = mar.createUnmarshaller();
//        StringBuilder xmlstr = new StringBuilder(repList);
//        JAXBElement<ArrayList> r = (JAXBElement<ArrayList>) un.unmarshal(new StreamSource(new StringReader(xmlstr.toString())),ArrayList.class);
//        
//        return r.getValue();
//    }
// 
//    public static URI getBaseURI(){
//        return UriBuilder.fromUri("http://localhost:8080/testWS").build();
//    }
//    
//    
//    
//}

	public class ServeurData {

		private WebResource webResource;
		private Client client;
		private static final String BASE_URI = "http://localhost:8080/testWS";

		public ServeurData() {
			com.sun.jersey.api.client.config.ClientConfig config = new com.sun.jersey.api.client.config.DefaultClientConfig();
			client = Client.create(config);
			webResource = client.resource(BASE_URI);
		}

		public String Test() throws UniformInterfaceException {
			WebResource resource = webResource;
			resource = resource.path("Test");
			return resource.accept(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
		}

		public <T> T getUser(Class<T> responseType, String login, String password) throws UniformInterfaceException {
			WebResource resource = webResource;
			resource = resource.path(java.text.MessageFormat.format("User/{0}/{1}", new Object[]{login, password}));
			return resource.accept(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
		}

		public void setUser(Object requestEntity) throws UniformInterfaceException {
			webResource.path("setUser").type(javax.ws.rs.core.MediaType.APPLICATION_XML).post(requestEntity);
		}

		public <T> T getListUser_XML(Class<T> responseType) throws UniformInterfaceException {
			WebResource resource = webResource;
			resource = resource.path("List");
			return resource.accept(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
		}

		public <T> T getListUser_JSON(Class<T> responseType) throws UniformInterfaceException {
			WebResource resource = webResource;
			resource = resource.path("List");
			return resource.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
		}

		public void close() {
			client.destroy();
		}
}
