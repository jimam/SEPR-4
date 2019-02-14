package com.geeselightning.zepr.world;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.geeselightning.zepr.entities.Player;
import com.geeselightning.zepr.entities.PowerUp;
import com.geeselightning.zepr.entities.Zombie;

/**
 * Creates events when contact between bodies in a {@link World} occurs.
 * @author Xzytl
 * Changes:
 * 	implemented - provides an easy and convenient way to detect collisions
 */
public class WorldContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		Fixture fA = contact.getFixtureA();
		Fixture fB = contact.getFixtureB();
		
		if (fA.getUserData() == null || fB.getUserData() == null) return;
		
		FixtureType fAType = (FixtureType) fA.getUserData();
		FixtureType fBType = (FixtureType) fB.getUserData();
		
		switch(fAType) {
		case ZOMBIE:
			zombieContactBegun(fA, fBType, fB);
			break;
		case PLAYER:
			playerContactBegun(fA, fBType, fB);
			break;
		case POWERUP:
			powerUpContactBegun(fA, fBType, fB);
			break;
		case MELEE_SENSOR:
			meleeSensorContactBegun(fA, fBType, fB);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Called when an zombie comes into contact with a second fixture.
	 * @param fA		the enemy body fixture
	 * @param fBType	the type of the second fixture
	 * @param fB		the second fixture
	 */
	public void zombieContactBegun(Fixture fA, FixtureType fBType, Fixture fB) {
		Zombie zombie = (Zombie) fA.getBody().getUserData();
		switch(fBType) {
		case PLAYER:
			zombie.beginContact();
			break;
		case MELEE_SENSOR:
			((Player)fB.getBody().getUserData()).onMeleeRangeEntered(zombie);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Called when a player comes into contact with a second fixture.
	 * @param fA		the player body fixture
	 * @param fBType	the type of the second fixture
	 * @param fB		the second fixture
	 */
	public void playerContactBegun(Fixture fA, FixtureType fBType, Fixture fB) {
		Player player = (Player) fA.getBody().getUserData();
		switch(fBType) {
		case ZOMBIE:
			((Zombie)fB.getBody().getUserData()).beginContact();
			break;
		case POWERUP:
			player.onPickup((PowerUp)fB.getBody().getUserData());
			((PowerUp)fB.getBody().getUserData()).setAlive(false);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Called when a power-up comes into contact with a second fixture.
	 * @param fA		the power-up body fixture
	 * @param fBType	the type of the second fixture
	 * @param fB		the second fixture
	 */
	public void powerUpContactBegun(Fixture fA, FixtureType fBType, Fixture fB) {
		PowerUp powerUp = (PowerUp) fA.getBody().getUserData();
		switch(fBType) {
		case PLAYER:
			((Player)fB.getBody().getUserData()).onPickup(powerUp);
			powerUp.setAlive(false);
		default:
			break;
		}
	}
	
	/**
	 * Called when the player's melee sensor comes into contact with a second fixture.
	 * @param fA		the power-up body fixture
	 * @param fBType	the type of the second fixture
	 * @param fB		the second fixture
	 */
	public void meleeSensorContactBegun(Fixture fA, FixtureType fBType, Fixture fB) {
		switch(fBType) {
		case ZOMBIE:
			((Player)fA.getBody().getUserData()).onMeleeRangeEntered((Zombie)fB.getBody().getUserData());
		default:
			break;
		}
	}

	@Override
	public void endContact(Contact contact) {
		Fixture fA = contact.getFixtureA();
		Fixture fB = contact.getFixtureB();
		
		if (fA.getUserData() == null || fB.getUserData() == null) return;
		
		FixtureType fAType = (FixtureType) fA.getUserData();
		FixtureType fBType = (FixtureType) fB.getUserData();
		
		switch(fAType) {
		case ZOMBIE:
			zombieContactEnded(fA, fBType, fB);
			break;
		case PLAYER:
			playerContactEnded(fA, fBType, fB);
			break;
		case MELEE_SENSOR:
			meleeSensorContactEnded(fA, fBType, fB);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Called when a zombie leaves contact with a second fixture.
	 * @param fA		the power-up body fixture
	 * @param fBType	the type of the second fixture
	 * @param fB		the second fixture
	 */
	public void zombieContactEnded(Fixture fA, FixtureType fBType, Fixture fB) {
		Zombie zombie = (Zombie) fA.getBody().getUserData();
		switch(fBType) {
		case PLAYER:
			zombie.endContact();
			break;
		case MELEE_SENSOR:
			((Player)fB.getBody().getUserData()).onMeleeRangeLeft(zombie);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Called when a player leaves contact with a second fixture.
	 * @param fA		the power-up body fixture
	 * @param fBType	the type of the second fixture
	 * @param fB		the second fixture
	 */
	public void playerContactEnded(Fixture fA, FixtureType fBType, Fixture fB) {
		switch(fBType) {
		case ZOMBIE:
			((Zombie)fB.getBody().getUserData()).endContact();
			break;
		default:
			break;
		}
	}
	
	/**
	 * Called when the player's melee sensor leaves contact with a second fixture.
	 * @param fA		the power-up body fixture
	 * @param fBType	the type of the second fixture
	 * @param fB		the second fixture
	 */
	public void meleeSensorContactEnded(Fixture fA, FixtureType fBType, Fixture fB) {
		switch(fBType) {
		case ZOMBIE:
			((Player)fA.getBody().getUserData()).onMeleeRangeLeft((Zombie)fB.getBody().getUserData());
		default:
			break;
		}
	}

	/**
	 * Unused methods required by interface.
	 */
	
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

}
