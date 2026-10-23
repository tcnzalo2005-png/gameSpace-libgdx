package com.mygdx.game.PowerUps;

import com.mygdx.game.naves.Nave4;

public class BalaPerforante implements PowerUpsEffecto
{
    @Override
    public void aplicarEffecto(Nave4 nave)
    {
        nave.activarBalaPerforante(20f);
    }
}
