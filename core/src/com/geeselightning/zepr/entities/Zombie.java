package com.geeselightning.zepr.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.geeselightning.zepr.game.Zepr;
import com.geeselightning.zepr.util.Constant;
import com.geeselightning.zepr.world.FixtureType;
import com.geeselightning.zepr.world.WorldContactListener;

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
	private final float hitCooldown = Constant.ZOMBIEHITCOOLDOWN;
	private float healthMulti = 1f;
	private float speedMulti = 1f;
	private float damageMulti = 1f;
	
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
		default:
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
		
		//b2body.applyLinearImpulse(getDirNormVector(gameManager.getPlayer().getPos().scl(this.speed)), b2body.getWorldCenter(), true);
		
		Vector2 playerLoc = gameManager.getPlayer().getPos();
		double angle = (Math.atan2(playerLoc.y - b2body.getPosition().y, playerLoc.x - b2body.getPosition().x)) - 90;
		
		//b2body.applyLinearImpulse(new Vector2((float)Math.cos(angle) * - speed, (float)Math.sin(angle) * - speed), b2body.getPosition(), true);
		
		this.setAngle(Math.toDegrees(angle));
		
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
		Vector2 playerPos = gameManager.getPlayer().getPos();
		Vector2 impulse = getDirNormVector(playerPos);
		b2body.applyLinearImpulse(5 * b2body.getMass(), 5 * b2body.getMass(), getX(), getY(), true);
		System.out.println("Take damage called");
		if (health - damage >= 0) {
    		health -= damage;
    	} else {
    		health = 0;
    		this.alive = false;
    	}
	}
}
