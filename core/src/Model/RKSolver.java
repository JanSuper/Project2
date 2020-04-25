package Model;

import static java.lang.Math.sqrt;

public class RKSolver extends Solver {

    public RKSolver(String ab) {
        super(ab);
    }

    // variable instances to change in the core Solver?
    Vector2d position;
    Vector2d velocity;


    @Override
    public void nextStep() {


        setNextPositions(solverStepSize);
        currentPosZ = get_height(position);

        this.position = getNextPositions(this.position, this.velocity, this.solverStepSize);
        this.velocity = computeSpeeds(this.position,this.velocity, this.solverStepSize);

    }

    /**
     * Compute the position of the ball at next time step with RungeKutta method
     * @param positions current coordinates of the ball
     * @param velocity current speed of the ball
     * @param solverStepSize current step size
     * @return Coordinates of the ball in Vector2d form
     */
    public Vector2d getNextPositions(Vector2d positions, Vector2d velocity, double solverStepSize) {
        Vector2d velApprox;

        double kx1 = solverStepSize*velocity.getX(),ky1 =solverStepSize*velocity.getY();
        velApprox = computeSpeeds(positions.cloneAndAdd(kx1/2, ky1/2), velocity, solverStepSize);
        double kx2 = solverStepSize*velApprox.getX(), ky2 = solverStepSize*velApprox.getY();
        velApprox = computeSpeeds(positions.cloneAndAdd(kx2/2, ky2/2), velocity, solverStepSize);
        double kx3 = solverStepSize*velApprox.getX(), ky3 = solverStepSize*velApprox.getY();
        velApprox = computeSpeeds(positions.cloneAndAdd(kx3, ky3), velocity, solverStepSize);
        double kx4 = solverStepSize*velApprox.getX(), ky4 = solverStepSize*velApprox.getY();

        return new Vector2d(positions.getX()+(kx1+2*kx2+2*kx3+kx4)/6, positions.getY() +(ky1+2*ky2+2*ky3+ky4)/6);
    }

    /**
     * Compute the velocity at given timestep
     * @param positions current coordinates of the ball
     * @param velocity current speed of the ball
     * @param step current step size
     * @return
     */
    private Vector2d computeSpeeds(Vector2d positions, Vector2d velocity, double step){
        Vector2d acceleration = getNextAcceleration(positions,velocity);
        double kx1=step*acceleration.getX(), ky1=step*acceleration.getY();
        acceleration = getNextAcceleration(positions,velocity.cloneAndAdd(kx1/2, ky1/2));
        double kx2=step*(acceleration.getX()),ky2 = step*(acceleration.getY());
        acceleration = getNextAcceleration(positions,velocity.cloneAndAdd(kx2/2, ky2/2));
        double kx3=step*acceleration.getX(), ky3 = step*acceleration.getY();
        acceleration = getNextAcceleration(positions,velocity.cloneAndAdd(kx3, ky3));
        double kx4=step*acceleration.getX(),ky4 = step*acceleration.getY();
        return new Vector2d(velocity.getX()+(kx1+2*kx2+2*kx3+kx4)/6,velocity.getY()+ (ky1+2*ky2+2*ky3+ky4)/6);
    }

    public void setPosition(Vector2d position){
        this.position = position;
    }


    @Override
    public double getPosZ(){
        return this.shape.evaluate(position);
    }




    public Vector2d getPosition(){
        return this.position;
    }

    public void setVelocity(Vector2d v){
        this.velocity=v;
    }
    @Override
    public Vector2d getVelocity() {
        return velocity;
    }

}
