package com.mygdx.game;

import java.util.Random;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;

public class Menu implements Screen {

    private static  Menu singleton = null;

    private final int BUTTON_WIDTH = 300;
    private final int BUTTON_HEIGHT = 100;
    private final int PLAY_HEIGHT = 325;
    private final int OPTION_HEIGHT = 200;
    private final int EXIT_HEIGHT = 75;
    public boolean newLVL = false;

    Texture exitButtonActive;
    Texture exitButtonInactive;
    Texture playButtonActive;
    Texture playButtonInactive;
    Texture optionButtonActive;
    Texture optionButtonInactive;
    Texture group20;

    public static Menu getInstance(){
        if(singleton == null) singleton = new Menu();
        return singleton;
    }

    public Menu(){
        PuttingSimulator.getInstance().create();

        newLVL = false;
        
        playButtonActive=new Texture("PlayButtonActive.jpg");
        playButtonInactive=new Texture("PlayButtonInactive.jpg");
        exitButtonActive=new Texture("ExitButtonActive.jpg");
        exitButtonInactive=new Texture("ExitButtonInactive.jpg");
        optionButtonActive=new Texture("OptionButtonActive.jpg");
        optionButtonInactive=new Texture("OptionButtonInactive.jpg");
        group20=new Texture("Group20.jpg");

    }



    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0.4f,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        Main.getInstance().batch.begin();
        Main.getInstance().batch.draw(group20,0, 0,Main.getInstance().WIDTH, Main.getInstance().HEIGHT );
        if(Gdx.input.getX()<Main.WIDTH/2-BUTTON_WIDTH/2+BUTTON_WIDTH && Gdx.input.getX() > Main.WIDTH/2-3*BUTTON_WIDTH/8
        && Gdx.input.getY()>Main.HEIGHT-(PLAY_HEIGHT+BUTTON_HEIGHT) && Gdx.input.getY()<Main.HEIGHT-PLAY_HEIGHT){
            Main.getInstance().batch.draw(playButtonInactive,Main.WIDTH/2-BUTTON_WIDTH/2,PLAY_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
            if(Gdx.input.isTouched()){ 
                this.dispose();

                play(0,0);
                PuttingCourse.getInstance().set_friction_coefficient((float)(1.0 - .5*new Random().nextDouble()));
                PuttingSimulator.getInstance().take_shot(PuttingSimulator.getInstance().calcInit());
                Main.getInstance().setScreen(PuttingSimulator.getInstance());
                
            }
        } else
            Main.getInstance().batch.draw(playButtonActive,Main.WIDTH/2-BUTTON_WIDTH/2,PLAY_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
        if(Gdx.input.getX()<Main.WIDTH/2-3*BUTTON_WIDTH/8+BUTTON_WIDTH && Gdx.input.getX() > Main.WIDTH/2-3*BUTTON_WIDTH/8
                && Gdx.input.getY()>Main.HEIGHT-(OPTION_HEIGHT+BUTTON_HEIGHT) && Gdx.input.getY()<Main.HEIGHT-OPTION_HEIGHT) {
            Main.getInstance().batch.draw(optionButtonInactive, Main.WIDTH / 2 - BUTTON_WIDTH / 2, OPTION_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
            if(Gdx.input.isTouched()) {
                this.dispose();
                Main.getInstance().setScreen(OptionMenu.getInstance());

            }
        }else
            Main.getInstance().batch.draw(optionButtonActive,Main.WIDTH/2-BUTTON_WIDTH/2,OPTION_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
        if(Gdx.input.getX()<Main.WIDTH/2-BUTTON_WIDTH/2+BUTTON_WIDTH && Gdx.input.getX() > Main.WIDTH/2-BUTTON_WIDTH/2
                && Gdx.input.getY()>Main.HEIGHT-(EXIT_HEIGHT+BUTTON_HEIGHT) && Gdx.input.getY()<Main.HEIGHT-EXIT_HEIGHT) {
            Main.getInstance().batch.draw(exitButtonInactive, Main.WIDTH / 2 - BUTTON_WIDTH / 2, EXIT_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
            if(Gdx.input.isTouched()) {
                Gdx.app.exit();
            }
        }else
            Main.getInstance().batch.draw(exitButtonActive,Main.WIDTH/2-BUTTON_WIDTH/2,EXIT_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);

        Main.getInstance().batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
    
    public void play(float speed, float angle){
    	if (speed == 0) {
    		speed = 0.00001f;
    	}

    	
        PuttingSimulator.getInstance().create();
        Main.getInstance().setScreen(PuttingSimulator.getInstance());
    }
}