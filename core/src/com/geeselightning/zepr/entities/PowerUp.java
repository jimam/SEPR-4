package com.geeselightning.zepr.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.geeselightning.zepr.Constant;
import com.geeselightning.zepr.levels.Level;

/**
 * 
 *	CHANGE IN ASSESSMENT 3: changed powerups to work in one class rather than several.
 *	Now uses Enums instead of integer types, and the effects are applied based on the Enum value.
 */
public class PowerUp extends Sprite {

    Player player = Player.getInstance();
    
    Level currentLevel;
    public boolean active;
    public float timeRemaining;
    public Type type;
    public Texture texture;
    //ASSESSMENT 3: Constructor now takes a type, texture, and the level to be initialised in.
    public PowerUp(Type type, Texture texture, Level currentLevel) {
    	
    	super(new Sprite(texture));
        this.type = type;
        this.currentLevel = currentLevel;
        if (currentLevel != null) {
            // Tests pass a null currentLevel
            setPosition(currentLevel.powerSpawn.x, currentLevel.powerSpawn.y);
        }
    }
    
    public enum Type{
    	HEAL,IMMUNE,SPEED,ATTACK,RAPIDFIRE;
    }

    public void activate(){
        active = true;
        //Applies an effect based on the powerup type.
        switch (this.type) {
        case HEAL: 
    			if (this.player.health + Constant.HEALUP <= (int)(this.player.HPMult * Constant.PLAYERMAXHP)) {
    				this.player.health += Constant.HEALUP;	
    			} else {
    				this.player.health = (int) this.player.HPMult * Constant.PLAYERMAXHP;
    			}
        			break;
        case IMMUNE: this.timeRemaining = Constant.IMMUNITYTIME;
        			this.player.isImmune = true;
        			break;
        case SPEED:  this.timeRemaining = Constant.SPEEDUPTIME;
        			this.player.speed += Constant.SPEEDUP;
        			break;
        case ATTACK: this.timeRemaining = Constant.ATKUPTIME;
        			this.player.attackDamage += 10;
        			break;
        case RAPIDFIRE: this.timeRemaining = Constant.ATKUPTIME;
        			this.player.hitCooldown = 0.08f;
        			break;
        }
        this.getTexture().dispose();
    }

    public void deactivate(){
        active = false;
        //Reverses the effects of powerups.
    	switch (this.type) {
    	case HEAL: break; //HEAL applies its effect all at once and as such does not need to be reverted.
    	case IMMUNE: this.player.isImmune = false;
			break;
    	case SPEED: this.player.speed -= Constant.SPEEDUP;
			break;
    	case ATTACK: this.player.attackDamage -= 10;
			break;
    	case RAPIDFIRE: this.player.hitCooldown = Constant.PLAYERHITCOOLDOWN;
    		break;
    	
    	}
        if (currentLevel != null) {
            // Tests pass a null currentLevel
            currentLevel.currentPowerUp = null;
        }
    }

    public boolean overlapsPlayer(){
        Rectangle rectanglePlayer = player.getBoundingRectangle();
        Rectangle rectanglePower = this.getBoundingRectangle();
        return rectanglePlayer.overlaps(rectanglePower);
    }

    public void update(float delta) {
    	//HEAL is not time based so is deactivated as soon as it has applied its effect.
    	if (this.type == Type.HEAL) {
    		if (active) {
                this.deactivate();
                this.getTexture().dispose();
            }
    	}
    	if (active) {
    		timeRemaining -= delta;
    	}
    	if (timeRemaining < 0) {
    		deactivate();
    	}
    }
}


