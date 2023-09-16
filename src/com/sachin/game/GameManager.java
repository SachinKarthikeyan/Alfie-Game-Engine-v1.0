package com.sachin.game;

import com.sachin.engine.AbstractGame;
import com.sachin.engine.GameContainer;
import com.sachin.engine.Renderer;
import com.sachin.engine.gfx.Image;
import com.sachin.engine.gfx.ImageTile;
import com.sachin.engine.audio.SoundClip;

import java.awt.event.KeyEvent;

public class GameManager extends AbstractGame
{
    private Image image;
    private Image image2;

    public GameManager()
    {
       image = new Image("/test.png");
       image2 = new Image("/test2.png");
    }

    @Override
    public void update(GameContainer gc, float dt)
    {

    }

    float temp = 0;

    @Override
    public void render(GameContainer gc, Renderer r)
    {
            r.drawImage(image,10,10);
            r.drawImage(image2,gc.getInput().getMouseX() , gc.getInput().getMouseY());
    }
    public static void main(String args[])
    {
        GameContainer gc = new GameContainer(new GameManager());
        gc.setWidth(320);
        gc.setHeight(240);
        gc.setScale(3f);
        gc.start();
    }
}
