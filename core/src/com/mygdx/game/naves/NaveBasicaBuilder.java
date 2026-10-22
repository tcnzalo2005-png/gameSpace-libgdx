package com.mygdx.game.naves;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.disparos.EstrategiaImpactoBala;
import com.mygdx.game.disparos.EstrategiaMovimientoBala;
import com.mygdx.game.disparos.ImpactoNormal;
import com.mygdx.game.disparos.MovimientoRecto;

// Builder concreto: arma la nave básica (la que ya existía).
// Los valores por defecto son los de la nave original.
public class NaveBasicaBuilder implements NaveBuilder {
    private int x, y;
    private Texture tx, txBala;
    private Sound sonidoHerido, soundBala;
    private int vidas = 5;                                                          // vidas por defecto
    private float velocidad = 300f;                                                 // velocidad por defecto
    private int tiempoHeridoMax = 50;                                               // frames en estado herido
    private EstrategiaMovimientoBala movimientoBala = new MovimientoRecto(0, 180);  // bala recta hacia arriba
    private EstrategiaImpactoBala impactoBala = new ImpactoNormal();                // bala se destruye al golpear

    // Cada paso guarda el valor y devuelve this para poder encadenar llamadas
    @Override 
    public NaveBuilder posicion(int x, int y) { 
    	this.x = x; this.y = y; return this; }
    @Override 
    public NaveBuilder sprite(Texture tx) { 
    	this.tx = tx; return this; }
    @Override 
    public NaveBuilder sonidoHerido(Sound s) { 
    	this.sonidoHerido = s; return this; }
    @Override public NaveBuilder bala(Texture txBala, Sound soundBala) {
        this.txBala = txBala; this.soundBala = soundBala; return this;
    }
    @Override 
    public NaveBuilder estrategiaDisparo(EstrategiaMovimientoBala e) { 
    	this.movimientoBala = e; return this; }
    @Override 
    public NaveBuilder vidas(int v) { 
    	this.vidas = v; return this; }
    @Override 
    public NaveBuilder velocidad(float v) { 
    	this.velocidad = v; return this; }
    @Override 
    public NaveBuilder tiempoHeridoMax(int t) { 
    	this.tiempoHeridoMax = t; return this; }

    // Crea la nave con todo lo configurado
    @Override
    public Nave4 build() {
        return new Nave4(x, y, tx, sonidoHerido, txBala, soundBala,
                         vidas, velocidad, tiempoHeridoMax, movimientoBala, impactoBala);
    }
}
