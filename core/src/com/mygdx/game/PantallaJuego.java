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


public class PantallaJuego implements Screen {

	private SpaceNavigation game;
	private OrthographicCamera camera;	
	private SpriteBatch batch;
	private Sound explosionSound;
	private Music gameMusic;
	private MundoJuego mundoJuego;
	private HashMap<String, Texture> texturas; //guarda todas las texturas en un hashmap

	public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score,  
			int velXAsteroides, int velYAsteroides, int cantAsteroides) {
		this.game = game;
		
		batch = game.getBatch();
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
		
		mundoJuego = new MundoJuego(game,ronda,vidas,score,velXAsteroides,velYAsteroides,cantAsteroides
									,texturas);
        	
	}
	
    
	public void dibujaEncabezado() {

		CharSequence str = "Vidas: "+mundoJuego.nave4().getVidas()+" Ronda: "+mundoJuego.getRondas();
		game.getFont().getData().setScale(2f);		
		game.getFont().draw(batch, str, 10, 30);
		game.getFont().draw(batch, "Score:"+mundoJuego.getScore(), Gdx.graphics.getWidth()-150, 30);
		game.getFont().draw(batch, "HighScore:"+game.getHighScore(), Gdx.graphics.getWidth()/2-100, 30);
	}
	@Override
	public void render(float delta) {
		  Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		  mundoJuego.colisionBalaAsteroide();
		  mundoJuego.updateEnemigo(delta, texturas);
		  mundoJuego.colisionEntreEnemigos();
		  mundoJuego.procesarBalaEnemiga();
		  mundoJuego.colisionEnemigoNave();

		  batch.begin();
		  dibujaEncabezado();
	      //dibujar balas
	     for (int i = 0; i<mundoJuego.largoDeBalas(); i++) {  
	    	 Bullet b = mundoJuego.balaActual(i);
	          b.draw(batch);
	      }
	     
	     for(int i = 0; i<mundoJuego.largoDeBalasEnemigo(); i++) {
	    	 Bullet b = mundoJuego.balaActualEnemigo(i);
	    	 b.draw(batch);
	     }
	     
	     
	      mundoJuego.nave4().draw(batch, mundoJuego);
	      
	      
	      if (mundoJuego.nave4().estaDestruido()) {
  			if (mundoJuego.getScore() > game.getHighScore())
  				game.setHighScore(mundoJuego.getScore());
	    	Screen ss = new PantallaGameOver(game);
  			ss.resize(1200, 800);
  			game.setScreen(ss);
  			dispose();
  		  }
	      
	      for(int i = 0; i<mundoJuego.largoEnemigo1(); i++) {
	    	  Enemigos b = mundoJuego.getEnemigo(i);
	    	  b.draw(batch);
	      }
	      
	      
	      batch.end();
	     
	      Screen ss = mundoJuego.nivelSuperado();
	      if (ss != null) {
			ss.resize(1200, 800);
			game.setScreen(ss);
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
