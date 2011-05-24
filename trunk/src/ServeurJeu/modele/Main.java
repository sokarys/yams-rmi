/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServeurJeu.modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author root
 */
public class Main implements Serializable{
    private HashMap<Integer,De> Des;
    private ArrayList<Integer> listeDesARelancer;
    private Regles regle;
    
    public Main(){
        Des = new HashMap<Integer,De>();
        listeDesARelancer = new ArrayList<Integer>();
        regle = new Regles();
    }
    
    public void clearListDeARelancer(){
        listeDesARelancer.clear();
    }
    
    public void addDeARelancer(){
        
    }
    
    public void relancerDes(){
        
    }
    
    public Integer getScrore(Regles.TYPESCORE type){
        return 0;
    }
}
