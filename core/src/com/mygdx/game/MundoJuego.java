package com.mygdx.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;


public class MundoJuego {
	private int score;
	private int ronda;
	private int velXEnemigos; 
	private int velYEnemigos; 
	private int cantEnemigos;
	
	private Nave4 nave;
	private ArrayList<Enemigos> enemigos1 = new ArrayList<>();
	private ArrayList<Enemigos> enemigos2 = new ArrayList<>();
	private ArrayList<Bullet> balasEnemigo = new ArrayList<>();
	private ArrayList<Bullet> balas = new ArrayList<>();
	
	private SpaceNavigation game;
	
	public MundoJuego(SpaceNavigation game, int ronda, int vidas, int score,  
			int velXEnemigos, int velYEnemigos, int cantEnemigos, HashMap <String, Texture> texturas) {
		
		this.game = game;
		this.ronda = ronda;
		this.score = score;
		this.velXEnemigos = velXEnemigos;
		this.velYEnemigos = velYEnemigos;
		this.cantEnemigos = cantEnemigos;
		
	    // cargar imagen de la nave, 64x64   
	    nave = new Nave4(Gdx.graphics.getWidth()/2-50,30,texturas.get("Nave"),
	    				Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")), 
	    				texturas.get("Bala"), 
	    				Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3"))); 
		
		nave.setVidas(vidas);
		//crear asteroides
		Random r = new Random();
	    for (int i = 0; i < cantEnemigos; i++) {
	    	int x = r.nextInt((int)Gdx.graphics.getWidth());
	    	int y = 50+r.nextInt((int)Gdx.graphics.getHeight()-50);
	    	int vx = velXEnemigos+r.nextInt(4);
	    	int vy = velYEnemigos+r.nextInt(4);
	    	
	    	Enemigos e;
	    	if(r.nextBoolean()) {
	    		e = new Tanque(x,y,vx,vy,texturas.get("Tanque"));
	    	}else {
	    		e = new Tirador(x,y,vx,vy,texturas.get("Tirador"));
	    	}
	    	enemigos1.add(e);
	    	enemigos2.add(e);
		
	    }
	}
	
	public void colisionBalaAsteroide() {
		if (!nave.estaHerido()) {
			for (int i = 0; i < balas.size(); i++) {
	            Bullet b = balas.get(i);
	            b.update();
	            for (int j = 0; j < enemigos1.size(); j++) { 
	            	Enemigos e = enemigos1.get(j);
	            	if(b.getArea().overlaps(e.getArea())) {
	            		//explosionSound.play(0.10f);
	            		b.setDestruido(true);
	            		e.recibirDanio();
	            		
	            		if(e.estaMuerto()) {
	            			enemigos1.remove(j);
	            			enemigos2.remove(j);
	            			j--;
	            			score+=10;
	            			if(score > game.getHighScore()) {
	            				game.setHighScore(score);
	            			}
	            		}
	            		break;
	            	}   	  
	  	        }
	                
	         
	            if (b.isDestroyed()) {
	                balas.remove(b);
	                i--; //para no saltarse 1 tras eliminar del arraylist
	            }
	      }
		}
	}
	
	public void updateEnemigo(float delta,HashMap<String,Texture> texturas) {
		if (!nave.estaHerido()) {

			//actualizar movimiento de asteroides dentro del area
			for (Enemigos ball : enemigos1) {
				ball.update();
				if(ball instanceof Tirador) {
					Tirador t = (Tirador) ball;
		        	  
					if(t.debeDisparar(delta)) {
						Bullet balasCreada = new Bullet(t.getx(),t.gety(),0,-5,texturas.get("Bala"));
						balasEnemigo.add(balasCreada);
		        		  
					}
				}
		          
			}
		}
	}
	
	public void colisionEntreEnemigos() {
		if (!nave.estaHerido()) {
			//colisiones entre asteroides y sus rebotes  
			for (int i=0;i<enemigos1.size();i++) {
				Enemigos ball1 = enemigos1.get(i);   
				for (int j=0;j<enemigos2.size();j++) {
					Enemigos ball2 = enemigos2.get(j); 
					if (i<j) {
						ball1.checkCollision(ball2);
		     
					}
		        	}
		    }
		}

	}
	
	public void procesarBalaEnemiga() {
		if (!nave.estaHerido()) {
			for (int i = 0; i < balasEnemigo.size(); i++) {
				Bullet be = balasEnemigo.get(i);
				be.update();
					
	
				// Colisión con la nave principal
				if (nave.checkCollision(be)) { 
					be.setDestruido(true);
				}
	
				if (be.isDestroyed()) {
					balasEnemigo.remove(i);
					i--;
					}
			}
		}	
	}
	

	
	public void colisionEnemigoNave() {
		for (int i = 0; i < enemigos1.size(); i++) {
			Enemigos b=enemigos1.get(i);

	    	    
	        if (nave.checkCollision(b)) {
	        	//asteroide se destruye con el choque             
	        	enemigos1.remove(i);
	        	enemigos2.remove(i);
	            i--;
            }
               	  
		}
	}
	
	public PantallaJuego nivelSuperado() {
		if(enemigos1.size() == 0) {
			ronda ++;
			velXEnemigos ++;
			velYEnemigos ++;
			cantEnemigos += 3;
			PantallaJuego ss = new PantallaJuego(game,ronda,nave.getVidas(),score,velXEnemigos,velYEnemigos
					,cantEnemigos);
			return ss;
		}
		return null;
	}
    public boolean agregarBala(Bullet bb) {
    	return balas.add(bb);
    }
	
	public int largoDeBalas() {
		return balas.size();
	}
	public Bullet balaActual(int i) {
		return balas.get(i);
	}
	
	public int largoDeBalasEnemigo() {
		return balasEnemigo.size();
	}
	public Bullet balaActualEnemigo(int i) {
		return balasEnemigo.get(i);
	}
	
	public int largoEnemigo1() {
		return enemigos1.size();
	}
	
	public Enemigos getEnemigo(int i) {
		return enemigos1.get(i);
	}

	public int largoEnemigo2() {
		return enemigos2.size();
	}

	public int getVelXAsteroide() {
		return velXEnemigos;
	}
	public int getVelYAsteroide() {
		return velYEnemigos;
	}
	public int getCantEnemigos() {
		return cantEnemigos;
	}
	public int getScore() {
		return score;
	}
	public int getRondas() {
		return ronda;
	}
	
	public void setVelXEnemigo(int i) {
		velXEnemigos = i;
	}
	public void setVelYEnemigo(int i) {
		velYEnemigos = i;
	}
	public void setCantEnemigos(int i) {
		cantEnemigos = i;
	}
	public void setScore(int i) {
		score = i;
	}
	public void setRondas(int i) {
		ronda = i;
	}
	
	public Nave4 nave4() {
		return nave;
	}
}
