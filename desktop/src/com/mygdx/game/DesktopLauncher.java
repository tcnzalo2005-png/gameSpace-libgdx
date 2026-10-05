package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.game.SpaceNavigation;

public class DesktopLauncher
{
	public static void main (String[] arg)
	{
		/* Objeto de configuracion de ventana */
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		config.setForegroundFPS(60);	/* Ejecutar ventana a 60fps en primer plano */
		config.setTitle("GameSpace");	/* Titulo de ventana */

		/* Arrancar el juego */
		new Lwjgl3Application(new SpaceNavigation(), config);
	}
}
