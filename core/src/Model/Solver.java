package Model;

import com.mygdx.game.CourseShaper;
import com.mygdx.game.FunctionMaker;

import static java.lang.Math.sqrt;

public abstract class Solver implements PhysicsEngine{
    /**
     * the step size used for finding the partial derivative
     */
    protected  double stepSize = 0.0001;
    /**
     * this is the time step that the system will be updating off of
     * 1/165s is every 0.00606060606060606060606060606061 seconds
     * TODO: make an array of all positions at these time intervals so we can set a the screen to update ball position based off of this array instead of calculating on the fly
     */
    public static double fps = 165.0;
    /**
     * Solver Step size
     * This is the step size that is changed for the set_step_size
     */
    protected double solverStepSize = 1.0/165.0;


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

    protected Vector2d goalPosition;
    protected static Function2d shape;

    public Solver(String ab) {
        shape = new FunctionMaker(ab);
    }

    /**
     * This is what the different solvers need to implement
     * Gets updates current position to next step in X and Y
     * Before completing this method it should always update the Current Velocity for the class with this.currentVelX = currentVelX + solverStepSize*Fx;
     * Likewise for currentVelY
     */
    public void setNextPositions(double solverStepSize) { }

    public double get_height(double x, double y){
        return shape.evaluate(new Vector2d(x,y));
    }
    public double get_height(Vector2d position){
        return shape.evaluate(position);
    }
    //TODO: remove slopes and use the shape.gradient() method
    public Vector2d getSlopes (double currentPosX, double currentPosY){
       return  shape.gradient(new Vector2d(currentPosX,currentPosY));
    }
    public Vector2d getSlopes (Vector2d position ){
        return shape.gradient(position);
    }
    @Override
    public void nextStep() {

//        DzDx = slopeDzDx(currentPosX, currentPosY, stepSize);
//        DzDy = slopeDzDy(currentPosX, currentPosY, stepSize);
//        setForce();
        setNextPositions(solverStepSize);
        currentPosZ = get_height(position);

    }

    /**
     * What to do with this one?
     *//*
    public void setForce() {
        Fx = -(g*DzDx) - (mu*g*(currentVelX/(sqrt((currentVelX*currentVelX) + (currentVelY*currentVelY)))));
        Fy = -(g*DzDy) - (mu*g*(currentVelY/(sqrt((currentVelX*currentVelX) + (currentVelY*currentVelY)))));
    } */
    protected Vector2d  getNextAcceleration (Vector2d position, Vector2d velocity){
        Vector2d slopes = this.shape.gradient(position);
        double velY = velocity.getY(), velX = velocity.getX();
        double sqrtSpeeds = sqrt((velX * velX) + (velY * velY));
        return new Vector2d( (-1)*(g*slopes.getX()) - (mu*g*(velX/ sqrtSpeeds)),
                (-1)*(g*slopes.getY()) - (mu*g*(velY/ sqrtSpeeds)));
    }

    /**
     * not too sure what to do with this either
     */
    /*
    public double velocityX(double h){
        return currentVelX + h*Fx;
    }

    public double velocityY(double h){
        return currentVelY + h*Fy;
    }

*/



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

}