package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;


import java.util.Random;

public class Tirador extends Enemigos {
	private float timerDisparo=0;
	private float tiempoEntreDisparos = 2.5f;
	private int pasos = 0; // contador para el movimiento ondulante

	public Tirador(int x, int y, int xSpeed, int ySpeed, Texture tx) {
		super(x,y,20,xSpeed,ySpeed,tx,3);
	}
	
	// Movimiento del Tirador: avanza en X y ondula en Y (zigzag suave)
	@Override
	protected void moverse() {
		pasos++;
		int ondulacion = (int) (Math.sin(pasos * 0.1) * 3);
		setx(getx() + getXSpeed());
		sety(gety() + getySpeed() + ondulacion);
	}

	@Override
	public boolean debeDisparar(float delta) {
		timerDisparo += delta;
		if(timerDisparo >= tiempoEntreDisparos) {
			timerDisparo = 0;
			Random r = new Random();
			if(r.nextBoolean()) {
				tiempoEntreDisparos = 4.0f;
			}else {
				tiempoEntreDisparos = 1.5f;
			}
			return true;
		}
		return false;
	}
	

}
