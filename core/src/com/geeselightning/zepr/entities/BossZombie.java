package com.geeselightning.zepr.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.geeselightning.zepr.game.Zepr;
import com.geeselightning.zepr.util.Constant;

public class BossZombie extends Zombie {
	
	public enum Type {
		MINIBOSS,
		BOSS
	}

	public BossZombie(Zepr parent, Sprite sprite, float bRadius, Vector2 initialPos, float initialRot, Type type) {
		super(parent, sprite, bRadius, initialPos, initialRot, null);
		this.speed = (int) (Constant.ZOMBIESPEED * 0.75f);
		this.health = (int) (Constant.ZOMBIEMAXHP * 5f);
		this.attackDamage = (int) (Constant.ZOMBIEDMG * 2f);
	}
	
	

}
