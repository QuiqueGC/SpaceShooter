package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.MainGame;

/**
 * La pantalla de presentación del juego
 */
public class MainMenuScreen extends AbstractScreen implements InputProcessor {


    boolean changeScreen;
    SpriteBatch batch;

    int SCREEN_WIDTH,
            SCREEN_HEIGHT;

    BitmapFont title,
            explanation;
    String explanationTxt;
    Sprite alienSprite;
    Texture alienTexture;

    public MainMenuScreen(MainGame game) {
        super(game);
        batch = game.getBatch();


        SCREEN_WIDTH = Gdx.graphics.getWidth();
        SCREEN_HEIGHT = Gdx.graphics.getHeight();

        changeScreen = false;
        Gdx.input.setInputProcessor(this);

        explanationTxt = "Toca la pantalla o pulsa\nla tecla de flecha hacia\narriba para empezar.";
    }


    @Override
    public void show() {
        super.show();

        paintImage();

        bitMapFontInit();

    }


    /**
     * Inicializa y posiciona la imagen de la portada
     */
    private void paintImage() {
        alienTexture = new Texture("alien.png");
        alienSprite = new Sprite(alienTexture);
        alienSprite.scale(4);
        alienSprite.setX(SCREEN_WIDTH/2f - alienSprite.getWidth()/2);
        alienSprite.setY(SCREEN_HEIGHT/3f * 2 - alienSprite.getHeight()/2);
    }

    /**
     * Inicializa los dos BitMapsFont
     */
    private void bitMapFontInit() {
        title = new BitmapFont();
        title.getData().setScale(10);
        title.setColor(Color.YELLOW);


        explanation = new BitmapFont();
        explanation.getData().setScale(6);
        explanation.setColor(Color.WHITE);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1);

        batch.begin();
        title.draw(batch, "Space Shooter",50, SCREEN_HEIGHT/2f - title.getCapHeight()/2);
        explanation.draw(batch, explanationTxt, 50, SCREEN_HEIGHT/3f - explanation.getCapHeight()/2);

        alienSprite.draw(batch);

        batch.end();

    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        title.dispose();
        explanation.dispose();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public boolean keyDown(int keycode) {

        if(keycode == Input.Keys.DPAD_UP){

            changeScreen();

        }
        return false;
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

        changeScreen();

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
     * Cambia a la siguiente pantalla
     */
    private void changeScreen() {
        Gdx.input.setInputProcessor(game.inGame);
        game.setScreen(game.inGame);
    }

}
