/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServeurJeu.modele;

import ServeurJeu.modele.Regles.TYPESCORE;
import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author root
 */
public class ScoreClient implements Serializable{
    private HashMap<Regles.TYPESCORE,Integer> score;
    
    
    public ScoreClient(){
        score = new HashMap<Regles.TYPESCORE, Integer>();
        for(int i=0; i<Regles.TYPESCORE.values().length; i++){
            score.put(Regles.TYPESCORE.values()[i], -1);
        }
    }
    
    public boolean addScore(Regles.TYPESCORE type, Integer value){
        if(score.get(type) == -1){
            score.put(type,value);
            return true;
        }
        
        return false;
    }
    
    public int getScoreTotal(){
	int total = 0;
	for(int i=0; i<Regles.TYPESCORE.values().length; i++){
		if(score.get(Regles.TYPESCORE.values()[i]) != -1){
			total +=score.get(Regles.TYPESCORE.values()[i]);
		}
	}
	return total;
    }
    
    public int getScore(Regles.TYPESCORE type){
	    if(this.score.get(type) != -1){
		    return this.score.get(type);
	    }
	    return 0;
    }
    
     public int getScoreRealValue(Regles.TYPESCORE type){
	return this.score.get(type);
    }
    

	@Override
	public String toString() {
		String str = "";
		for(int i=0; i<Regles.TYPESCORE.values().length; i++){
			if(score.get(Regles.TYPESCORE.values()[i]) != -1){
				str += i + " - " + Regles.TYPESCORE.values()[i] + " : " + score.get(Regles.TYPESCORE.values()[i]) + "\n";
			}else{
				str += i + " - " + Regles.TYPESCORE.values()[i] + " :\n";
			}
		}	
		return str +"\nScore Total : "+ this.getScoreTotal();
	}

	public void setScore(HashMap<TYPESCORE, Integer> score) {
		this.score = score;
	}

	public HashMap<TYPESCORE, Integer> getScore() {
		return score;
	}

	public boolean isEmpty(Regles.TYPESCORE type){
	    return this.score.get(type)==-1;
    }
    
	
    
}
