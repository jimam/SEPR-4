package com.geeselightning.zepr.entities;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.geeselightning.zepr.game.Zepr;
import com.geeselightning.zepr.pathfinding.TileNode;
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
	private float healthMulti;
	private float speedMulti;
	private float damageMulti;
	
	/**
	 * Whether or not the zombie is in range of the player - used to determine whether the zombie.
	 * should attack the player.
	 */
	private boolean inMeleeRange;
	
	/**
	 * The amount of time the zombie has been stunned as a result of being hit by the player.
	 */
	private float stunTimer;
	
	private DefaultGraphPath<TileNode> path;
	private int pathStep;
	/**
	 * The amount of time since the zombie last requested a path (avoiding requesting A* run every
	 * 1/30th 1/60th of a second per zombie).
	 */
	private float pathTimer = 3f;
	
	
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
		fBodyDef.density = 20;
		
		b2body = world.createBody(bDef);
		b2body.createFixture(fBodyDef).setUserData(FixtureType.ZOMBIE);
		
		b2body.setUserData(this);
		shape.dispose();
		
		b2body.setLinearDamping(5f);
		b2body.setAngularDamping(5f);
	}

	/**
	 * The original attack function checked for the distance to the player each update cycle and,
	 * if the cooldown for attacking was finished and the player was within range, would apply 
	 * damage. Using box2d, an event will be generated whenever the zombie collides with a player, 
	 * making the job easier. The zombie now attacks when in enters melee range of the player.
	 */
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		if (stunTimer > 0) {
			stunTimer -= delta;
			return;
		}
		
		Player player = gameManager.getPlayer();
		
		Vector2 targetVector;
		
		// If distant from player, use A* to track them down. If close, go straight for them.
		if (distanceFrom(player) > 5) {
			if (path == null) {
				System.out.println("Path is null!");
			}
			if (pathTimer >= 1 || pathStep + 1 == path.getCount()) {
				System.out.println(this.toString() + " requesting path");
				path = gameManager.findPath(getPos(), player.getPos());
				pathStep = 0;
				pathTimer = 0;
			}
			
			if (path == null) return;
			
			if (gameManager.getTileCoordinates(getPos()).equals(path.get(pathStep).getPosition())) {
				pathStep += 1;
			}
			
			targetVector = new Vector2(path.get(pathStep).getX() - this.getX(), path.get(pathStep).getY() - this.getY());
			
		} else {
			targetVector = getVectorTo(player);
			
		}
			
		double angle = Math.toDegrees(Math.atan2(targetVector.y, targetVector.x)) - 90;
			
		b2body.applyLinearImpulse(targetVector.nor(), getPos(), true);	
			
		this.setAngle(angle);
		
		pathTimer += delta;

		if (inMeleeRange && hitRefresh > hitCooldown) {
			gameManager.getPlayer().takeDamage(this.attackDamage);
			hitRefresh = 0;
		} else {
			hitRefresh += delta;
		}
	}
	
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
		Player player = gameManager.getPlayer();
		Vector2 impulse = getVectorTo(player);

		b2body.applyLinearImpulse(impulse.scl(-10f), getPos(), true);
		
		stunTimer = 0.5f;
		
		if (health - damage >= 0) {
    		health -= damage;
    	} else {
    		health = 0;
    		this.alive = false;
    	}
	}
}
