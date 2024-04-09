package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.classes.PlayerShip;
import com.mygdx.game.screens.GameOverScreen;
import com.mygdx.game.screens.InGameScreen;
import com.mygdx.game.screens.MainMenuScreen;

/**
 * Clase principal del juego donde se inicializan todas las pantallas
 */
public class MainGame extends Game {

    private SpriteBatch batch;
    public InGameScreen inGame;
    public GameOverScreen gameOver;
    public MainMenuScreen mainMenu;
    private PlayerShip playerShip;

    @Override
    public void create() {
        batch = new SpriteBatch();
        playerShip = new PlayerShip();
        inGame = new InGameScreen(this);
        mainMenu = new MainMenuScreen(this);
        gameOver = new GameOverScreen(this);

        setScreen(mainMenu);
    }

    public SpriteBatch getBatch() {
        return batch;
    }


    public PlayerShip getPlayerShip() {
        return playerShip;
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}