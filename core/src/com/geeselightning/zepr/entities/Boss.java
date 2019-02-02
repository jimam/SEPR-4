package com.geeselightning.zepr.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.geeselightning.zepr.Constant;
import com.geeselightning.zepr.levels.Level;

public class Boss extends Character{
	
	private Player player = Player.getInstance();
	int attackDamage = Constant.BOSSDMG;
	public int hitRange = Constant.BOSSRANGE;
	public final float hitCooldown = Constant.BOSSHITCOOLDOWN;
	float HPMult;
	float speedMult;
	float dmgMult;
	Texture mainTexture;

	public Boss(Sprite sprite, Vector2 bossSpawn, Level currentLevel, Type type) {
		super(sprite, bossSpawn, currentLevel);
		switch (type) {
		case SMALLBOSS :
			HPMult = Constant.SMALLBOSSHPMULT;
			speedMult = Constant.SMALLBOSSSPEEDMULT;
			dmgMult = Constant.SMALLBOSSDMGMULT;
			mainTexture = new Texture("smallboss.png");
			break;
		case BIGBOSS :
			HPMult = Constant.BIGBOSSHPMULT;
			speedMult = Constant.BIGBOSSSPEEDMULT;
			dmgMult = Constant.BIGBOSSDMGMULT;
			mainTexture = new Texture("zombie01.png");
			break;
		}
		this.setTexture(mainTexture);
		this.speed = (int) (Constant.BOSSSPEED * speedMult);
		this.health = (int) (Constant.BOSSMAXHP * HPMult);
		this.attackDamage = (int) (Constant.BOSSDMG * dmgMult);
	}
	
	public enum Type {
		SMALLBOSS, BIGBOSS
	}
	
	public void attack(Player player, float delta) {
		if (canHitGlobal(player, hitRange) && hitRefresh > hitCooldown) {
			player.takeDamage(attackDamage);
			hitRefresh = 0;
		} else {
			hitRefresh += delta;
		}
	}

	@Override
	public void update(float delta) {
		// move according to velocity
		super.update(delta);

		// update velocity to move towards player
		// Vector2.scl scales the vector
		velocity = getDirNormVector(player.getCenter()).scl(speed);

		// update direction to face the player
		direction = getDirectionTo(player.getCenter());

		//if (health <= 0) {
			//currentLevel.zombiesRemaining--;
			//currentLevel.aliveZombies.remove(this);
			//this.getTexture().dispose();
		//}
	}
}

