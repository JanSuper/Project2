package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;


public class Menu implements Screen {


    private final int BUTTON_WIDTH = 300;
    private final int BUTTON_HEIGHT = 100;

    private final int PLAY_HEIGHT = 325;
    private final int OPTION_HEIGHT = 200;
    private final int EXIT_HEIGHT = 75;
    
    public boolean newLVL = false;
    
    public PuttingSimulator hold;

    Texture exitButtonActive;
    Texture exitButtonInactive;

    Texture playButtonActive;
    Texture playButtonInactive;

    Texture optionButtonActive;
    Texture optionButtonInactive;

    Texture group20;

    private Main main;
    
    public OptionMenu menu;
    
    public int count;

    public Menu(Main main){
        this.main = main;
        menu = new OptionMenu(main);
        hold = new PuttingSimulator(main.getCourse(), main.getEngine(), main, menu);
        hold.create();
        
        newLVL = false;
        //TODO:Golf becomes PuttingSimulator
        
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


        main.batch.begin();
        main.batch.draw(group20,0, 0,main.WIDTH, main.HEIGHT );
        if(Gdx.input.getX()<Main.WIDTH/2-BUTTON_WIDTH/2+BUTTON_WIDTH && Gdx.input.getX() > Main.WIDTH/2-3*BUTTON_WIDTH/8
        && Gdx.input.getY()>Main.HEIGHT-(PLAY_HEIGHT+BUTTON_HEIGHT) && Gdx.input.getY()<Main.HEIGHT-PLAY_HEIGHT){
            main.batch.draw(playButtonInactive,Main.WIDTH/2-BUTTON_WIDTH/2,PLAY_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
            if(Gdx.input.isTouched()){ // need to talk?
                this.dispose();
                
                if (newLVL) {
                	hold.setCourse(menu);
                	hold.create();
                }
                
                hold.setOption(menu);
                hold.take_shot(hold.calcInit());
                main.setScreen(hold);
                
            }
        } else
            main.batch.draw(playButtonActive,Main.WIDTH/2-BUTTON_WIDTH/2,PLAY_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
        if(Gdx.input.getX()<Main.WIDTH/2-3*BUTTON_WIDTH/8+BUTTON_WIDTH && Gdx.input.getX() > Main.WIDTH/2-3*BUTTON_WIDTH/8
                && Gdx.input.getY()>Main.HEIGHT-(OPTION_HEIGHT+BUTTON_HEIGHT) && Gdx.input.getY()<Main.HEIGHT-OPTION_HEIGHT) {
            main.batch.draw(optionButtonInactive, Main.WIDTH / 2 - BUTTON_WIDTH / 2, OPTION_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
            if(Gdx.input.isTouched()) {
                this.dispose();
                main.setScreen(new OptionMenu(main));
                //TODO options
            }
        }else
            main.batch.draw(optionButtonActive,Main.WIDTH/2-BUTTON_WIDTH/2,OPTION_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
        if(Gdx.input.getX()<Main.WIDTH/2-BUTTON_WIDTH/2+BUTTON_WIDTH && Gdx.input.getX() > Main.WIDTH/2-BUTTON_WIDTH/2
                && Gdx.input.getY()>Main.HEIGHT-(EXIT_HEIGHT+BUTTON_HEIGHT) && Gdx.input.getY()<Main.HEIGHT-EXIT_HEIGHT) {
            main.batch.draw(exitButtonInactive, Main.WIDTH / 2 - BUTTON_WIDTH / 2, EXIT_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
            if(Gdx.input.isTouched()) {
                Gdx.app.exit();
            }
        }else
            main.batch.draw(exitButtonActive,Main.WIDTH/2-BUTTON_WIDTH/2,EXIT_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);

        main.batch.end();
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
    
    public void setOptionMenu(OptionMenu option) {
    	this.menu = option;
    }
}