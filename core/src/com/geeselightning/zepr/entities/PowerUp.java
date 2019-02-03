package com.geeselightning.zepr.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.geeselightning.zepr.Constant;
import com.geeselightning.zepr.levels.Level;

public class PowerUp extends Sprite {

    Player player = Player.getInstance();
    
    Level currentLevel;
    public boolean active;
    public float timeRemaining;
    public Type type;
    public Texture texture;
    public PowerUp(Type type, Texture texture, Level currentLevel) {
    	
    	super(new Sprite(texture));
        this.type = type;
    	switch (this.type) {
    	case HEAL: this.texture = new Texture("heal.png");
			break;
    	case IMMUNE: this.texture = new Texture("immunity.png");
			break;
    	case SPEED: this.texture = new Texture("speed.png");
			break;
    	case ATTACK: this.texture = new Texture("strength.png");
			break;
    	case RAPIDFIRE: this.texture = new Texture("rapidfire.png");
    		break;
    	
    	}
    	
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
    	switch (this.type) {
    	case HEAL: 
			break;
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


