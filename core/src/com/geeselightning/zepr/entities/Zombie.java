package com.geeselightning.zepr.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.geeselightning.zepr.game.Zepr;
import com.geeselightning.zepr.util.Constant;
import com.geeselightning.zepr.world.FixtureType;

public class Zombie extends Character {
	
	public enum Type {
		SLOW, MEDIUM, FAST
	}

	private int attackDamage = Constant.ZOMBIEDMG;
	private int hitRange = Constant.ZOMBIERANGE;
	private final float hitCooldown = Constant.ZOMBIEHITCOOLDOWN;
	private float healthMulti;
	private float speedMulti;
	private float damageMulti;

	public Zombie(Zepr parent, Sprite sprite, float bRadius, Vector2 initialPos, float initialRot, Type type) {
		super(parent, sprite, bRadius, initialPos, initialRot);
		switch (type) {
		case SLOW:
			healthMulti = Constant.SLOWHPMULT;
			speedMulti = Constant.SLOWSPEEDMULT;
			damageMulti = Constant.SLOWDMGMULT;
			break;
		case MEDIUM:
			healthMulti = Constant.MEDHPMULT;
			speedMulti = Constant.MEDSPEEDMULT;
			damageMulti = Constant.MEDDMGMULT;
			break;
		case FAST:
			healthMulti = Constant.FASTHPMULT;
			speedMulti = Constant.FASTSPEEDMULT;
			damageMulti = Constant.FASTDMGMULT;
			break;
		}
		this.speed = (int) (Constant.ZOMBIESPEED * speedMulti);
		this.health = (int) (Constant.ZOMBIEMAXHP * healthMulti);
		this.attackDamage = (int) (Constant.ZOMBIEDMG * damageMulti);
	}
	
	/* Accessor methods */
	
	@Override
	public void defineBody() {
		BodyDef bDef = new BodyDef();
		bDef.type = BodyDef.BodyType.DynamicBody;
		bDef.position.set(initialPos);
		
		FixtureDef fBodyDef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(this.bRadius);
		fBodyDef.shape = shape;
		
		b2body = world.createBody(bDef);
		b2body.createFixture(fBodyDef).setUserData(FixtureType.ENEMY);
		
		b2body.setUserData(this);
		shape.dispose();
		
		b2body.setLinearDamping(5.0f);
	}

	public void attack(float delta) {
		Player player = gameManager.getPlayer();
		if (this.distanceFrom(player) < hitRange && hitRefresh > hitCooldown) {
			player.takeDamage(attackDamage);
			hitRefresh = 0;
		} else {
			hitRefresh += delta;
		}
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		this.setLinearVelocity(getDirNormVector(gameManager.getPlayer().getPos()).scl(this.speed));
		
		Vector2 playerLoc = gameManager.getPlayer().getPos();
		double angle = Math.toDegrees(Math.atan2(playerLoc.y - b2body.getPosition().y, playerLoc.x - b2body.getPosition().x)) - 90;
		this.setAngle(angle);
		
		attack(delta);
	}
}
