/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServeurJeu.modele;

import java.io.Serializable;

/**
 *
 * @author root
 */
public class Regles implements Serializable{
    public static enum TYPESCORE {AS,DEUX,TROIS,QUATRE,CINQ,SIX,SUPERIEUR,INFERIEUR,CARRE,FULL,PETITE_SUITE,GRANDE_SUITE,YAMS};
    
    /*public Integer insertScore(Main m, FeuilleScore.TYPESCORE type,int X){
        switch(type){
            case AS :
                return pointNombreUnique(m,1);
            case DEUX :
                return pointNombreUnique(m,2);
            case TROIS :
                return pointNombreUnique(m,3);
            case QUATRE :
                return pointNombreUnique(m,4); 
            case CINQ :
                return pointNombreUnique(m,5);
            case SIX :
                return pointNombreUnique(m,6);
            case SUPERIEUR :
                return pointSuperieur(m,X);
            case INFERIEUR :
                return pointInferieur(m,X);
            case CARRE :
                return pointCarre(m);
            case FULL :
                return pointFull(m);
            case PETITE_SUITE :
                return pointPetiteSuite(m);
            case GRANDE_SUITE :
                return pointGrandeSuite(m);     
            case YAMS :
                return PointYams(m);
            default:
                return -1;
        }
    }*/
}
