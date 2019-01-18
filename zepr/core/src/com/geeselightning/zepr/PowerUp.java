package com.geeselightning.zepr;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class PowerUp extends Sprite {

    Sprite sprite;
    Player player = Player.getInstance();
    private int type;
    private Texture texture;
    boolean isActive;
    float elapsedTime = 0f;

    PowerUp(int type, Texture texture) {
        this.type = type;
        this.texture = texture;
        sprite = new Sprite(texture);
    }

    public void activate(){}

    public void deactivate(){}

    public Vector2 getCenter() {
        return new Vector2(getX() + (getHeight() / 2), getY() + (getWidth() / 2));
    }

    public boolean overlapsPlayer(){
        Rectangle rectanglePlayer = player.getBoundingRectangle();
        Rectangle rectanglePower = this.sprite.getBoundingRectangle();
        return rectanglePlayer.overlaps(rectanglePower);
    }

}
