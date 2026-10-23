package com.mygdx.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.PowerUps.Curacion;
import com.mygdx.game.PowerUps.Danio;
import com.mygdx.game.PowerUps.PowerUp;
import com.mygdx.game.PowerUps.PowerUpsEffecto;
import com.mygdx.game.PowerUps.BalaPerforante;
import com.mygdx.game.disparos.*;
import com.mygdx.game.naves.*;


public class MundoJuego
{
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
	private ArrayList<PowerUp> powerUps = new ArrayList<>();
	
	private float tiempoAparecerPowerUp = 0f;
	private final float tiempoPowerUp = 10.0f; 
	private Random randomSpawn = new Random();
	
	//private SpaceNavigation game;
	
	public MundoJuego(
		int ronda, int vidas, int score,  
		int velXEnemigos, int velYEnemigos,
		int cantEnemigos, HashMap <String, Texture> texturas)
	{
		//this.game = game;
		this.ronda = ronda;
		this.score = score;
		this.velXEnemigos = velXEnemigos;
		this.velYEnemigos = velYEnemigos;
		this.cantEnemigos = cantEnemigos;
		
	    // cargar imagen de la nave, 64x64   
		nave = new NaveBasicaBuilder()
        	.posicion(Gdx.graphics.getWidth()/2-50, 30)
        	.sprite(texturas.get("Nave"))
        	.sonidoHerido(Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")))
        	.bala(texturas.get("Bala"), Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3")))
        	.vidas(vidas)
        	.build();

		//crear asteroides
		Random r = new Random();
	    for (int i = 0; i < cantEnemigos; i++)
		{
	    	int x = r.nextInt(1200);
	    	int y = 50+r.nextInt(800-50);
	    	int vx = velXEnemigos+r.nextInt(4);
	    	int vy = velYEnemigos+r.nextInt(4);
	    	
	    	Enemigos e;
	    	if(r.nextBoolean())
	    		e = new Tanque(x,y,vx,vy,texturas.get("Tanque"));

	    	else
	    		e = new Tirador(x,y,vx,vy,texturas.get("Tirador"));

	    	enemigos1.add(e);
	    	enemigos2.add(e);
	    }
	}
	
	public void colisionBalaEnemigo(float delta) {
		if (!nave.estaHerido()) {
			for (int i = 0; i < balas.size(); i++) {
				Bullet b = balas.get(i);
				b.update(delta);

				for (int j = 0; j < enemigos1.size(); j++) {
					Enemigos e = enemigos1.get(j);

					if (b.checkCollision(e)) {
						e.recibirDanio(nave.getDanio());

						if (e.estaMuerto()) {
							enemigos1.remove(j);
							enemigos2.remove(j);
							j--;

							score += 10;

							if (score > SpaceNavigation.getInstance().getHighScore()) {
								SpaceNavigation.getInstance().setHighScore(score);
							}
						}

						// La bala normal se destruye al impactar.
						// La perforante puede continuar hacia otros enemigos.
						if (b.isDestroyed()) {
							break;
						}
					}
				}

				if (b.isDestroyed()) {
					balas.remove(i);
					i--;
				}
			}
		}
}

	
	public void updateEnemigo(float delta, HashMap<String,Texture> texturas)
	{
		if (!nave.estaHerido())
		{
			//actualizar movimiento de asteroides dentro del area
			for (Enemigos enemigo : enemigos1)
			{
				/* Actualizar posicion de enemigo */
				enemigo.update();

				/* Validar si enemigo debe disparar */
				if(enemigo.debeDisparar(delta))
				{
					Bullet balasCreada = new Bullet(enemigo.getx(), enemigo.gety(), texturas.get("Bala"), new MovimientoRecto(0, -300), new ImpactoNormal());
					balasEnemigo.add(balasCreada);
				}
			}
		}
	}
	
	public void colisionEntreEnemigos()
	{
		if (!nave.estaHerido())
		{
			//colisiones entre asteroides y sus rebotes  
			for (int i=0;i<enemigos1.size();i++)
			{
				Enemigos ball1 = enemigos1.get(i);

				for (int j=0;j<enemigos2.size();j++)
				{
					Enemigos ball2 = enemigos2.get(j); 
					if (i<j)
						ball1.checkCollision(ball2);
		        }
		    }
		}
	}
	
	public void procesarBalaEnemiga(float delta)
	{
		if (!nave.estaHerido())
		{
			for (int i = 0; i < balasEnemigo.size(); i++)
			{
				Bullet be = balasEnemigo.get(i);
				be.update(delta);
					
				// Colisi�n con la nave principal
				if (nave.checkCollision(be)) 
					be.setDestruido(true);
	
				if (be.isDestroyed())
				{
					balasEnemigo.remove(i);
					i--;
				}
			}
		}	
	}
	
	public void colisionEnemigoNave()
	{
		for (int i = 0; i < enemigos1.size(); i++)
		{
			Enemigos b=enemigos1.get(i);

	        if (nave.checkCollision(b))
			{
	        	//asteroide se destruye con el choque             
	        	enemigos1.remove(i);
	        	enemigos2.remove(i);
	            i--;
            }  
		}
	}
	
	public boolean nivelSuperado()
	{
		if(enemigos1.size() == 0)
		{
			ronda ++;
			velXEnemigos ++;
			velYEnemigos ++;
			cantEnemigos += 3;

			return true;
		}
		return false;
	}

	public void actualizarPowerUps(float delta,HashMap<String, Texture> texturas) {
		// se Acumula el tiempo transcurrido en segundos
		tiempoAparecerPowerUp += delta;

	    //Si pasaron 20 segundos, se genera el power-up y reiniciamos el reloj
	    if (tiempoAparecerPowerUp >= tiempoPowerUp) {
	        generarPowerUpAleatorio(texturas);
	        tiempoAparecerPowerUp = 0f; // Reinicia el cronometro a 0
	    }
		
		for (int i = 0; i < powerUps.size(); i++) {
	        PowerUp p = powerUps.get(i);
	        
	        // Si ya fue recolectado, lo eliminamos de la lista
	        if (p.isRecolectado()) {
	            powerUps.remove(i);
	            i--;
	            continue;
	        }

	        // Si la nave colisiona con el power-up, se aplica el efecto
	        if (nave.getArea().overlaps(p.getArea())) {
	            p.aplicarA(nave);
	        }
	    }
	}

	public void generarPowerUpAleatorio(HashMap<String, Texture> texturas) {
	    // Coordenadas aleatorias dentro del ancho y alto del juego (ej: ancho 1200, alto 800)
	    int x = randomSpawn.nextInt(1000);
	    int y = 50 + randomSpawn.nextInt (700);

	    PowerUpsEffecto efecto;
	    String claveTextura;

		int tipo = randomSpawn.nextInt(3);

	    /* 1/3 de probabilidad en cada powerUp */
	    if (tipo == 0) {
	        efecto = new Curacion();
	        claveTextura = "Curacion";
	    }
		else if(tipo == 1)
		{
	        efecto = new Danio();
	        claveTextura = "Danio";
	    }
		else
		{
			efecto = new BalaPerforante();
			claveTextura = "BalaPerforante"
		}

	    // Creamos el power-up y se aniade a la lista
	    PowerUp nuevoPowerUp = new PowerUp(x, y, texturas.get(claveTextura), efecto);
	    agregarPowerUp(nuevoPowerUp);
	}
	
	public void agregarPowerUp(PowerUp p) {
	    powerUps.add(p);
	} 
	
	public int largoPowerUps() {
		return powerUps.size();
	}
	public PowerUp powerUpActual(int i) {
		return powerUps.get(i);
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
