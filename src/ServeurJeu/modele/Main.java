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
    private final int NB_RELANCE = 2;
    private int nbRelance;
    
    public Main(){
	nbRelance = 0;
        des = new HashMap<Integer,De>();
        for(int i=1; i<=5; i++){
            des.put(i, new De());
        }
        listeDesARelancer = new ArrayList<Integer>();
    }

    //DEPRECIE
    public void MainCheat(int de, int face){
        des.get(de).setFace(face);
    }
    
    public void dellDeARelancer(int de){
	listeDesARelancer.remove(de);
    }

    public void clearListDeARelancer(){
        listeDesARelancer.clear();
    }

	public ArrayList<Integer> getListeDesARelancer() {
		return listeDesARelancer;
	}

    public void relancerLesDes(){
	if(nbRelance<NB_RELANCE){
		for(Integer i : listeDesARelancer){
		    des.get(i).lancerLeDe();
		}
		listeDesARelancer.clear();
		nbRelance++;
	}else{
		System.out.println("Nombre de relance maximum atteint");
	}
    }
    public void choixDeARelancer(int index){
        listeDesARelancer.add(index);
    }

    public HashMap<Integer, De> getDes() {
        return des;
    }
    
    @Override
    public String toString(){
        return getAffichageMain();
    }

    public String getAffichageMain(){
        String ligne0 = ("┌-------┐ ┌-------┐ ┌-------┐ ┌-------┐ ┌-------┐");
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
        String ligne4 = ("└-------┘ └-------┘ └-------┘ └-------┘ └-------┘");
	String ligne5 = ("    1         2         3         4         5");
	return ligne0  + "\n"+ ligne1  + "\n"+ligne2 + "\n"+ligne3 + "\n"+ligne4 + "\n" + ligne5 +"\n";
    }

    public Integer getScore(TYPESCORE type,int sup, int inf){
        switch(type){
            case AS :
                return Regles.pointNombreUnique(this,1);
            case DEUX :
                return Regles.pointNombreUnique(this,2);
            case TROIS :
                return Regles.pointNombreUnique(this,3);
            case QUATRE :
                return Regles.pointNombreUnique(this,4);
            case CINQ :
                return Regles.pointNombreUnique(this,5);
            case SIX :
                return Regles.pointNombreUnique(this,6);
            case SUPERIEUR :
                return Regles.pointSuperieur(this,inf);
            case INFERIEUR :
                return Regles.pointInferieur(this,sup);
            case CARRE :
                return Regles.pointCarre(this);
            case FULL :
                return Regles.pointFull(this);
            case PETITE_SUITE :
                return Regles.pointPetiteSuite(this);
            case GRANDE_SUITE :
                return Regles.pointGrandeSuite(this);
            case YAMS :
                return Regles.pointYams(this);
            default:
                return -1;
        }
    }

	public int getNbRelance() {
		return nbRelance;
	}

	public void setNbRelance(int nbRelance) {
		this.nbRelance = nbRelance;
	}

	public void setDes(HashMap<Integer, De> des) {
		this.des = des;
	}

	public void setListeDesARelancer(ArrayList<Integer> listeDesARelancer) {
		this.listeDesARelancer = listeDesARelancer;
	}

	public int getNB_RELANCE() {
		return NB_RELANCE;
	}
    
	
    
}
