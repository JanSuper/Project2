package Model;

import org.graalvm.compiler.lir.amd64.vector.AMD64VectorMove;

public class EulerSolver extends Solver{



    public EulerSolver(String ab) {
        super(ab);
    }
    @Override
    public void setNextPositions(double solverStepSize) {
    /*    acceleration = getNextAcceleration(currentPosition, velocity);
        //update positions based on velocity
        currentPosition.add(solverStepSize*(velocity.getX()+acceleration.getX()*solverStepSize),
                solverStepSize*(velocity.getY()+acceleration.getY()*solverStepSize));// = currentPosX + solverStepSize*velocityX(solverStepSize);
        //currentPosY = currentPosY + solverStepSize*velocityY(solverStepSize);
        //update velocity
        velocity.add(acceleration.getX()*solverStepSize, acceleration.getY()*solverStepSize);
//        currentVelX = velocityX(solverStepSize);
//        currentVelY = velocityY(solverStepSize);*/
        acceleration = getNextAcceleration(position, velocity);
        position = nextPosition(position, velocity, this.solverStepSize);
        velocity = nextVelocity(velocity, acceleration, stepSize );

    }

    /**
     *
     * @param currentVelocity
     * @param currentAcceleration
     * @param stepSize
     * @return nextVelocity
     */
    public Vector2d nextVelocity(Vector2d currentVelocity, Vector2d currentAcceleration, double stepSize){
        return new Vector2d(currentVelocity.getX()+currentAcceleration.getX()*stepSize,currentVelocity.getY()+currentAcceleration.getY()*stepSize);
    }

    /**
     * @param currentPosition
     * @param currentVelocity
     * @param stepSize
     * @return the next postion
     */
    public Vector2d nextPosition(Vector2d currentPosition, Vector2d currentVelocity, double stepSize) {
        return new Vector2d(currentPosition.getX()+velocity.getX()*stepSize,currentPosition.getY()+velocity.getY()*stepSize);
    }

    @Override
    public void setPosition(Vector2d v) {
        this.position=v;
    }

    @Override
    public void setVelocity(Vector2d velocity) {
        this.velocity=velocity;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public Vector2d getVelocity() {
        return velocity;
    }
}
