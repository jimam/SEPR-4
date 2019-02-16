package com.geeselightning.zepr.entities;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.geeselightning.zepr.game.Zepr;
import com.geeselightning.zepr.util.Constant;
import com.geeselightning.zepr.world.BodyFactory;
import com.geeselightning.zepr.world.FixtureType;
import com.geeselightning.zepr.world.WorldContactListener;

public class Player extends Character {

	public enum Type {

		NERDY("player01.png", "player01_attack.png", 1.5f, 1.0f, 1.0f),
		SPORTY("player02.png", "player02_attack.png", 1.0f, 1.0f, 1.5f),
		HEAVY("player03.png", "player03_attack.png", 1.0f, 1.5f, 1.0f);

		String normalTextureName;
		String attackTextureName;
		float healthMultiplier;
		float damageMultiplier;
		float speedMultiplier;

		Type(String normalTextureName, String attackTextureName, float healthMultiplier, float damageMultiplier,
				float speedMultiplier) {
			this.normalTextureName = normalTextureName;
			this.attackTextureName = attackTextureName;
			this.healthMultiplier = healthMultiplier;
			this.damageMultiplier = damageMultiplier;
			this.speedMultiplier = speedMultiplier;
		}
	}

	private Type type;

	private boolean attacking;

	private int attackDamage = Constant.PLAYERDMG;
	private int hitRange = Constant.PLAYERRANGE;
	private float hitCooldown = Constant.PLAYERHITCOOLDOWN;
	private Texture mainTexture;
	private Texture attackTexture;

	/**
	 * Contains the zombies currently in range and in front of the player that will
	 * be damaged when the attack ability is used.
	 */
	private Set<Zombie> zombiesInRange;

	/**
	 * Contains the power-ups currently active on the player. Float represents the
	 * seconds until the effect expires.
	 */
	private ConcurrentHashMap<PowerUp.Type, Float> activePowerUps;

	public Player(Zepr parent, float bRadius, Vector2 initialPos, float initialRot, Type type) {
		super(parent, new Sprite(new Texture(type.normalTextureName)), bRadius, initialPos, initialRot);
		this.type = type;
		this.attackDamage = (int) (Constant.PLAYERDMG * type.damageMultiplier);
		this.speed = (int) (Constant.PLAYERSPEED * type.speedMultiplier);
		this.health = (int) (Constant.PLAYERMAXHP * type.healthMultiplier);

		mainTexture = new Texture(type.normalTextureName);
		attackTexture = new Texture(type.attackTextureName);
		this.sprite.setTexture(mainTexture);

		this.zombiesInRange = new HashSet<>();
		this.activePowerUps = new ConcurrentHashMap<>();
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

	public boolean isPowerUpActive(PowerUp.Type type) {
		return activePowerUps.containsKey(type);
	}

	@Override
	public void update(float delta) {
		super.update(delta);

		if (isPowerUpActive(PowerUp.Type.HEAL)) {
			if (getHealth() + Constant.HEALUP < type.healthMultiplier * Constant.PLAYERMAXHP) {
				this.health += Constant.HEALUP;
			} else {
				this.health = (int) (type.healthMultiplier * Constant.PLAYERMAXHP);
			}
		}

		for (Map.Entry<PowerUp.Type, Float> entry : activePowerUps.entrySet()) {
			if (entry.getValue() > 0) {
				activePowerUps.put(entry.getKey(), entry.getValue() - delta);
			} else {
				activePowerUps.remove(entry.getKey());
			}
		}

		if (attacking && hitRefresh > hitCooldown) {
			this.sprite.setTexture(attackTexture);
			final int damage = isPowerUpActive(PowerUp.Type.STRENGTH) ? attackDamage * 2 : attackDamage;
			zombiesInRange.forEach(z -> z.takeDamage(damage));
			hitRefresh = (isPowerUpActive(PowerUp.Type.RAPID_FIRE)) ? hitCooldown / 2 : 0;
		} else {
			this.sprite.setTexture(mainTexture);
			hitRefresh += delta;
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
	public void takeDamage(int damage) {
		// If dev mode is active or the player has the immunity power-up, don't apply damage.
		if (isPowerUpActive(PowerUp.Type.IMMUNITY) || Zepr.devMode) {
			return;
		} else {
			if (health - damage >= 0) {
				health -= damage;
			} else {
				health = 0;
			}
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
		fBodyDef.density = 10;

		b2body = world.createBody(bDef);
		b2body.createFixture(fBodyDef).setUserData(FixtureType.PLAYER);

		BodyFactory.makeMeleeSensor(b2body, 7, hitRange, 1f);

		b2body.setUserData(this);
		b2body.setSleepingAllowed(false);
		shape.dispose();
	}

	/**
	 * Called by {@link WorldContactListener} when the player touches a power-up.
	 * 
	 * @param powerUp
	 */
	public void onPickup(PowerUp powerUp) {
		activePowerUps.put(powerUp.getType(), powerUp.getDuration());
	}

	/**
	 * Called by {@link WorldContactListener} when a zombie enters the player's
	 * melee range.
	 * 
	 * @param zombie the zombie that has entered range
	 */
	public void onMeleeRangeEntered(Zombie zombie) {
		this.zombiesInRange.add(zombie);
	}

	/**
	 * Called by {@link WorldContactListener} when a zombie leaves the player's
	 * melee range
	 * 
	 * @param zombie the zombie that has left range
	 */
	public void onMeleeRangeLeft(Zombie zombie) {
		this.zombiesInRange.remove(zombie);
	}

}
