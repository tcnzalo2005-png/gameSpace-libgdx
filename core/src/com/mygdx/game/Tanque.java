package com.mygdx.game;
 
import com.badlogic.gdx.graphics.Texture;
 
public class Tanque extends Enemigos {
	public Tanque(int x, int y, int xSpeed, int ySpeed, Texture tx) {
		super(x,y,30,xSpeed,ySpeed,tx,10);
	}
 
	// Movimiento del Tanque: lineal y constante (avanza segun su velocidad)
	@Override
	protected void moverse() {
		setx(getx() + getXSpeed());
		sety(gety() + getySpeed());
	}
}
 
