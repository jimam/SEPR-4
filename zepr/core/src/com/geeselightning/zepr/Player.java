package com.geeselightning.zepr;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

public class Player extends Character {

    private static final Player instance = new Player(new Sprite(new Texture("core/assets/player01.png")), new Vector2(0, 0));
    int attackDamage = Constant.PLAYERDMG;
    int hitRange = 30;
    final float hitCooldown = (float) 0.2;
    float HPMult;
    float attackMult;
    String playertype;


    private Player(Sprite sprite, Vector2 playerSpawn) {
        super(sprite, playerSpawn, null);
    }

    public static Player getInstance(){
        return instance;
    }

    public void setType(String playertype){
        this.playertype = playertype;
    }

    public void respawn(Vector2 playerSpawn, Level level){
        setX(playerSpawn.x);
        setY(playerSpawn.y);
        if (playertype == "nerdy"){
            attackMult = Constant.NERDYATTACKMULT;
            HPMult = Constant.NERDYHPMULT;
        }
        else if (playertype == "sporty"){
            attackMult = Constant.SPORTYATTACKMULT;
            HPMult = Constant.SPORTYHPMULT;
        }
        else if(playertype == null){
            attackMult = 0.1f;
            HPMult = 0.1f;
        }
        this.attackDamage = (int)(Constant.PLAYERDMG * attackMult);
        this.speed = Constant.PLAYERSPEED;
        this.health = (int)(HPMult * Constant.PLAYERMAXHP);
        this.currentLevel = level;
        this.setTexture(new Texture("core/assets/player01.png"));
    }

    public boolean canHit(Character character){
        return canHitGlobal(character, hitRange);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        // update the direction the player is facing
        direction = getDirection(currentLevel.getMouseWorldCoordinates());

        if (health <= 0) {
            currentLevel.gameOver();
        }
    }
}
