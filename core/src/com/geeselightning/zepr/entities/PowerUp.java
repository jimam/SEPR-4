package com.geeselightning.zepr.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.geeselightning.zepr.game.Zepr;
import com.geeselightning.zepr.world.FixtureType;

public class PowerUp extends Entity {
	
	public enum Type {
		HEAL,
		IMMUNITY,
		RAPID_FIRE,
		SPEED,
		STRENGTH
	}
	
	private final Type type;

	public PowerUp(Zepr parent, Sprite sprite, float bRadius, Vector2 initialPos, float initialRot, Type type) {
		super(parent, sprite, bRadius, initialPos, initialRot);
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
	
	@Override
	public void defineBody() {
		BodyDef bDef = new BodyDef();
		bDef.type = BodyDef.BodyType.StaticBody;
		bDef.position.set(initialPos);
		
		CircleShape shape = new CircleShape();
		shape.setRadius(this.bRadius);
		
		FixtureDef fDef = new FixtureDef();
		fDef.shape = shape;
		
		b2body = world.createBody(bDef);
		b2body.createFixture(fDef).setUserData(FixtureType.POWERUP);
		b2body.setUserData(this);
		
		shape.dispose();
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

	
	
	
//    Player player;
//    public int type;
//    Level currentLevel;
//    public boolean active;

//    public PowerUp(int type, Texture texture, Level currentLevel) {
//        super(new Sprite(texture));
//        this.type = type;
//        this.currentLevel = currentLevel;
//        if (currentLevel != null) {
//            // Tests pass a null currentLevel
//            setPosition(currentLevel.powerSpawn.x, currentLevel.powerSpawn.y);
//        }
//    }
//
//    public void activate(){
//        active = true;
//    }
//
//    public void deactivate(){
//        active = false;
//        if (currentLevel != null) {
//            // Tests pass a null currentLevel
//            currentLevel.currentPowerUp = null;
//        }
//    }
//
//    public boolean overlapsPlayer(){
//        Rectangle rectanglePlayer = player.getBoundingRectangle();
//        Rectangle rectanglePower = this.getBoundingRectangle();
//        return rectanglePlayer.overlaps(rectanglePower);
//    }
//
//    public void update(float delta) {}

}
