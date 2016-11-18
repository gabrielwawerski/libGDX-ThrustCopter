package com.masmix.thrustcopter.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.masmix.thrustcopter.ThrustCopter;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 800;
        config.height = 400;
        config.resizable = false;
		new LwjglApplication(new ThrustCopter(), config);
	}
}
