package com.mygdx.game;

import Model.*;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Game {

    private static Main singleton;

    private PuttingCourse course;
	private PhysicsEngine solver;
	private String function=" sin (x) + y ^ 2";
	
    public SpriteBatch batch;

    public static int WIDTH = 1080;
    public static int HEIGHT = 720;
    
    public int count = 0;



    @Override
    public void create() {
        batch = new SpriteBatch();
        solver = new RKSolver(function);
        Vector2d start = new Vector2d(0,0);
        Vector2d flag =  new Vector2d((Math.PI*9.0)/2.0,0);
        course = new PuttingCourse(new FunctionMaker(function),flag,start );
        this.setScreen(new OptionMenu(this));
    }

    @Override
    public void render(){
        super.render();
    }


    public void SetCourse(PuttingCourse c){
        course=c;
    }

    public void setEngine(PhysicsEngine engine){
        this.solver = engine;
    }
    public PhysicsEngine getEngine(){
        return this.solver;
    }
    public void setCourse(PuttingCourse course){
        this.course = course;
    }
    public PuttingCourse getCourse(){
        return this.course;
    }

    /**
     * Singeleton get instance method
     * @return
     */
    public static Main getInstance(){
        if(singleton==null){
            return new Main();
        }else {
            return singleton;
        }
    }

    /**
     * singleton implementation for main complete
     */
    private Main(){

    }
}
