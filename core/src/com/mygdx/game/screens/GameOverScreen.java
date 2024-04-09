package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.MainGame;
import com.mygdx.game.classes.PlayerShip;

/**
 * La pantalla de Game Over
 */
public class GameOverScreen extends AbstractScreen implements InputProcessor, Runnable {

    SpriteBatch batch;
    int SCREEN_WIDTH,
            SCREEN_HEIGHT;
    PlayerShip playerShip;
    BitmapFont gameOver,
            score;
    Thread thread;
    boolean canSkip;

    public GameOverScreen(MainGame game) {
        super(game);

        batch = game.getBatch();
        playerShip = game.getPlayerShip();
    }

    @Override
    public void show() {
        super.show();
        SCREEN_WIDTH = Gdx.graphics.getWidth();
        SCREEN_HEIGHT = Gdx.graphics.getHeight();

        gameOver = new BitmapFont();
        gameOver.getData().setScale(20);
        gameOver.setColor(Color.RED);

        score = new BitmapFont();
        score.getData().setScale(8);
        score.setColor(Color.YELLOW);

        Gdx.input.setInputProcessor(this);

        thread = new Thread(this);
        canSkip = false;
        thread.start();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        batch.begin();

        gameOver.draw(batch, "GAME\nOVER", 150, SCREEN_HEIGHT/4f*3);
        score.draw(batch, "Puntuación: "+playerShip.getScore(),150,SCREEN_HEIGHT/2f-100);

        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {

        if(keycode == Input.Keys.DPAD_UP){

            if(canSkip){

                changeScreen();
            }
        }
        return false;
    }

    /**
     * Reinicializa los atributos del jugador y vuelve a la pantalla principal
     */
    private void changeScreen() {

        game.getPlayerShip().setShield(100);
        game.getPlayerShip().setScore(0);
        game.getPlayerShip().setLives(3);
        Gdx.input.setInputProcessor(game.mainMenu);
        game.setScreen(game.mainMenu);
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if(canSkip){
            changeScreen();
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    /**
     * Espera 2 segundos para permitir que el jugador cambie de pantalla
     * y así no hacerlo por error en cuanto aparece el Game Over
     */
    @Override
    public void run() {

        try {
            Thread.sleep(2000);

        } catch (InterruptedException e) {

            System.out.println(e);

        }
        canSkip = true;
    }
}
