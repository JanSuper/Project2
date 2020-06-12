package com.mygdx.game;

import java.util.regex.Pattern;

import Bot.WigerToods;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.CollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionAlgorithm;
import com.badlogic.gdx.physics.bullet.collision.btCollisionAlgorithmConstructionInfo;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btDispatcherInfo;
import com.badlogic.gdx.physics.bullet.collision.btManifoldResult;
import com.badlogic.gdx.physics.bullet.collision.btSphereBoxCollisionAlgorithm;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import Model.*;


public class PuttingSimulator extends Game implements Screen{
    private static PuttingSimulator singleton = null;

	String decimalPattern = "([0-9]*)\\.([0-9]*)";
    String naturalPattern = "([0-9]*)";


    private Vector2d ballPosition;

    private WigerToods ai=null;

    PerspectiveCamera cam;
    CameraInputController camController;
    ModelBatch modelBatch;
    Array<ModelInstance> instances;
    Environment environment;
    Model model;
    ModelInstance groundBall;
    ModelInstance ball;
    ModelInstance flagPole;
    ModelInstance flag;

    btCollisionShape groundShape;
    btCollisionShape ballShape;
    btCollisionShape wallShape;
    btCollisionObject groundObject;
    btCollisionObject ballObject;
    btCollisionObject wallObject;
    TextField textFieldSpeed;
    TextField textFieldAngle;
    TextButton buttonShot;
    Label shotLabelSpeed;
    Label shotLabelAngle;
    private Stage stage;
    int count;
    boolean shot = false;

    /**
     * API Required
     * @param course
     * @param solver
     */
    public PuttingSimulator(PuttingCourse course, PhysicsEngine solver){
        PuttingCourse.getInstance().setCourse(course);
        Main.getInstance().setSolver(solver);
    }
    private PuttingSimulator(){

    }

    public static PuttingSimulator getInstance(){
        if(singleton==null) singleton = new PuttingSimulator();
        return singleton;
    }

    public void set_ball_position(Vector2d v){
        Main.getInstance().getSolver().setPosition(v);
    }

    public Vector2d get_ball_position(){
        return Main.getInstance().getSolver().getPosition();
    }

    public void take_shot(Vector2d initial_ball_velocity){
   /** 	Main.getInstance(.getSolver().setVelX((float)initial_ball_velocity.getX());
    	Main.getInstance(.getSolver().setVelY((float)initial_ball_velocity.getY());
    *///changes to
       // if(initial_ball_velocity.evaluateVector()> menu.vMax) initial_ball_velocity.scaleDown(menu.vMax);

        Main.getInstance().getSolver().setVelocity(initial_ball_velocity);
    }

    @Override
    public void create() {
        Bullet.init();
        this.ballPosition=PuttingCourse.getInstance().get_start_position();


        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        cam = new PerspectiveCamera(105, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(3f, 7f, 10f);
        cam.update();

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        // to create a new object add the id to the mb.node()
        // then call mb.part and give the type of object, shape etc
        ModelBuilder mb = new ModelBuilder();
        mb.begin();
        mb.node().id = "ground";
        mb.part("box", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.RED)))
                .box(5f, 1f, 5f);
        mb.node().id = "ball";
        mb.part("sphere", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.WHITE)))
                .sphere(1f, 1f, 1f, 10, 10);
        mb.node().id = "wall";
        mb.part("wall",  GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.RED)))
                .box(5f, 2f, 1f);
        mb.node().id = "groundBalls";
        mb.part("sphere", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GREEN)))
                .sphere(0.5f, 0.5f, 0.5f, 5, 5);
        mb.node().id = "flagpole";
        mb.part("cylinder", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GRAY)))
        		.cylinder(0.5f, 5f, 0.5f, 10);
        mb.node().id = "flag";
        mb.part("box", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.RED)))
        		.box(0.25f,1f,1f);
        model = mb.end();

        // create a modelinstance of the new model

        ball = new ModelInstance(model, "ball");
        groundBall = new ModelInstance(model, "groundBalls");
      //TODO: double check variables
        // translate is to a certain position if wanted
        // remember that the middle of the object is 0.0.0
        ball.transform.setToTranslation((float)PuttingCourse.getInstance().get_start_position().getX(),
                (float)PuttingCourse.getInstance().get_height().evaluate(new Vector2d(PuttingCourse.getInstance().get_start_position().getX(),PuttingCourse.getInstance().get_start_position().getY())) + 1f,
                (float)PuttingCourse.getInstance().get_start_position().getY());
        // add the modelinstance of the object to the array of objects that has to be rendered
        instances = new Array<ModelInstance>();
        instances.add(ball);


      /*  for (float j = -5f; j <= 5f; j += 0.3f) {
            for (float i = 0; i <= 199; i += 0.3f) {
                groundBall = new ModelInstance(model, "groundBalls");
                groundBall.transform.setToTranslation(i - 50,(float)course.get_height().evaluate(new Vector2d(i,j))-.25f, j);
                instances.add(groundBall);
            }
        }*/
        instances.addAll(PuttingCourse.getInstance().getCourseModel(model));

        flagPole = new ModelInstance(model, "flagpole");
        flagPole.transform.setToTranslation((float)PuttingCourse.getInstance().get_flag_position().getX(), 2.5f + (float)PuttingCourse.getInstance().get_height().evaluate(new Vector2d(PuttingCourse.getInstance().get_flag_position().getX(), PuttingCourse.getInstance().get_flag_position().getY())), (float)PuttingCourse.getInstance().get_flag_position().getY());
        instances.add(flagPole);
        
        flag = new ModelInstance(model, "flag");
        flag.transform.setToTranslation((float)PuttingCourse.getInstance().get_flag_position().getX(),(float)PuttingCourse.getInstance().get_height().evaluate(new Vector2d(PuttingCourse.getInstance().get_flag_position().getX(), PuttingCourse.getInstance().get_flag_position().getY())) + 4.5f, (float)PuttingCourse.getInstance().get_flag_position().getY()- .5f);
        instances.add(flag);
        
        // give the object a collision shape if you want it to have collision
        ballShape = new btSphereShape(0.5f);
        groundShape = new btBoxShape(new Vector3(2.5f, 0.5f, 2.5f));
        wallShape = new btBoxShape(new Vector3(2.5f, 1f, .5f));

        // create a collision object with that shape
        groundObject = new btCollisionObject();
        groundObject.setCollisionShape(groundShape);
        //groundObject.setWorldTransform(ground.transform);

        ballObject = new btCollisionObject();
        ballObject.setCollisionShape(ballShape);
        ballObject.setWorldTransform(ball.transform);

        wallObject = new btCollisionObject();
        wallObject.setCollisionShape(wallShape);
      //  ballObject.setWorldTransform(wall.transform);

//        collisionConfig = new btDefaultCollisionConfiguration();
//        dispatcher = new btCollisionDispatcher(collisionConfig);

/**        Main.getInstance(.getSolver().setPosX((float)ballPosition.getX());
        Main.getInstance(.getSolver().setPosY((float)ballPosition.getY());
        Main.getInstance(.getSolver().setPosZ(Main.getInstance(.getSolver().get_height((float)ballPosition.getX(), (float)ballPosition.getY()) + 1f);
*///changing these lines to make use of RKM
        Main.getInstance().getSolver().setPosition(ballPosition);

        take_shot(calcInit());
        //System.out.println(course.get_flag_position().getX() + " " + course.get_flag_position().getY());
        
        count = 0;
        
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        
        shotLabelSpeed= new Label("Speed:", skin);
        shotLabelAngle = new Label("Angle:", skin);

        shotLabelSpeed.setSize(100, 60);
        shotLabelSpeed.setPosition(50,Main.getInstance().HEIGHT-100);
        shotLabelAngle.setSize(100, 60);
        shotLabelAngle.setPosition(50,Main.getInstance().HEIGHT-170);

        textFieldSpeed = new TextField("", skin);
        textFieldSpeed.setPosition(150, Main.getInstance().HEIGHT-100);
        textFieldSpeed.setSize(300,60);
        textFieldAngle = new TextField("", skin);
        textFieldAngle.setPosition(150, Main.getInstance().HEIGHT-170);
        textFieldAngle.setSize(300,60);
        stage = new Stage();

        buttonShot = new TextButton("Take a shot!",skin);
        buttonShot.setPosition(470,Main.getInstance().HEIGHT-170);
        buttonShot.setSize(100,30);
        buttonShot.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
            	count = 0;
            	System.out.println("here");
                if((Pattern.matches(decimalPattern, textFieldSpeed.getText())||textFieldSpeed.getText().matches(naturalPattern))&&
                        (Pattern.matches(decimalPattern, textFieldAngle.getText())||textFieldAngle.getText().matches(naturalPattern))) {
                	
                	
                    play(Float.parseFloat(textFieldSpeed.getText()), Float.parseFloat(textFieldAngle.getText()));
                }
                else if(!Pattern.matches(decimalPattern, textFieldSpeed.getText()))
                    textFieldSpeed.setText("");
                else if(!Pattern.matches(decimalPattern, textFieldAngle.getText()))
                    textFieldAngle.setText("");
                
                
               
            }
        });
        
        stage = new Stage();
        
        stage.addActor(buttonShot);
        stage.addActor(textFieldSpeed);
        stage.addActor(textFieldAngle);
        stage.addActor(shotLabelSpeed);
        stage.addActor(shotLabelAngle);
    }

    @Override
    public void render (float delta) {

        //TODO: add game over
        if ( Main.getInstance().getSolver().finish()) {
           
//        	Menu holdMenu = new Menu(Main.getInstance());
//        	holdMenu.newLVL = true;
            Main.getInstance().setScreen(Menu.getInstance());
        }
        else if (shot) {
        	stage.act(delta);
            stage.draw();
        }
        else if (count == 2*60) {
            if(ai==null) {
                Gdx.input.setInputProcessor(stage);
                shot = true;
//        	Menu holdMenu = new Menu(main);
//        	main.setScreen(holdMenu);
                buttonShot.addListener(new ClickListener() {
                    @Override
                    public void touchUp(InputEvent e, float x, float y, int point, int button) {
                        count = 0;
                        System.out.println("here");
                        if ((Pattern.matches(decimalPattern, textFieldSpeed.getText()) || textFieldSpeed.getText().matches(naturalPattern)) &&
                                (Pattern.matches(decimalPattern, textFieldAngle.getText()) || textFieldAngle.getText().matches(naturalPattern))) {

                            play(Float.parseFloat(textFieldSpeed.getText()), Float.parseFloat(textFieldAngle.getText()));
                            shot = false;
                            Gdx.input.setInputProcessor(camController);
                        } else if (!Pattern.matches(decimalPattern, textFieldSpeed.getText()))
                            textFieldSpeed.setText("");
                        else if (!Pattern.matches(decimalPattern, textFieldAngle.getText()))
                            textFieldAngle.setText("");


                    }
                });
            }else{
                Vector2d shot = ai.search();
                take_shot(shot);
            }
        }
        else {
        	Main.getInstance().getSolver().nextStep();
        	Main.getInstance().getSolver().setPosZ(Main.getInstance().getSolver().get_height(Main.getInstance().getSolver().getPosition().getX(), Main.getInstance().getSolver().getPosition().getY()));

   /**     	this.ballPosition.setX(Main.getInstance(.getSolver().getPosX());
        	this.ballPosition.setY(Main.getInstance(.getSolver().getPosY());
     */  // changes to..
            this.ballPosition= Main.getInstance().getSolver().getPosition();

            ball.transform.setToTranslation((float)ballPosition.getX(), (float) Main.getInstance().getSolver().getPosZ()+.5f,(float) ballPosition.getY());
            ballObject.setWorldTransform(ball.transform);

            cam.position.set((float) ballPosition.getX() - 5f, (float)Math.max(5f, Main.getInstance().getSolver().getPosZ()+3f),(float) ballPosition.getY());
            cam.update();
            camController.update();


            Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1.f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

            modelBatch.begin(cam);
            for(ModelInstance instance : instances)  modelBatch.render(instance, environment);
            modelBatch.end();
            
              if (Math.abs(Main.getInstance().getSolver().getVelocity().getX()) <= 0.2f &&  Math.abs(Main.getInstance().getSolver().getVelocity().getY()) <= 0.2f) {
            	count++;
            }
            else {
            	count = 0;
            }

        }
    }

//    public boolean isVisible(PerspectiveCamera cam, ModelInstance instance){
//        instance.transform.getTranslation(posTemp);
//        return cam.frustum.pointInFrustum(posTemp);
//    }

    public void setAi(WigerToods wt){
        this.ai = wt;
    }

    boolean checkCollision () {
        // add a collisionObject wrapper for your new object
        CollisionObjectWrapper co0 = new CollisionObjectWrapper(ballObject);
        CollisionObjectWrapper co1 = new CollisionObjectWrapper(groundObject);
        CollisionObjectWrapper co2 = new CollisionObjectWrapper(wallObject);

        btCollisionAlgorithmConstructionInfo ci = new btCollisionAlgorithmConstructionInfo();
 //       ci.setDispatcher1(dispatcher);
        btCollisionAlgorithm algorithmBallGround = new btSphereBoxCollisionAlgorithm(null, ci, co0.wrapper, co1.wrapper, false);
        btCollisionAlgorithm algorithmBallWall = new btSphereBoxCollisionAlgorithm(null, ci, co0.wrapper, co2.wrapper, false);

        btDispatcherInfo info = new btDispatcherInfo();
        btManifoldResult resultBallGround = new btManifoldResult(co0.wrapper, co1.wrapper);
        btManifoldResult resultBallWall = new btManifoldResult(co0.wrapper, co2.wrapper);

        algorithmBallGround.processCollision(co0.wrapper, co1.wrapper, info, resultBallWall);

        boolean r = resultBallWall.getPersistentManifold().getNumContacts() > 0;

        resultBallWall.dispose();
        info.dispose();
        algorithmBallGround.dispose();
        ci.dispose();
        co1.dispose();
        co0.dispose();

        return r;
    }

    @Override
    public void dispose () {
        groundObject.dispose();
        groundShape.dispose();

        ballObject.dispose();
        ballShape.dispose();

//        dispatcher.dispose();
//        collisionConfig.dispose();

        modelBatch.dispose();
        model.dispose();
        
    }

    @Override
    public void pause () {
    }

    @Override
    public void resume () {
    }

    @Override
    public void resize (int width, int height) {
    }
    //TODO: height formula becomes puttingCourse.getHeight().evaluate(vector2D)
    public static float heightFormula(float x, float y) {
        return (float)(Math.sin(x) + Math.pow(Math.abs(y), 1.5));
    }

    /**
     * TODO: i think this is the definition
     * To calc convert the double:velocity field in OptionMenu to a Vector2d
     * would usually be used for user input of velocity and angle
     * @return
     */
    public Vector2d calcInit() {
            //max velocity
        if (OptionMenu.getInstance().velocity>PuttingCourse.getInstance().get_maximum_velocity()){
            OptionMenu.getInstance().velocity=(float)PuttingCourse.getInstance().get_maximum_velocity();
        }
    	return new Vector2d(Math.cos(OptionMenu.getInstance().angle/360*2*Math.PI)*OptionMenu.getInstance().velocity, Math.sin(OptionMenu.getInstance().angle/360*2*Math.PI)*OptionMenu.getInstance().velocity);

    }
    @Override
    public void show() {


    }

    @Override
    public void hide() {

    }
    
    public void play(float speed, float angle){
    	if (speed == 0) {
    		speed = 0.00001f;
    	}
        if (speed>(float) PuttingCourse.getInstance().get_maximum_velocity()){
            speed=(float) PuttingCourse.getInstance().get_maximum_velocity();
        }
    	double holdxv = (speed*(Math.cos(angle/180*Math.PI)));
    	double holdyv = (speed*(Math.sin(angle/180*Math.PI)));
    	Vector2d shot = new Vector2d(holdxv, holdyv);
        Main.getInstance().getSolver().setVelocity(shot);
        take_shot(shot);
        Main.getInstance().getSolver().nextStep();
    }
}