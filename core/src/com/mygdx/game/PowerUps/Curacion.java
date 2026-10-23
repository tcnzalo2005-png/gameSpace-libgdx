package com.mygdx.game.PowerUps;

import com.mygdx.game.naves.Nave4;

public class Curacion implements PowerUpsEffecto {
	private int cantVidas = 1;
	
	@Override
	public void aplicarEffecto(Nave4 nave) {
		int vidasActuales = nave.getVidas();
		nave.setVidas(vidasActuales + cantVidas);
	}	
}
