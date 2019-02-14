package com.geeselightning.zepr.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.geeselightning.zepr.game.Zepr;
import com.geeselightning.zepr.util.Constant;
import com.geeselightning.zepr.world.FixtureType;

/**
 * A hostile computer-controlled character that will pursue and attempt to harm the player.
 * @author Xzytl
 * Changes:
 * 	integrated box2d - position now revolves around world location rather than screen position
 */
public class Zombie extends Character {
	
	public enum Type {
		SLOW, MEDIUM, FAST
	}

	protected int attackDamage = Constant.ZOMBIEDMG;
	private int hitRange = Constant.ZOMBIERANGE;
	private final float hitCooldown = Constant.ZOMBIEHITCOOLDOWN;
	private float healthMulti;
	private float speedMulti;
	private float damageMulti;
	
	public boolean inMeleeRange;

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
		b2body.createFixture(fBodyDef).setUserData(FixtureType.ZOMBIE);
		
		b2body.setUserData(this);
		shape.dispose();
		
		b2body.setLinearDamping(5.0f);
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		this.setLinearVelocity(getDirNormVector(gameManager.getPlayer().getPos()).scl(this.speed));
		
		Vector2 playerLoc = gameManager.getPlayer().getPos();
		double angle = Math.toDegrees(Math.atan2(playerLoc.y - b2body.getPosition().y, playerLoc.x - b2body.getPosition().x)) - 90;
		this.setAngle(angle);
		
		if (inMeleeRange && hitRefresh > hitCooldown) {
			gameManager.getPlayer().takeDamage(this.attackDamage);
			hitRefresh = 0;
		} else {
			hitRefresh += delta;
		}
	}
	
	/**
	 * The original attack function checked for the distance to the player each update cycle and,
	 * if the cooldown for attacking was finished, would apply damage. Using box2d, an event will
	 * be generated whenever the zombie collides with a player, making the job easier. The zombie
	 * now attacks when in enters melee range of the player.
	 */
	
	/**
	 * Called by {@link WorldContactListener} when the player is in contact.
	 */
	public void beginContact() {
		this.inMeleeRange = true;
	}
	
	/**
	 * Called by {@link WorldContactListener} when the player leaves contact.
	 */
	public void endContact() {
		this.inMeleeRange = false;
	}
	
	@Override
	public void takeDamage(int damage) {
		if (health - damage >= 0) {
    		health -= damage;
    	} else {
    		health = 0;
    		this.alive = false;
    	}
	}
}
