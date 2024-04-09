package com.mygdx.game.classes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;

/**
 * Clase que contiene a los enemigos y sus sprites
 */
public class Enemy implements Runnable {

    int hp,
            speed,
            timeToShot,
            randomDirectionX,
            randomDirectionY;
    float SCREEN_WIDTH,
            SCREEN_HEIGHT;
    boolean destroyed;
    Texture texture;
    Sprite sprite;
    Sprite playerSprite;
    ShotEnemy shotEnemy;
    ArrayList<ShotEnemy> shotsList;
    Thread thread;


    public Enemy(float SCREEN_WIDTH, float SCREEN_HEIGHT, Sprite playerSprite) {

        this.hp = 10;
        this.speed = (int) (Math.random() * (50 - 5) + 5);
        this.texture = new Texture("alien.png");
        this.sprite = new Sprite(this.texture);
        //this.sprite.scale(2);
        this.SCREEN_WIDTH = SCREEN_WIDTH;
        this.SCREEN_HEIGHT = SCREEN_HEIGHT;
        this.sprite.setX((float)(Math.random()*(SCREEN_WIDTH-this.sprite.getWidth())+this.sprite.getWidth()));
        this.sprite.setY(SCREEN_HEIGHT - sprite.getHeight());
        randomDirectionX = (int) (Math.random() * 2);
        randomDirectionY = (int) (Math.random() * 3);
        this.destroyed = false;
        timeToShot = 0;
        shotsList = new ArrayList<>();
        this.playerSprite = playerSprite;
    }

    public int getHp() {
        return hp;
    }
    public void setHp(int hp) {
        this.hp = hp;
    }
    public Sprite getSprite() {
        return sprite;
    }
    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
    public ArrayList<ShotEnemy> getShotsList() {
        return shotsList;
    }
    public void setShotsList(ArrayList<ShotEnemy> shotsList) {
        this.shotsList = shotsList;
    }


    /**
     * Mueve las naves de los enemigos y las hace disparar
     */
    @Override
    public void run() {

        do {
            yDirectionChoosing(randomDirectionY);

            xDirectionChoosing(randomDirectionX);

            try {

                Thread.sleep(50);

            } catch (InterruptedException e) {

                e.printStackTrace();
            }

            if (timeToShot >= 40){

                shooting();

            }

            timeToShot++;

        } while (!destroyed);
    }

    /**
     * Escoge la dirección aleatoria para el eje Y
     * @param randomDirection Número aleatorio que puede indicar arriba, abajo o ninguna
     */
    private void yDirectionChoosing(int randomDirection) {

        switch (randomDirection) {
            case 0:
                goingDown();
                break;
            case 1:
                goingTop();
                break;
        }
    }

    /**
     * Indica la dirección aleatoria de la nave en el eje X
     * @param randomDirection Número que indicará izquierda o derecha
     */
    private void xDirectionChoosing(int randomDirection) {

        switch (randomDirection) {
            case 0:
                goingLeft();
                break;
            case 1:
                goingRight();
                break;
        }
    }


    /**
     * Inicializa un disparo desde la nave y reinicia el contador para el siguiente
     */
    private void shooting() {

        shotEnemy = new ShotEnemy(this.sprite, playerSprite.getX(), playerSprite.getY());
        shotsList.add(shotEnemy);
        timeToShot = 0;

        thread = new Thread(shotEnemy);
        thread.start();
    }

    /**
     * Mueve la nave hacia la derecha o cambia su dirección en caso de llegar al límite
     * de la pantalla
     */
    private void goingRight() {
        if (this.sprite.getX() + this.sprite.getWidth() + this.speed < SCREEN_WIDTH) {

            this.sprite.setX(this.sprite.getX() + this.speed);

        } else {
            randomDirectionX = 0;
        }
    }

    /**
     * Mueve la nave hacia la izquierda
     * (o cambia su dirección en caso de llegar al límite de la pantalla)
     */
    private void goingLeft() {
        if (this.sprite.getX() - this.speed >= 0) {

            this.sprite.setX(this.sprite.getX() - this.speed);

        } else {
            randomDirectionX = 1;
        }
    }

    /**
     * Mueve la nave hacia arriba (o cambia su dirección si llega al límite de la pantalla)
     */
    private void goingTop() {
        if (this.sprite.getY() + this.sprite.getHeight() + this.speed < SCREEN_HEIGHT) {

            this.sprite.setY(this.sprite.getY() + this.speed);

        } else {
            randomDirectionY = 0;
        }
    }

    /**
     * Mueve la nave hacia abajo (o cambia su dirección si llega al límite de la pantalla)
     */
    private void goingDown() {
        if (this.sprite.getY() - this.speed >= SCREEN_HEIGHT/6) {

            this.sprite.setY(this.sprite.getY() - this.speed);

        } else {
            randomDirectionY = 1;
        }
    }
}