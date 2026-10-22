package com.mygdx.game.PowerUps;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.naves.Nave4;

public class PowerUp {
	private Sprite spr;
	private PowerUpsEffecto efecto;
	private boolean recolectado = false;
	
	public PowerUp(float x, float y, Texture tx, PowerUpsEffecto efecto) {
		spr = new Sprite(tx);
		spr.setBounds(x, y, 35, 35);
		this.efecto = efecto;
	}
	
	public void draw(SpriteBatch batch) {
        if (!recolectado) {
            spr.draw(batch);
        }
    }
	
	public void aplicarA(Nave4 nave) {
        if (!recolectado) {
            efecto.aplicarEffecto(nave); // Polimorfismo puro: ejecuta el efecto que tenga asignado
            recolectado = true;
        }
    }
	
	public boolean isRecolectado() { 
		return recolectado; 
		}
    public Rectangle getArea() { 
    	return spr.getBoundingRectangle(); 
    	}
}
