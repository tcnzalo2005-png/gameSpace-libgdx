package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Enemigos
{
	private int x;
    private int y;
    private int xSpeed;
    private int ySpeed;
    private int vidas;
    private Sprite spr;

    protected Enemigos(int x, int y, int size, int xSpeed, int ySpeed, Texture tx,int vidas) {
    	spr = new Sprite(tx);
    	spr.setSize(45, 45);
    	this.x = x; 
    	this.vidas = vidas;
 	
        //validar que borde de esfera no quede fuera
    	if (x-size < 0) this.x = x+size;
    	/*if (x+size > Gdx.graphics.getWidth())this.x = x-size;*/
    	if (x+size > 1200)this.x = x-size;
    	
         
        this.y = y;
        //validar que borde de esfera no quede fuera
    	if (y-size < 0) this.y = y+size;
    	/*if (y+size > Gdx.graphics.getHeight())this.y = y-size;*/
    	if (y+size > 800)this.y = y-size;
    	
        spr.setPosition(x, y);
        this.setXSpeed(xSpeed);
        this.setySpeed(ySpeed);
    }
    /**
     * METODO PLANTILLA (Template Method).
     * Define el esqueleto fijo de la actualizacion de un enemigo:
     *   1. moverse()          -> paso VARIABLE (cada subclase lo implementa)
     *   2. rebotarEnBordes()  -> paso COMUN (hook, se puede sobreescribir)
     *   3. actualizar sprite  -> paso COMUN
     * Es final para que las subclases no alteren el orden de los pasos.
     */
    public final void update() {
        moverse();
        rebotarEnBordes();
        spr.setPosition(x, y);
    }

    /** Paso variable: cada tipo de enemigo define su patron de movimiento. */
    protected abstract void moverse();

    /** Paso comun con implementacion por defecto: rebote contra los bordes del mapa. */
    protected void rebotarEnBordes() {
        if (x+getXSpeed() < 0 || x+getXSpeed()+spr.getWidth() > 1200)
        	setXSpeed(getXSpeed() * -1);
        if (y+getySpeed() < 0 || y+getySpeed()+spr.getHeight() > 800)
        	setySpeed(getySpeed() * -1);
    }

    // Acceso para que las subclases modifiquen la posicion dentro de moverse()
    protected void setx(int x) { this.x = x; }
    protected void sety(int y) { this.y = y; }

    public Rectangle getArea() {
    	return spr.getBoundingRectangle();
    }
    public void draw(SpriteBatch batch) {
    	spr.draw(batch);
    }
    
    public void checkCollision(Enemigos b2) {
        if(spr.getBoundingRectangle().overlaps(b2.spr.getBoundingRectangle())){
        	// rebote
            if (getXSpeed() ==0) setXSpeed(getXSpeed() + b2.getXSpeed()/2);
            if (b2.getXSpeed() ==0) b2.setXSpeed(b2.getXSpeed() + getXSpeed()/2);
        	setXSpeed(- getXSpeed());
            b2.setXSpeed(-b2.getXSpeed());
            
            if (getySpeed() ==0) setySpeed(getySpeed() + b2.getySpeed()/2);
            if (b2.getySpeed() ==0) b2.setySpeed(b2.getySpeed() + getySpeed()/2);
            setySpeed(- getySpeed());
            b2.setySpeed(- b2.getySpeed()); 
        }
    }
	public int getXSpeed() {
		return xSpeed;
	}
	public void setXSpeed(int xSpeed) {
		this.xSpeed = xSpeed;
	}
	public int getySpeed() {
		return ySpeed;
	}
	public void setySpeed(int ySpeed) {
		this.ySpeed = ySpeed;
	}
	
    public void recibirDanio(int d) {
    	vidas -= d;
    }
    public boolean estaMuerto() {
    	return vidas <= 0;
    }
    public int getx() {
    	return x;
    }
    public int gety() {
    	return y;
    }
    
    
    /** Hook: por defecto los enemigos no disparan; las subclases pueden sobreescribirlo. */
    public boolean debeDisparar(float delta) {
    	return false;
    }

    
}
