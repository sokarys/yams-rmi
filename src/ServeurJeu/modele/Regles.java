/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServeurJeu.modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author root
 */
public class Regles implements Serializable{

    public static enum TYPESCORE {AS,DEUX,TROIS,QUATRE,CINQ,SIX,SUPERIEUR,INFERIEUR,CARRE,FULL,PETITE_SUITE,GRANDE_SUITE,YAMS};

    //***********************************
    //* Partie supérieure de la feuille
    //***********************************
    public static Integer pointNombreUnique(Main main,Integer nombre){
        HashMap<Integer,De> des = main.getDes();
        Integer nbElements = 0;
        for (Entry<Integer, De> currentEntry : des.entrySet()) {
            De de = currentEntry.getValue();
            if(de.getFace() == nombre){
                nbElements++;
            }
        }
        return nbElements*nombre;
    }

    //***********************************
    //* Partie centrale de la feuille
    //***********************************

    public static Integer pointSuperieur(Main main, Integer pointsInferieur){
        HashMap<Integer,De> des = main.getDes();
        Integer somme = 0;
        for(Entry<Integer, De> currentEntry : des.entrySet()) {
            De de = currentEntry.getValue();
            somme += de.getFace();
        }
        // -1 indique que la case "Inférieur" n'est pas encore remplie
        if(pointsInferieur == -1){
            return somme;
        }else{
            if(somme > pointsInferieur){
                return somme;
            }else{
                return 0;
            }
        }
    }

    public static Integer pointInferieur(Main main, Integer pointsSuperieur){
        HashMap<Integer,De> des = main.getDes();
        Integer somme = 0;
        for(Entry<Integer, De> currentEntry : des.entrySet()) {
            De de = currentEntry.getValue();
            somme += de.getFace();
        }
        // -1 indique que la case "Supérieur" n'est pas encore remplie
        if(pointsSuperieur == -1){
            return somme;
        }else{
            if(somme < pointsSuperieur){
                return somme;
            }else{
                return 0;
            }
        }
    }

    //***********************************
    //* Partie inférieure de la feuille
    //***********************************

    public static Integer pointCarre(Main main){
        HashMap<Integer,De> des = main.getDes();
        HashMap<Integer,Integer> occurences = new HashMap<Integer, Integer>();
        Integer somme = 0;
        //Intitialisation
        for(int i=1; i<=6;i++){
            occurences.put(i, 0);
        }
        //Remplissage
        for(Entry<Integer, De> currentEntry : des.entrySet()) {
            De de = currentEntry.getValue();
            somme += de.getFace();
            occurences.put(de.getFace(),occurences.get(de.getFace())+1);
        }
        //Vérification
        for(Entry<Integer, Integer> currentEntry : occurences.entrySet()) {
            Integer nbrOccurences = currentEntry.getValue();
            if(nbrOccurences >= 4){
                return 40 + somme;
            }
        }
        return 0;
    }

    public static Integer pointFull(Main main){
        HashMap<Integer,De> des = main.getDes();
        HashMap<Integer,Integer> occurences = new HashMap<Integer, Integer>();
        Integer somme = 0;
        Boolean DeuxIdentiques = false;
        Boolean TroisIdentiques = false;
        //Intitialisation
        for(int i=1; i<=6;i++){
            occurences.put(i, 0);
        }
        //Remplissage
        for(Entry<Integer, De> currentEntry : des.entrySet()) {
            De de = currentEntry.getValue();
            somme += de.getFace();
            occurences.put(de.getFace(),occurences.get(de.getFace())+1);
        }
        //Vérification
        for(Entry<Integer, Integer> currentEntry : occurences.entrySet()) {
            Integer nbrOccurences = currentEntry.getValue();
            if(nbrOccurences == 3){
                TroisIdentiques = true;
            }else if(nbrOccurences == 2){
                DeuxIdentiques = true;
            }
        }
        if(DeuxIdentiques && TroisIdentiques){
            return 30 + somme;
        }else{
            return 0;
        }
    }

    public static Integer pointPetiteSuite(Main main){
        HashMap<Integer,De> des = main.getDes();
        ArrayList<Integer> arrayDes = new ArrayList<Integer>();
        for(Entry<Integer, De> currentEntry : des.entrySet()) {
            De de = currentEntry.getValue();
            if(!arrayDes.contains(de.getFace())){
                arrayDes.add(de.getFace());
            }
        }
        Collections.sort(arrayDes);
        if(arrayDes.size() >= 4){
            for(int i=1; i<arrayDes.size(); i++){
                if(arrayDes.get(i) != arrayDes.get(i-1) + 1){
                    return 0;
                }
            }
            return 45;
        }
        return 0;
    }

    public static Integer pointGrandeSuite(Main main){
        HashMap<Integer,De> des = main.getDes();
        ArrayList<Integer> arrayDes = new ArrayList<Integer>();
        for(Entry<Integer, De> currentEntry : des.entrySet()) {
            De de = currentEntry.getValue();
            if(!arrayDes.contains(de.getFace())){
                arrayDes.add(de.getFace());
            }
        }
        Collections.sort(arrayDes);
        if(arrayDes.size() == 5){
            for(int i=1; i<arrayDes.size(); i++){
                if(arrayDes.get(i) != arrayDes.get(i-1) + 1){
                    return 0;
                }
            }
            return 50;
        }
        return 0;
    }

    public static Integer pointYams(Main main){
        HashMap<Integer,De> des = main.getDes();
        HashMap<Integer,Integer> occurences = new HashMap<Integer, Integer>();
        Integer somme = 0;
        //Intitialisation
        for(int i=1; i<=6;i++){
            occurences.put(i, 0);
        }
        //Remplissage
        for(Entry<Integer, De> currentEntry : des.entrySet()) {
            De de = currentEntry.getValue();
            somme += de.getFace();
            occurences.put(de.getFace(),occurences.get(de.getFace())+1);
        }
        //Vérification
        for(Entry<Integer, Integer> currentEntry : occurences.entrySet()) {
            Integer nbrOccurences = currentEntry.getValue();
            if(nbrOccurences == 5){
                return 50 + somme;
            }
        }
        return 0;
    }

    /* Au Yahtzee uniquement
    public Integer pointChance(Main main){
        HashMap<Integer,De> des = main.getDes();
        return 0;
    }

    public Integer pointBrelan(Main main){
        HashMap<Integer,De> des = main.getDes();
        return 0;
    }
    */
}
