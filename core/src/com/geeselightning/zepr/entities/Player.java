package com.geeselightning.zepr.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.geeselightning.zepr.game.GameManager;
import com.geeselightning.zepr.game.Zepr;
import com.geeselightning.zepr.levels.Level;
import com.geeselightning.zepr.util.Constant;

public class Player extends Character {
	
	public enum Type {
		
		NERDY(150, 20, 50, 0.2f, 120f),
		SPORTY(100, 20, 50, 0.2f, 180f),
		HEAVY(100, 30, 50, 0.2f, 120f);
		
		int health;
		int attackDamage;
		int hitRange;
		float hitCooldown;
		float speed;
		
		Type(int health, int attackDamage, int hitRange, float hitCooldown, float speed) {
			this.health = health;
			this.attackDamage = attackDamage;
			this.hitRange = hitRange;
			this.hitCooldown = hitCooldown;
			this.speed = speed;
		}
		
		public int getHealth() {
			return health;
		}
		
		public int getAttackDamage() {
			return attackDamage;
		}
		
		public int getHitRange() {
			return hitRange;
		}
		
		public float getHitCooldown() {
			return hitCooldown;
		}
		
		public float getSpeed() {
			return speed;
		}
	}

	// private static final Player instance = new Player(new Sprite(new Texture("player01.png")), new Vector2(0, 0));
	int attackDamage = Constant.PLAYERDMG;
	int hitRange = Constant.PLAYERRANGE;
	float hitCooldown = Constant.PLAYERHITCOOLDOWN;
	Texture mainTexture;
	Texture attackTexture;
	
	private boolean attacking;
	private boolean immune;
	
	float HPMult;
	float dmgMult;
	float speedMult;
	String playertype;

	public Player(Zepr parent, Sprite sprite, float bRadius, Vector2 initialPos, float initialRot) {
		super(parent, sprite, bRadius, initialPos, initialRot);
	}

	public void setType(String playertype) {
		this.playertype = playertype;
	}

	public String getType() {
		return playertype;
	}
	
	/* Accessor methods */
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
	
	
	/* Convenience methods for setting velocity */
	public void setLinearVelocity(float vX, float vY) {
		this.b2body.setLinearVelocity(vX, vY);
	}
	
	public void setLinearVelocityX(float vX) {
		setLinearVelocity(vX, this.b2body.getLinearVelocity().y);
	}
	
	public void setLinearVelocityY(float vY) {
		setLinearVelocity(this.b2body.getLinearVelocity().x, vY);
	}
	

	public void attack(Zombie zombie, float delta) {
		if (canHitGlobal(zombie, hitRange) && hitRefresh > hitCooldown) {
			zombie.takeDamage(attackDamage);
			hitRefresh = 0;
		} else {
			hitRefresh += delta;
		}
	}

	public void respawn() {
		if (playertype == "nerdy") {
			dmgMult = Constant.NERDYDMGMULT;
			HPMult = Constant.NERDYHPMULT;
			speedMult = Constant.NERDYSPEEDMULT;
			// Nerdy Texture
			mainTexture = new Texture("player01.png");
			attackTexture = new Texture("player01_attack.png");
		} else if (playertype == "sporty") {
			dmgMult = Constant.SPORTYDMGMULT;
			HPMult = Constant.SPORTYHPMULT;
			speedMult = Constant.SPORTYSPEEDMULT;
			// Sporty Texture
			mainTexture = new Texture("player02.png");
			attackTexture = new Texture("player02_attack.png");
		} else if (playertype == "heavy") {
			dmgMult = Constant.HEAVYDMGMULT;
			HPMult = Constant.HEAVYHPMULT;
			speedMult = Constant.HEAVYSPEEDMULT;
			// Test Texture
			mainTexture = new Texture("player03.png");
			attackTexture = new Texture("player03_attack.png");
		} else if (playertype == null) {
			dmgMult = 1;
			HPMult = 1;
			speedMult = 1;
			// Default Nerdy playertype
			mainTexture = new Texture("player01.png");
			attackTexture = new Texture("player01_attack.png");
		}
		this.attackDamage = (int) (Constant.PLAYERDMG * dmgMult);
		this.speed = (int) (Constant.PLAYERSPEED * speedMult);
		this.health = (int) (HPMult * Constant.PLAYERMAXHP);
		this.sprite.setTexture(mainTexture);
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
		bDef.position.set(initialPos);
		bDef.angle = (float) Math.toRadians(initialRot);
	}

}
