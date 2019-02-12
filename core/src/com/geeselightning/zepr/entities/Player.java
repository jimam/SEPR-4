
package com.geeselightning.zepr.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.geeselightning.zepr.game.GameManager;
import com.geeselightning.zepr.game.Zepr;
import com.geeselightning.zepr.levels.Level;
import com.geeselightning.zepr.util.Constant;
import com.geeselightning.zepr.world.FixtureType;

public class Player extends Character {
	
	public enum Type {
		
		NERDY(1.5f, 1.0f, 1.0f),
		SPORTY(1.0f, 1.0f, 1.5f),
		HEAVY(1.0f, 1.5f, 1.0f);
		
		float healthMultiplier;
		float damageMultiplier;
		float speedMultiplier;
		
		Type(float healthMultiplier, float damageMultiplier, float speedMultiplier) {
			this.healthMultiplier = healthMultiplier;
			this.damageMultiplier = damageMultiplier;
			this.speedMultiplier = speedMultiplier;
		}
	}

	private Type type;
	
	private boolean attacking;
	private boolean immune;
	
	private int attackDamage = Constant.PLAYERDMG;
	private int hitRange = Constant.PLAYERRANGE;
	private float hitCooldown = Constant.PLAYERHITCOOLDOWN;
	private Texture mainTexture;
	private Texture attackTexture;
	
	public Player(Zepr parent, float bRadius, Vector2 initialPos, float initialRot, Type type) {
		super(parent, new Sprite(new Texture("player01.png")), bRadius, initialPos, initialRot);
		this.type = type;
		if (type == Type.NERDY) {
			// Nerdy Texture
			mainTexture = new Texture("player01.png");
			attackTexture = new Texture("player01_attack.png");
		} else if (type == Type.SPORTY) {
			// Sporty Texture
			mainTexture = new Texture("player02.png");
			attackTexture = new Texture("player02_attack.png");
		} else if (type == Type.HEAVY) {
			// Test Texture
			mainTexture = new Texture("player03.png");
			attackTexture = new Texture("player03_attack.png");
		} else {
			// Default Nerdy playertype
			mainTexture = new Texture("player01.png");
			attackTexture = new Texture("player01_attack.png");
		}
		this.attackDamage = (int) (Constant.PLAYERDMG * type.damageMultiplier);
		this.speed = (int) (Constant.PLAYERSPEED * type.speedMultiplier);
		this.health = (int) (Constant.PLAYERMAXHP * type.healthMultiplier);
		this.sprite.setTexture(mainTexture);
	}
	
	/* Accessor methods */

	public Player.Type getType() {
		return type;
	}
	
	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}
	
	public boolean isAttacking() {
		return attacking;
	}
	
	public void setImmune(boolean immune) {
		this.immune = immune;
	}
	
	public boolean isImmune() {
		return immune;
	}

	public void attack(Zombie zombie, float delta) {
		if (canHitGlobal(zombie, hitRange) && hitRefresh > hitCooldown) {
			zombie.takeDamage(attackDamage);
			hitRefresh = 0;
		} else {
			hitRefresh += delta;
		}
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		// Gives the player the attack texture for 0.1s after an attack.
		// if (hitRefresh <= 0.1 && getTexture() != attackTexture) {
		if (attacking) {
			this.sprite.setTexture(attackTexture);
		} else {
			// Changes the texture back to the main one after 0.1s.
			// if (hitRefresh > 0.1 && getTexture() == attackTexture) {
			this.sprite.setTexture(mainTexture);
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		Vector2 mousePos = gameManager.getMouseWorldPos();
		double angle = Math
				.toDegrees(Math.atan2(mousePos.y - b2body.getPosition().y, mousePos.x - b2body.getPosition().x));
		this.setAngle((float) (angle - 90));
		super.draw(batch);
	}

	@Override
	public void takeDamage(int dmg) {
		if (!immune || !Zepr.devMode) {
			// If powerUpImmunity is activated
			health -= dmg;
		}
	}

	@Override
	public void defineBody() {
		BodyDef bDef = new BodyDef();
		bDef.type = BodyDef.BodyType.DynamicBody;
		if (initialPos != null) {
			bDef.position.set(initialPos);
		} else {
			bDef.position.set(Vector2.Zero);
		}
		bDef.angle = (float) Math.toRadians(initialRot);
		
		FixtureDef fBodyDef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(this.bRadius);
		fBodyDef.shape = shape;
		
		b2body = world.createBody(bDef);
		b2body.createFixture(fBodyDef).setUserData(FixtureType.PLAYER);
		
		b2body.setUserData(this);
		b2body.setSleepingAllowed(false);
		shape.dispose();
		
	}

}
