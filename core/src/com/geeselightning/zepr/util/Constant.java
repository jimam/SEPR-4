package com.geeselightning.zepr.util;

import com.badlogic.gdx.math.Vector2;

/**
 * Defines constants used repeatedly in other classes in the program.
 * @author Xzytl
 * Changes:
 * 	- added different multipliers for different zombie types
 * 	- added definition of PPM (pixels-per-metre) and PPT (pixels-per-tile)
 */
public final class Constant {
	
	public static final int PPM = 50;
	public static final int PPT = 32;
	
	public static final Vector2 ORIGIN = new Vector2(0, 0);
	public static final float PLAYERSPEED = 6;
	public static final int PLAYERMAXHP = 100;
	public static final int PLAYERDMG = 20;
	public static final int PLAYERRANGE = 50;
	public static final float PLAYERHITCOOLDOWN = 0.2f;

	public static final float ZOMBIESPEED = 4;
	public static final int ZOMBIEMAXHP = 100;
	public static final int ZOMBIEDMG = 10;
	public static final int ZOMBIERANGE = 20;
	public static final float ZOMBIEHITCOOLDOWN = 1;

	public static final float SLOWSPEEDMULT = 0.5f;
	public static final float SLOWHPMULT = 2f;
	public static final float SLOWDMGMULT = 1;

	public static final float MEDSPEEDMULT = 1;
	public static final float MEDHPMULT = 1.5f;
	public static final float MEDDMGMULT = 2f;

	public static final float FASTSPEEDMULT = 2f;
	public static final float FASTHPMULT = 1;
	public static final float FASTDMGMULT = 1;

	public static final int HEALUP = 30;
	public static final int SPEEDUP = 50;
	public static final float SPEEDUPTIME = 10;
	public static final float IMMUNITYTIME = 5;
	public static final float ATKUPTIME = 10;
	
}
