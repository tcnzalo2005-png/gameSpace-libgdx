package com.mygdx.game.naves;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import com.mygdx.game.Enemigos;
import com.mygdx.game.MundoJuego;
import com.mygdx.game.disparos.*;

public class Nave4 {
    private boolean destruida = false;
    private int vidas;
    private int danio = 1;
    private float xVel = 0;
    private float yVel = 0;
    private Sprite spr;
    private Sound sonidoHerido;
    private Sound soundBala;
    private Texture txBala;
    private boolean herido = false;
    private int tiempoHeridoMax;
    private int tiempoHerido;
    private float velocidad;

    private EstrategiaMovimientoBala movimientoBala;
    private EstrategiaImpactoBala impactoBala;
    private EstrategiaImpactoBala impactoBalaNormal;
    private float tiempoBalaPerforante = 0f;

    public Nave4(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala,
                 int vidas, float velocidad, int tiempoHeridoMax,
                 EstrategiaMovimientoBala movimientoBala, EstrategiaImpactoBala impactoBala) {
        this.sonidoHerido = soundChoque;
        this.soundBala = soundBala;
        this.txBala = txBala;
        this.vidas = vidas;
        this.velocidad = velocidad;
        this.tiempoHeridoMax = tiempoHeridoMax;
        this.movimientoBala = movimientoBala;
        this.impactoBala = impactoBala;
        this.impactoBalaNormal = impactoBala;
        spr = new Sprite(tx);
        spr.setBounds(x, y, 45, 45);
    }

    public void draw(SpriteBatch batch, MundoJuego juego) {
        float x = spr.getX();
        float y = spr.getY();
        float vel = velocidad * Gdx.graphics.getDeltaTime();          // <-- cambiado

        actualizarEfectos(Gdx.graphics.getDeltaTime());

        if (!herido) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) x -= vel;
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) x += vel;
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) y -= vel;
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) y += vel;

            if (x < 0) x = 0;
            if (x + spr.getWidth() > 1200) x = 1200 - spr.getWidth();
            if (y < 0) y = 0;
            if (y + spr.getHeight() > 800) y = 800 - spr.getHeight();

            spr.setPosition(x, y);
            spr.draw(batch);
        } else {
            spr.setX(spr.getX() + MathUtils.random(-2, 2));
            spr.draw(batch);
            spr.setX(x);
            tiempoHerido--;
            if (tiempoHerido <= 0) herido = false;
        }
        // disparo
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            Bullet bala = new Bullet(spr.getX() + spr.getWidth() / 2 - 5,
                                     spr.getY() + spr.getHeight() - 5,
                                     txBala, movimientoBala, impactoBala);  // <-- cambiado
            juego.agregarBala(bala);
            soundBala.play();
        }
    }

    public boolean checkCollision(Enemigos b) {
        if (!herido && b.getArea().overlaps(spr.getBoundingRectangle())) {
            // rebote
            if (xVel == 0) xVel += b.getXSpeed() / 2;
            if (b.getXSpeed() == 0) b.setXSpeed(b.getXSpeed() + (int) xVel / 2);
            xVel = -xVel;
            b.setXSpeed(-b.getXSpeed());

            if (yVel == 0) yVel += b.getySpeed() / 2;
            if (b.getySpeed() == 0) b.setySpeed(b.getySpeed() + (int) yVel / 2);
            yVel = -yVel;
            b.setySpeed(-b.getySpeed());

            // actualizar vidas y herir
            vidas--;
            herido = true;
            tiempoHerido = tiempoHeridoMax;
            sonidoHerido.play();
            if (vidas <= 0) destruida = true;
            return true;
        }
        return false;
    }

    public boolean checkCollision(Bullet b) {
        if (this.getArea().overlaps(b.getArea())) {
            // Lógica de daño a la nave cuando la impacta una bala
            vidas--;
            herido = true;
            tiempoHerido = tiempoHeridoMax;
            sonidoHerido.play();
            if (vidas <= 0) destruida = true;
            return true;
        }
        return false;
    }

    public void activarBalaPerforante(float duracion)
    {
        impactoBala = new ImpactoPerforante();
        tiempoBalaPerforante = duracion;
    }

    private void actualizarEfectos(float delta)
    {
        if(tiempoBalaPerforante > 0f)
        {
            tiempoBalaPerforante -= delta;
            
            if(tiempoBalaPerforante <= 0f)
            {
                tiempoBalaPerforante = 0f;
                impactoBala = impactoBalaNormal;
            }
        }
    }

    public boolean estaDestruido() { return !herido && destruida; }
    public boolean estaHerido() { return herido; }

    public int getVidas() { return vidas; }
    public int getX() { return (int) spr.getX(); }
    public int getY() { return (int) spr.getY(); }
    public void setVidas(int vidas2) { vidas = vidas2; }
    public Rectangle getArea() { return spr.getBoundingRectangle(); }
    
    public int getDanio() {
    	return danio;
    }
    public void setDanio(int d) {
    	danio = d;
    }

    public void setImpactoBala(EstrategiaImpactoBala impactoBala) {
        this.impactoBala = impactoBala;
    }

    public void setMovimientoBala(EstrategiaMovimientoBala movimientoBala) {
        this.movimientoBala = movimientoBala;
    }
}
