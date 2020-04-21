package Model;

import static java.lang.Math.sqrt;

public class RKSolver extends Solver {

    public RKSolver(String ab) {
        super(ab);
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
        double kx1 = solverStepSize*currentVelX,ky1 =solverStepSize*currentVelY;
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
    public Vector2d computeSpeeds(Vector2d positions, Vector2d velocity, double step){
        Vector2d acceleration = computeAccel(positions,velocity);
        double kx1=step*acceleration.getX(), ky1=step*acceleration.getY();
        acceleration = computeAccel(positions,velocity.cloneAndAdd(kx1/2, ky1/2));
        double kx2=step*(acceleration.getX()),ky2 = step*(acceleration.getY());
        acceleration = computeAccel(positions,velocity.cloneAndAdd(kx2/2, ky2/2));
        double kx3=step*acceleration.getX(), ky3 = step*acceleration.getY();
        acceleration = computeAccel(positions,velocity.cloneAndAdd(kx3, ky3));
        double kx4=step*acceleration.getX(),ky4 = step*acceleration.getX();
        return new Vector2d(velocity.getX()+(kx1+2*kx2+2*kx3+kx4)/6,velocity.getY()+ (ky1+2*ky2+2*ky3+ky4)/6);
    }

    /**
     * Compute the acceleration at a certain point with given speed
     * @param positions coordinates of the ball
     * @param velocity speed of the ball
     * @return the acceleration
     */
    public Vector2d computeAccel(Vector2d positions, Vector2d velocity){
        Vector2d slopes = this.shape.gradient(positions);
        double velY = velocity.getY(), velX = velocity.getX();
        double sqrtSpeeds = sqrt((velX * velX) + (velY * velY));
        return new Vector2d( -(g*slopes.getX()) - (mu*g*(velX/ sqrtSpeeds)),
         -(g*slopes.getY()) - (mu*g*(velY/ sqrtSpeeds)));
    }




}
