/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServeurJeu.modele;

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
}
