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
public class De implements Serializable{
    private Integer face;
    
    public void lancerLeDe(){
        face = (int) (Math.random()*6)+1;
    }

    public Integer getFace(){
        return this.face;
    }


    public De() {
        lancerLeDe();
    }

	public De(Integer face) {
		this.face = face;
	}

    
    public void setFace(Integer face){
        this.face = face;
    }

    @Override
    public String toString(){
        return face.toString();
    }
    
}
