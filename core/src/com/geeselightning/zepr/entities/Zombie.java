package com.geeselightning.zepr.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.geeselightning.zepr.Constant;
import com.geeselightning.zepr.levels.Level;

public class Zombie extends Character {

    private Player player = Player.getInstance();
    int attackDamage = Constant.ZOMBIEDMG;
    public int hitRange = Constant.ZOMBIERANGE;
    public final float hitCooldown = Constant.ZOMBIEHITCOOLDOWN;
    float HPMult;
    float speedMult;
    float dmgMult;
    String zombieType;
    Texture mainTexture;

    public Zombie(Sprite sprite, Vector2 zombieSpawn, Level currentLevel) {
        super(sprite, zombieSpawn, currentLevel);
      
    }
    
    public void spawnZombies() {
    	if (zombieType == "slow") {
        	HPMult = Constant.SLOWHPMULT;
        	speedMult = Constant.SLOWSPEEDMULT;
        	dmgMult = Constant.SLOWDMGMULT;
        	mainTexture = new Texture("zombie01.png");
        }
        else if (zombieType == "medium") {
        	HPMult = Constant.MEDHPMULT;
        	speedMult = Constant.MEDSPEEDMULT;
        	dmgMult = Constant.MEDDMGMULT;
        	mainTexture = new Texture("zombie02.png");
        }
        else if (zombieType == "fast"){
        	HPMult = Constant.FASTHPMULT;
        	speedMult = Constant.FASTSPEEDMULT;
        	dmgMult = Constant.FASTDMGMULT;
        	mainTexture = new Texture("zombie03.png");
        }
        this.speed = (int)(Constant.ZOMBIESPEED * speedMult);
        this.health = (int)(Constant.ZOMBIEMAXHP * HPMult);
        this.attackDamage = (int)(Constant.ZOMBIEDMG * dmgMult);
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
        //move according to velocity
        super.update(delta);

        // update velocity to move towards player
        // Vector2.scl scales the vector
        velocity = getDirNormVector(player.getCenter()).scl(speed);

        // update direction to face the player
        direction = getDirectionTo(player.getCenter());

        if (health <= 0) {
            currentLevel.zombiesRemaining--;
            currentLevel.aliveZombies.remove(this);
            this.getTexture().dispose();
        }
    }
}
