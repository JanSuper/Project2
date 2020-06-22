package Model;

import com.badlogic.gdx.math.Intersector;
import com.mygdx.game.Main;
import com.mygdx.game.PuttingCourse;
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
    protected Vector2d prevPos;
    protected Vector2d velocity;
    protected Vector2d acceleration;
    protected double g = 9.81;


    boolean stopShot = false;
    boolean isAi = true;

    /**
     * This is what the different solvers need to implement
     * Gets updates current position to next step in X and Y
     * Before completing this method it should always update the Current Velocity and Acceleration
     */
    @Override
    public void nextStep() {   }

    public double get_height(double x, double y){
        return PuttingCourse.getInstance().get_height().evaluate(new Vector2d(x,y));
    }
    public double get_height(Vector2d position){
        return PuttingCourse.getInstance().get_height().evaluate(position);
    }
    public Vector2d getSlopes (double currentPosX, double currentPosY){
       return  PuttingCourse.getInstance().get_height().gradient(new Vector2d(currentPosX,currentPosY));
    }
    public Vector2d getSlopes (Vector2d position){
        return PuttingCourse.getInstance().get_height().gradient(position);
    }

    protected Vector2d  getNextAcceleration (Vector2d position, Vector2d velocity){
        Vector2d slopes = PuttingCourse.getInstance().get_height().gradient(position);
        double velY = velocity.getY();
        double velX = velocity.getX();
        double sqrtSpeeds = sqrt((velX * velX) + (velY * velY));
        return new Vector2d( (-1)*(g*slopes.getX()) - (PuttingCourse.getInstance().get_friction_coefficient()*g*(velX/ sqrtSpeeds)),
                (-1)*(g*slopes.getY()) - (PuttingCourse.getInstance().get_friction_coefficient()*g*(velY/ sqrtSpeeds)));
    }


    public Vector2d getPrevPos() {
        return prevPos;
    }

    public void setPrevPos(Vector2d prevPos) {
        this.prevPos = prevPos;
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
    public double getPosZ() {
        return currentPosZ;
    }

    public Vector2d getVelocity() {
        return velocity;
    }

    /**
     * A couple of things going on here that could cause bugs
     * -we need previous position to always be updated every step, should this be done in the next Step implementations
     * - we are setting the position back to where we started before we entered the loop, this was for the benefit of the search Ai.
     * @param position
     * @param velocity
     * @return
     */
    public Vector2d takeShot(Vector2d position, Vector2d velocity){
        this.velocity=velocity;
        this.position=position;
        if(velocity.evaluateVector()>PuttingCourse.getInstance().get_maximum_velocity()) this.velocity.scaleDown(PuttingCourse.getInstance().get_maximum_velocity());
        int count =0;
        prevPos = position;
        while(count<300){

            nextStep();
            if((Math.abs(this.velocity.getX())<0.2)&&(Math.abs(this.velocity.getY())<0.2)){
                count++;
            }else{
                count = 0;
            }
        }
//        setVelocity(new Vector2d(0,0));
        Vector2d tmp = this.position;
        this.position = position;
        return tmp;
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

    public boolean finish(Vector2d flagPostion, double tolerance) {
    	return (((((flagPostion.getX() - tolerance <= this.position.getX()) &&
                (position.getX() <= flagPostion.getX()+ tolerance))
                &&((flagPostion.getY() - tolerance <= this.position.getY())
                && (this.position.getY() <= flagPostion.getY() + tolerance)))
    			&& (Math.abs(velocity.getX())<= 0.01 && Math.abs(velocity.getY())<= 0.01)));

    }
    
    public int isLayingStill(int count, boolean canCount) {

        if (!stopShot) {//this is the variable used if in water and the ball needs to stop suddenly
            if ((Math.abs(velocity.getX()) <= 0.2f && Math.abs(velocity.getY()) <= 0.2f) && canCount)
                return ++count;
            else
                return 0;
        }else{
            stopShot = false;
            return Integer.MAX_VALUE;
        }
    }


       public abstract void set_step_size(double h);

    public void setIsAi(boolean x){
        this.isAi=x;
    }
    public void stopShot(){
        //velocity = new Vector2d(0,0);
        position = new Vector2d
                (position.getX()+Math.signum(velocity.getX())*-2,
                        position.getY()+Math.signum(velocity.getY())*-2);
        stopShot=true;
    }

}