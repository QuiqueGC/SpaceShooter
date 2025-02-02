package com.mygdx.game.screens;

import com.badlogic.gdx.Screen;
import com.mygdx.game.MainGame;

/**
 * Clase (con las características generales de cada pantalla)
 * que implementa Screen y que heredarán cada una de las pantallas
 */
public abstract class AbstractScreen implements Screen {

    protected MainGame game;
    public AbstractScreen(MainGame game) {
        this.game = game;
    }
    @Override
    public void show() {}
    @Override
    public void render(float delta) {}
    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
    @Override
    public void dispose() {}
}
