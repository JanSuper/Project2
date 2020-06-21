package com.mygdx.game;

import Model.*;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Game {

    private static Main singleton;
    private PhysicsEngine solver = new RKSolver();
	
    public SpriteBatch batch;

    public static int WIDTH = 1080;
    public static int HEIGHT = 720;
    
    public int count = 0;

    @Override
    public void create() {
        batch = new SpriteBatch();
        this.setScreen(Menu.getInstance());
    }

    @Override
    public void render(){
        super.render();
    }

    public void setSolver(PhysicsEngine engine){
        this.solver = engine;
        solver.setIsAi(false);
    }
    public PhysicsEngine getSolver(){
        return this.solver;
    }

    /**
     * Singleton get instance method
     * @return
     */
    public static Main getInstance(){
        if(singleton==null){
            singleton= new Main();
        }
            return singleton;

    }

    /**
     * singleton implementation for main complete
     */
    private Main(){
        solver.setIsAi(false);
    }

}
