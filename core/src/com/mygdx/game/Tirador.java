package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class Tirador extends Ball2 {
	private float timerDisparo=0;
	private float tiempoEntreDisparos = 2.5f;

	public Tirador(int x, int y, int xSpeed, int ySpeed, Texture tx) {
		super(x,y,20,xSpeed,ySpeed,tx,3);
	}
	
	public boolean debeDisparar(float delta) {
		timerDisparo += delta;
		if(timerDisparo >= tiempoEntreDisparos) {
			timerDisparo = 0;
			return true;
		}
		return false;
	}
}	
