package com.mygdx.game;

import java.util.HashMap;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.PowerUps.PowerUp;
import com.mygdx.game.disparos.Bullet;

public class PantallaJuego implements Screen {

	//private SpaceNavigation game;
	private OrthographicCamera camera;	
	private SpriteBatch batch;
	private Sound explosionSound;
	private Music gameMusic;
	private MundoJuego mundoJuego;
	private HashMap<String, Texture> texturas; //guarda todas las texturas en un hashmap
	private ControladorNarrativa controlNarrativa;

	public PantallaJuego(int ronda, int vidas, int score, int velXAsteroides, int velYAsteroides, int cantAsteroides)
	{
		//this.game = game;
		controlNarrativa = new ControladorNarrativa(); //inicializa las narrativas
		//batch = game.getBatch();
		batch = SpaceNavigation.getInstance().getBatch();
		camera = new OrthographicCamera();	
		camera.setToOrtho(false, 800, 640);
		//inicializar assets; musica de fondo y efectos de sonido
		explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
		explosionSound.setVolume(1,0.5f);
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("Space-Invaders-PS1-OST-Pluto_004_48k.ogg")); //
		
		gameMusic.setLooping(true);
		gameMusic.setVolume(0.5f);
		gameMusic.play();
		
		texturas = new HashMap<>();
		//Textura enemigos
		texturas.put("Nave", new Texture(Gdx.files.internal("Space-Invaders.png")));
		texturas.put("Tanque", new Texture(Gdx.files.internal("NaveTanque.png")));
		texturas.put("Tirador", new Texture(Gdx.files.internal("NaveTirador.png")));
		texturas.put("Bala",new Texture(Gdx.files.internal("Rocket2.png")));
		texturas.put("Curacion",new Texture(Gdx.files.internal("corazon.png")));
		texturas.put("Danio",new Texture(Gdx.files.internal("daño.jpg")));
		
		mundoJuego = new MundoJuego(ronda,vidas,score,velXAsteroides,velYAsteroides,cantAsteroides,texturas);
	}
	
    
	public void dibujaEncabezado()
	{
		CharSequence str = "Vidas: "+mundoJuego.nave4().getVidas()+" Ronda: "+mundoJuego.getRondas();
		SpaceNavigation.getInstance().getFont().getData().setScale(2f);
		SpaceNavigation.getInstance().getFont().draw(batch, str, 10, 30);
		SpaceNavigation.getInstance().getFont().draw(batch, "Score:"+mundoJuego.getScore(),280, 30);
		SpaceNavigation.getInstance().getFont().draw(batch, "HighScore:"+SpaceNavigation.getInstance().getHighScore(), 420, 30);
	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		mundoJuego.colisionBalaEnemigo(delta);
		mundoJuego.updateEnemigo(delta, texturas);
		mundoJuego.colisionEntreEnemigos();
		mundoJuego.procesarBalaEnemiga(delta);
		mundoJuego.colisionEnemigoNave();
		mundoJuego.actualizarPowerUps(delta,texturas);

		batch.begin();
		dibujaEncabezado();
		if( controlNarrativa.entregarMensaje(mundoJuego.getScore()) != null) {
			SpaceNavigation.getInstance().getFont().draw(batch,controlNarrativa.entregarMensaje(mundoJuego.getScore()),300,770);
		}
		//dibujar balas
		for (int i = 0; i<mundoJuego.largoDeBalas(); i++) {  
			Bullet b = mundoJuego.balaActual(i);
			b.draw(batch);
		}
	     
	     for(int i = 0; i<mundoJuego.largoDeBalasEnemigo(); i++) {
	    	 Bullet b = mundoJuego.balaActualEnemigo(i);
	    	 b.draw(batch);
	     }
	     
	     for(int i = 0; i<mundoJuego.largoPowerUps(); i++) {
	    	 PowerUp p = mundoJuego.powerUpActual(i);
	    	 p.draw(batch);
	     }
	     
	      mundoJuego.nave4().draw(batch, mundoJuego);
	      
	      
	      if (mundoJuego.nave4().estaDestruido()) {
  			if (mundoJuego.getScore() > SpaceNavigation.getInstance().getHighScore())
  				SpaceNavigation.getInstance().setHighScore(mundoJuego.getScore());
	    	Screen ss = new PantallaGameOver();
  			ss.resize(1200, 800);
  			SpaceNavigation.getInstance().setScreen(ss);
  			//game.setScreen(ss);
  			dispose();
  		  }
	      
	      for(int i = 0; i<mundoJuego.largoEnemigo1(); i++) {
	    	  Enemigos b = mundoJuego.getEnemigo(i);
	    	  b.draw(batch);
	      }
	      
	      
	      batch.end();
	     
	      
	      if (mundoJuego.nivelSuperado()) {

	    	  Screen ss = new PantallaJuego( 
	    			  mundoJuego.getRondas(),
	    			  mundoJuego.nave4().getVidas(),
	    			  mundoJuego.getScore(),
	    			  mundoJuego.getVelXAsteroide(),
	    			  mundoJuego.getVelYAsteroide(),
	    			  mundoJuego.getCantEnemigos()
	    			  );
	    	  ss.resize(1200, 800);
	    	  SpaceNavigation.getInstance().setScreen(ss);
	    	  //game.setScreen(ss);
	    	  dispose();
		  }
	    	 
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		gameMusic.play();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		this.explosionSound.dispose();
		this.gameMusic.dispose();
		/*
		if (txTanque != null) txTanque.dispose();
		if (txTirador != null) txTirador.dispose();
		if (txBalaEnemiga != null) txBalaEnemiga.dispose();
		*/
	}
   
}
