/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author root
 */
@XmlRootElement(name="ClientSave")
public class ClientSave implements Serializable{
	
	private String login;
	private String password;
	private Historique historique;

	public ClientSave(String login, String password, Historique historique) {
		this.login = login;
		this.password = password;
		this.historique = historique;
	}

	public ClientSave(){
		
	}
	
	
	public Historique getHistorique() {
		return historique;
	}

	public void setHistorique(Historique historique) {
		this.historique = historique;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean equals(Object obj) {
		return this.login.equals(((ClientSave) obj).login) &&this.password.equals(((ClientSave) obj).password) ;
	}

	@Override
	public String toString() {
		return login + " " + password + "  " + getHistorique();
	}
}



