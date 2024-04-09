package com.mygdx.game.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.MainGame;

import java.util.ArrayList;

/**
 * Clase con las estadísiticas y el Sprite de la nave del jugador
 */
public class PlayerShip {

    final int SPEED = 30;
    int lives, shield;
    Texture texture;
    Sprite sprite;

    int score;

    public PlayerShip() {

        this.lives = 3;
        this.shield = 100;
        this.texture = new Texture("playerShip.png");
        this.sprite = new Sprite(texture);
        this.score = 0;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public int getShield() {
        return shield;
    }

    public void setShield(int shield) {
        this.shield = shield;
    }

    public int getSPEED() {
        return SPEED;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }


    /**
     * Reduce el escudo de la nave del jugador por el impacto
     * @param shotEnemy El disparo enemigo que chequeamos
     * @param shotEnemiesToDelete la lista de disparos que habrá que borrar
     * @param game el juego principal (para poder cambiar de pantalla en caso de derrota)
     */
    public void impactShield(ShotEnemy shotEnemy, ArrayList<ShotEnemy> shotEnemiesToDelete, ArrayList<Enemy> enemies ,MainGame game) {

        shotEnemiesToDelete.add(shotEnemy);
        this.setShield(this.getShield() - 1);

        if(this.getShield() <= 0){

            loosingLives(enemies, game);
            this.setShield(100);
        }
    }

    /**
     * Resta la vida al jugador y chequea si es game over
     * @param game el juego principal (para poder cambiar de pantalla en caso de derrota)
     */
    public void loosingLives(ArrayList<Enemy> enemies,MainGame game) {

        this.setLives(this.getLives() - 1);

        if (this.getLives() <= 0) {

            changeScreen(enemies, game);
        }
    }

    /**
     * Cambia a la pantalla de Game Over
     * @param game el juego principal (para poder cambiar de pantalla en caso de derrota)
     */
    private void changeScreen(ArrayList<Enemy> enemies, MainGame game) {
        enemies.clear();
        Gdx.input.setInputProcessor(game.gameOver);
        game.setScreen(game.gameOver);
    }
}
