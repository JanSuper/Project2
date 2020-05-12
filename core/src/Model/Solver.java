package Model;

import com.mygdx.game.CourseShaper;
import com.mygdx.game.FunctionMaker;
import java.util.ArrayList;
import java.util.LinkedList;

import static java.lang.Math.sqrt;

public abstract class Solver implements PhysicsEngine{
    /**
     * this is the time step that the system will be updating off of
     * 1/165s is every 0.00606060606060606060606060606061 seconds
     * 0.00001
     * TODO: make an array of all positions at these time intervals so we can set a the screen to update
     * ball position based off of this array instead of calculating on the fly, this will allow us to calculate the motion in smaller steps and only store evere 0.006 seconds
     */
    public static double fps = 165.0;
    /**
     * Solver Step size
     * This is the step size that is changed for the set_step_size
     */
    protected double solverStepSize = 1.0/165.0;//moving to 0.00001 when we refactor the putting simulator
    private int modulus = (int)((1.0/fps)/solverStepSize);


    protected double currentPosZ;

    protected Vector2d position;
    protected Vector2d velocity;
    protected Vector2d acceleration;

    protected Vector2d slopes;


    public double Fx;
    public double Fy;

    protected double g = 9.81;
    protected double m = 45.93;
    protected double mu = 0.3;
    protected double vmax= 15.0;
    protected double tol = 0.02;

    protected static Vector2d goalPosition;
    protected static Function2d shape;

    public Solver(String ab) {
        shape = new FunctionMaker(ab);
    }

    /**
     * This is what the different solvers need to implement
     * Gets updates current position to next step in X and Y
     * Before completing this method it should always update the Current Velocity and Acceleration
     */
    @Override
    public void nextStep() {   }

    public double get_height(double x, double y){
        return shape.evaluate(new Vector2d(x,y));
    }
    public double get_height(Vector2d position){
        return shape.evaluate(position);
    }
    public Vector2d getSlopes (double currentPosX, double currentPosY){
       return  shape.gradient(new Vector2d(currentPosX,currentPosY));
    }
    public Vector2d getSlopes (Vector2d position){
        return shape.gradient(position);
    }
    protected Vector2d  getNextAcceleration (Vector2d position, Vector2d velocity){
        Vector2d slopes = this.shape.gradient(position);
        double velY = velocity.getY();
        double velX = velocity.getX();
        double sqrtSpeeds = sqrt((velX * velX) + (velY * velY));
        return new Vector2d( (-1)*(g*slopes.getX()) - (mu*g*(velX/ sqrtSpeeds)),
                (-1)*(g*slopes.getY()) - (mu*g*(velY/ sqrtSpeeds)));
    }
    @Override
    public void setPosZ(double d) { //do we need this method?
        currentPosZ=d;
    }

    @Override
    public void setPosition(Vector2d v){
        this.position = v;
    }

    @Override
    public void setMu(double mu) {
        this.mu=mu;
    }

    @Override
    public void setVMax(double vMax) {
        vmax=vMax;
    }

    @Override
    public double getPosZ() {
        return currentPosZ;
    }

    public Vector2d getVelocity() {
        return velocity;
    }

    public Vector2d takeShot(Vector2d position, Vector2d velocity){
        //Vector2d tmpPosition = position.cloneAndAdd(-100,-100);
        this.velocity=velocity;
        this.position=position;
        int count =0;
        while(count<300){
            nextStep();
            if((this.velocity.getX()<0.2)&&(this.velocity.getY()<0.2)){
                count++;
            }else{
                count = 0;
            }
        }
        return this.position;
    }

    public LinkedList<Vector2d> takeShot(Vector2d velocity){
        LinkedList<Vector2d> positionList = new LinkedList<Vector2d>();
        int stepCounter=0;
        Vector2d tmpPosition = position.cloneAndAdd(-100,-100);
        while((tmpPosition.difference(position)>0.000000001)&&(acceleration.evaluateVector()>0.000001)){
            nextStep();
            if(stepCounter%modulus==0) {
                positionList.add(position.clone());
            }
            stepCounter++;
        }
        return positionList;
    }
    public boolean finish() {
    	return (((((goalPosition.getX() - tol <= this.position.getX()) &&
                (position.getX() <= goalPosition.getX()+ tol))
                &&((goalPosition.getY() - tol <= this.position.getY())
                && (this.position.getY() <= goalPosition.getY() + tol)))
                && (velocity.getX()<= 5 && velocity.getY()<= 5)));

    }

    /**
     * this sets goal position but does not set flag position TODO need to link these up
     * @param x Vector2D value that we want to set the goal position to
     */
    public void setGoalPosition(Vector2d x){this.goalPosition=x;}
    public Vector2d getGoalPosition(){return goalPosition;}
}