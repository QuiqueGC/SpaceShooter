package com.mygdx.game.classes;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Clase que contiene los datos de cada disparo enemigo
 */
public class ShotEnemy implements Runnable{

    Texture texture;
    Sprite sprite;
    Sprite shipOrigin;
    float targetX, targetY, directionX, directionY;
    int speed;

    public ShotEnemy(Sprite shipOrigin, float targetX, float targetY) {

        this.targetX = targetX;
        this.targetY = targetY;
        this.shipOrigin = shipOrigin;
        this.texture = new Texture("tret.png");
        this.sprite = new Sprite(this.texture);
        this.sprite.scale(5);
        sprite.setX(shipOrigin.getX()+shipOrigin.getWidth()/2);
        sprite.setY(shipOrigin.getY());
        this.directionX = targetX - shipOrigin.getX();
        this.directionY = targetY - shipOrigin.getY();
        this.speed = 40;
    }

    public Sprite getSprite() {
        return this.sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public float getDirectionX() {
        return directionX;
    }

    public void setDirectionX(float directionX) {
        this.directionX = directionX;
    }

    public float getDirectionY() {
        return directionY;
    }

    public void setDirectionY(float directionY) {
        this.directionY = directionY;
    }

    /**
     * Mueve el sprite del disparo
     */
    @Override
    public void run() {

        do {

            this.sprite.setY(this.sprite.getY() + directionY/speed);
            this.sprite.setX(this.sprite.getX() + directionX/speed);

            try {

                Thread.sleep(50);

            } catch (InterruptedException e) {

                e.printStackTrace();
            }

        } while (true);
    }
}
