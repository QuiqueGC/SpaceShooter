package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.MainGame;
import com.mygdx.game.classes.Enemy;
import com.mygdx.game.classes.PlayerShip;
import com.mygdx.game.classes.ShotEnemy;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Timer;

/**
 * La pantalla de juego donde ocurre toda la magia
 */
public class InGameScreen extends AbstractScreen implements InputProcessor, Runnable {

    SpriteBatch batch;
    boolean playing;
    int SCREEN_WIDTH,
            SCREEN_HEIGHT,
            MENU_HEIGHT,
            MARGIN;
    int xTouched = -1;
    int yTouched = -1;
    PlayerShip playerShip;
    Enemy enemyToLoadSprite;
    Thread thread;
    ArrayList<Enemy> enemies,
            enemiesToDelete;
    ArrayList<Sprite> playerShots,
            shotsToDelete;
    ArrayList<ShotEnemy> shotEnemiesToDelete;
    Texture textureShot;
    Sprite spriteShot;
    ShapeRenderer shapeRenderer;
    BitmapFont bitmapFont;
    String scoreText,
            shieldText,
            livesText;


    public InGameScreen(MainGame game) {
        super(game);

        playing = false;
        batch = game.getBatch();
        playerShip = game.getPlayerShip();
    }

    @Override
    public void show() {

        playing = true;

        bitmapFont = new BitmapFont();
        scoreText = "Puntuación: ";
        shieldText = "Escudo: ";
        livesText = "Vidas: ";
        bitmapFont.getData().setScale(5);
        bitmapFont.setColor(Color.WHITE);


        SCREEN_WIDTH = Gdx.graphics.getWidth();
        SCREEN_HEIGHT = Gdx.graphics.getHeight();
        MENU_HEIGHT = SCREEN_HEIGHT / 6;
        MARGIN = 50;

        textureShot = new Texture("shot.png");
        spriteShot = new Sprite(textureShot);
        playerShots = new ArrayList<>();


        shotEnemiesToDelete = new ArrayList<>();
        enemiesToDelete = new ArrayList<>();
        shotsToDelete = new ArrayList<>();


        shapeRenderer = new ShapeRenderer();


        playerShip.getSprite().setX(SCREEN_WIDTH / 2f - playerShip.getSprite().getWidth() / 2);
        playerShip.getSprite().setY(MENU_HEIGHT + playerShip.getSprite().getHeight());


        enemies = new ArrayList<>();
        //si no inicializo aquí un "enemy", no me los genera en el método run
        //no sé por qué
        enemyToLoadSprite = new Enemy(SCREEN_WIDTH, SCREEN_HEIGHT, playerShip.getSprite());


        thread = new Thread(this);
        thread.start();
        Gdx.input.setInputProcessor(this);

    }


    @Override
    public void render(float delta) {

        ScreenUtils.clear(0, 0, 0, 1);

        System.out.println(playerShip.getShield());

        drawingMenu();

        batch.begin();


        try{
            drawingEnemiesAndTheirShots();

        }catch(ConcurrentModificationException e){

            System.out.println(e);
        }


        playerShip.getSprite().draw(batch);

        drawingAndMovingPlayerShots();

        updatingArrays();

        writtingTexts();

        batch.end();

    }
    /**
     * Dibuja el fondo del panel información
     */
    private void drawingMenu() {

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(0, 0, SCREEN_WIDTH, MENU_HEIGHT);
        shapeRenderer.end();
    }

    /**
     * Dibuja los enemigos y sus disparos
     */
    private void drawingEnemiesAndTheirShots() {

        for (Enemy enemy :
                enemies) {

            enemy.getSprite().draw(batch);

            drawingAndCheckingShotsOfEnemy(enemy);

            enemy.getShotsList().removeAll(shotEnemiesToDelete);
            shotEnemiesToDelete.clear();

            if (enemy.getSprite().getBoundingRectangle().overlaps(playerShip.getSprite().getBoundingRectangle())) {

                enemiesToDelete.add(enemy);

                playerShip.loosingLives(enemies, game);
            }
        }
    }

    /**
     * Dibuja los disparos de los enemigos y chequea sus colisiones
     */
    private void drawingAndCheckingShotsOfEnemy(Enemy enemy) {

        try {
            for (ShotEnemy shotEnemy :
                    enemy.getShotsList()) {

                //Si no hago esto, los sprites se mezclan con los de las naves
                shotEnemy.getSprite().setTexture(textureShot);
                shotEnemy.getSprite().draw(batch);

                detectingShotEnemyCollisions(shotEnemy);

            }
        } catch (ConcurrentModificationException e) {
            System.out.println(e);
            System.out.println("ERROR");
        }
    }

    /**
     * Detecta las colisiones del disparo (o si sale fuera del tablero)
     * @param shotEnemy El disparo que se chequeará
     */
    private void detectingShotEnemyCollisions(ShotEnemy shotEnemy) {

        if (shotEnemy.getSprite().getBoundingRectangle().overlaps(playerShip.getSprite().getBoundingRectangle())) {

            playerShip.impactShield(shotEnemy, shotEnemiesToDelete, enemies, game);

        } else if (shotEnemy.getSprite().getY() <= MENU_HEIGHT) {

            shotEnemiesToDelete.add(shotEnemy);
        }
    }



    /**
     * Dibuja y mueve los disparos del jugador
     */
    private void drawingAndMovingPlayerShots() {

        for (Sprite playerShot :
                playerShots) {

            playerShot.draw(batch);
            playerShot.setY(playerShot.getY() + 800 * Gdx.graphics.getDeltaTime());

            checkingNewPosition(playerShot);

        }
    }

    /**
     * Chequea la posición del disparo (por si impacta o se sale del tablero)
     * @param playerShot El disparo del jugador
     */
    private void checkingNewPosition(Sprite playerShot) {

        for (Enemy enemy :
                enemies) {

            if (playerShot.getBoundingRectangle().overlaps(enemy.getSprite().getBoundingRectangle())) {

                impactingEnemy(enemy);

                shotsToDelete.add(playerShot);

            } else if (playerShot.getY() > SCREEN_HEIGHT) {

                shotsToDelete.add(playerShot);
            }
        }
    }

    /**
     * Resta vida al enemigo, lo destruye en caso de llegar a cero y
     * suma la puntuación pertinente
     * @param enemy el enemigo con el que está impactando el disparo
     */
    private void impactingEnemy(Enemy enemy) {

        enemy.setHp(enemy.getHp() - 1);
        playerShip.setScore(playerShip.getScore() + 10);

        if (enemy.getHp() <= 0) {

            enemiesToDelete.add(enemy);
            playerShip.setScore((playerShip.getScore() + 100));
        }
    }

    /**
     * Actualiza los datos contenidos en cada ArrayList
     */
    private void updatingArrays() {
        enemies.removeAll(enemiesToDelete);
        playerShots.removeAll(shotsToDelete);
        enemiesToDelete.clear();
        shotsToDelete.clear();
    }

    /**
     * Escribe los BitMapFonts con los datos del jugador
     */
    private void writtingTexts() {

        bitmapFont.draw(batch, scoreText + playerShip.getScore(), MARGIN, MENU_HEIGHT- MARGIN /2f);
        bitmapFont.draw(batch, shieldText + playerShip.getShield(), MARGIN, MENU_HEIGHT / 3f * 2- MARGIN /2f);
        bitmapFont.draw(batch, livesText + playerShip.getLives(), MARGIN, MENU_HEIGHT / 3f * 1- MARGIN /2f);
    }


    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        bitmapFont.dispose();
        shapeRenderer.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode) {

            case Input.Keys.DPAD_LEFT:
                movingLeft();
                break;
            case Input.Keys.DPAD_RIGHT:
                movingRight();
                break;
            case Input.Keys.DPAD_UP:
                    shooting();
                break;
        }
        return false;
    }

    /**
     * Mueve al jugador a la izquierda
     */
    private void movingLeft() {
        playerShip.getSprite().setX(
                playerShip.getSprite().getX() - playerShip.getSPEED() >= 0
                        ? playerShip.getSprite().getX() - playerShip.getSPEED()
                        : playerShip.getSprite().getX()
        );
    }

    /**
     * Mueve al jugador a la derecha
     */
    private void movingRight() {
        playerShip.getSprite().setX(
                playerShip.getSprite().getX() + playerShip.getSPEED() + playerShip.getSprite().getWidth() <= SCREEN_WIDTH
                        ? playerShip.getSprite().getX() + playerShip.getSPEED()
                        : playerShip.getSprite().getX()
        );
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

        screenY = SCREEN_HEIGHT - screenY;
        boolean isTouchingShip;

        isTouchingShip = checkingIfIsTouching(screenX, screenY);

        if (!isTouchingShip) {
            shooting();
        }
        return false;
    }

    /**
     * Chequea si el jugador está tocando el Sprite de la nave
     * @param screenX la posición en el eje X donde está tocando el jugador
     * @param screenY la posición en el eje Y donde está tocando el jugador
     * @return Booleano indicando si está tocando o no al Sprite
     */
    private boolean checkingIfIsTouching(int screenX, int screenY) {
        boolean isTouchingShip = screenX >= playerShip.getSprite().getX()
                && screenX <= playerShip.getSprite().getX() + playerShip.getSprite().getWidth()
                && screenY >= playerShip.getSprite().getY()
                && screenY <= playerShip.getSprite().getY() + playerShip.getSprite().getHeight();

        if (isTouchingShip) {
            xTouched = screenX;
            yTouched = screenY;
        }

        return isTouchingShip;
    }

    /**
     * Inicializa el Sprite con el disparo del jugador
     */
    private void shooting() {

        spriteShot = new Sprite(textureShot);
        spriteShot.setX(playerShip.getSprite().getX() + playerShip.getSprite().getWidth() / 2);
        spriteShot.setY(playerShip.getSprite().getY() + playerShip.getSprite().getHeight());

        playerShots.add(spriteShot);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        xTouched = -1;
        yTouched = -1;
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {


        if (xTouched != -1 && yTouched != -1) {

            screenY = SCREEN_HEIGHT - screenY;

            checkPositionAndMove(screenX, screenY);
        }
        return false;
    }

    /**
     * Chequea que el Sprite del jugador no se sale del tablero y lo mueve
     * @param screenX La posición en el eje X donde toca el jugador
     * @param screenY La posición en el eje Y donde toca el jugador
     */
    private void checkPositionAndMove(int screenX, int screenY) {
        int desplX = xTouched - screenX;

        if (playerShip.getSprite().getX() - desplX < 0) {

            playerShip.getSprite().setX(playerShip.getSprite().getX());

        } else if (playerShip.getSprite().getX() + playerShip.getSprite().getWidth() - desplX > SCREEN_WIDTH) {

            playerShip.getSprite().setX(playerShip.getSprite().getX());

        } else {

            playerShip.getSprite().setX(playerShip.getSprite().getX() - desplX);
        }
        xTouched = screenX;
        yTouched = screenY;
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
     * Inicializa un enemigo cada 5 segundos hasta que haya el máximo
     */
    @Override
    public void run() {
        Thread tr;

        while (playing) {

            if (enemies.size() < 7) {

                enemyToLoadSprite = new Enemy(SCREEN_WIDTH, SCREEN_HEIGHT, playerShip.getSprite());
                enemies.add(enemyToLoadSprite);

                tr = new Thread(enemyToLoadSprite);
                tr.start();

                try {

                    Thread.sleep(5000);

                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
        }
    }
}
