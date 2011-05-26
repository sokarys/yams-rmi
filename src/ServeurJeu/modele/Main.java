/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServeurJeu.modele;

import ServeurJeu.modele.Regles.TYPESCORE;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author root
 */
public class Main implements Serializable{
    private HashMap<Integer,De> des;
    private ArrayList<Integer> listeDesARelancer;
    
    public Main(){
        des = new HashMap<Integer,De>();
        for(int i=0; i<5; i++){
            des.put(i, new De());
        }
        listeDesARelancer = new ArrayList<Integer>();
    }

    public void MainCheat(int de, int face){
        des.get(de).setFace(face);
    }

    public void clearListDeARelancer(){
        listeDesARelancer.clear();
    }
    public void relancerLesDes(){
        for(Integer i : listeDesARelancer){
            des.get(i).lancerLeDe();
        }
        listeDesARelancer.clear();
    }
    public void choixDeARelancer(int index){
        listeDesARelancer.add(index);
    }

    public HashMap<Integer, De> getDes() {
        return des;
    }
    
    public Integer getScrore(Regles.TYPESCORE type){
        return 0;
    }

    @Override
    public String toString(){
        ArrayList<Integer> array = new ArrayList<Integer>();
        for(Entry<Integer, De> currentEntry : des.entrySet()) {
            array.add(currentEntry.getValue().getFace());
        }
        return array.toString();
    }

    public void afficherMain(){
        System.out.println("┌-------┐ ┌-------┐ ┌-------┐ ┌-------┐ ┌-------┐");
        String ligne1 = "";
        String ligne2 = "";
        String ligne3 = "";
        for(Entry<Integer, De> currentEntry : des.entrySet()) {
            switch(currentEntry.getValue().getFace()){
                case 1 :
                    ligne1 +="│       │ ";
                    ligne2 +="│   •   │ ";
                    ligne3 +="│       │ ";
                    break;
                case 2:
                    ligne1 +="│ •     │ ";
                    ligne2 +="│       │ ";
                    ligne3 +="│     • │ ";
                    break;
                case 3 :
                    ligne1 +="│ •     │ ";
                    ligne2 +="│   •   │ ";
                    ligne3 +="│     • │ ";
                    break;
                case 4 :
                    ligne1 +="│ •   • │ ";
                    ligne2 +="│       │ ";
                    ligne3 +="│ •   • │ ";
                    break;
                case 5 :
                    ligne1 +="│ •   • │ ";
                    ligne2 +="│   •   │ ";
                    ligne3 +="│ •   • │ ";
                    break;
                case 6 :
                    ligne1 +="│ •   • │ ";
                    ligne2 +="│ •   • │ ";
                    ligne3 +="│ •   • │ ";
                    break;
            }
        }
        System.out.println(ligne1);
        System.out.println(ligne2);
        System.out.println(ligne3);
        System.out.println("└-------┘ └-------┘ └-------┘ └-------┘ └-------┘");
    }

    public Integer getScore(Main m,TYPESCORE type,int X){
        switch(type){
            case AS :
                return Regles.pointNombreUnique(m,1);
            case DEUX :
                return Regles.pointNombreUnique(m,2);
            case TROIS :
                return Regles.pointNombreUnique(m,3);
            case QUATRE :
                return Regles.pointNombreUnique(m,4);
            case CINQ :
                return Regles.pointNombreUnique(m,5);
            case SIX :
                return Regles.pointNombreUnique(m,6);
            case SUPERIEUR :
                return Regles.pointSuperieur(m,X);
            case INFERIEUR :
                return Regles.pointInferieur(m,X);
            case CARRE :
                return Regles.pointCarre(m);
            case FULL :
                return Regles.pointFull(m);
            case PETITE_SUITE :
                return Regles.pointPetiteSuite(m);
            case GRANDE_SUITE :
                return Regles.pointGrandeSuite(m);
            case YAMS :
                return Regles.pointYams(m);
            default:
                return -1;
        }
    }
}
