/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import ServeurJeu.modele.ScoreClient;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author root
 */
@XmlRootElement(name="Historique")
public class Historique implements Serializable{
	private HashMap<Date,ScoreClient> histoScore;
	
	public Historique(){
		histoScore = new HashMap<Date, ScoreClient>();
	}

	public HashMap<Date, ScoreClient> getHistoScore() {
		return histoScore;
	}

	public void setHistoScore(HashMap<Date, ScoreClient> histoScore) {
		this.histoScore = histoScore;
	}
	public void addHistorique(ScoreClient sc){
		this.histoScore.put(new Date(), sc);
	}

	@Override
	public String toString() {
		String str = "";
		for(Entry<Date, ScoreClient> entry : histoScore.entrySet()) {
			Date cle = entry.getKey();
			ScoreClient valeur = entry.getValue();
			
			str += "\n" + cle + "\n" + valeur;
		}
		return str;
	}
	
}
