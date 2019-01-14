package com.geeselightning.zepr;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Player extends Character {

    private static final Player instance = new Player(new Sprite(new Texture("core/assets/player01.png")), new Vector2(0, 0));

    private Player(Sprite sprite, Vector2 playerSpawn) {
        super(sprite, playerSpawn, null);
    }

    public static Player getInstance(){
        return instance;
    }

    float HPMult;
    float attackMult;

    public void respawn(Vector2 playerSpawn, Level level, String playertype){
        setX(playerSpawn.x);
        setY(playerSpawn.y);
        if (playertype == "nerdy"){
            attackMult = Constant.NERDYATTACKMULT;
            HPMult = Constant.NERDYATTACKMULT;
        }
        else if (playertype == "sporty"){
            attackMult = Constant.SPORTYATTACKMULT;
            HPMult = Constant.SPORTYATTACKMULT;
        }
        else if(playertype == null){
            attackMult =1;
            HPMult = 1;
        }
        this.speed = Constant.PLAYERSPEED;
        this.health = (int)(HPMult * Constant.PLAYERMAXHP);
        this.currentLevel = level;
        this.setTexture(new Texture("core/assets/player01.png"));
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        // update the direction the player is facing
        direction = getDirection(currentLevel.getMouseWorldCoordinates());

        if (health <= 0) {
            // GAME OVER
        }
    }
}
