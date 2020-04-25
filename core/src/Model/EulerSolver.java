package Model;

public class EulerSolver extends Solver{

    public EulerSolver(String ab) {
        super(ab);
    }
    @Override
    public void nextStep() {
        position = nextPosition(position, velocity, solverStepSize);
        acceleration = getNextAcceleration(position, velocity);
        velocity = nextVelocity(velocity, acceleration, solverStepSize );
        currentPosZ = get_height(position);
    }
    /**
     *
     * @param currentVelocity
     * @param currentAcceleration
     * @param stepSize
     * @return nextVelocity
     */
    public Vector2d nextVelocity(Vector2d currentVelocity, Vector2d currentAcceleration, double stepSize){
        return new Vector2d(currentVelocity.getX()+(currentAcceleration.getX()*stepSize),currentVelocity.getY()+(currentAcceleration.getY()*stepSize));
    }

    /**
     * @param currentPosition
     * @param currentVelocity
     * @param stepSize
     * @return the next postion
     */
    public Vector2d nextPosition(Vector2d currentPosition, Vector2d currentVelocity, double stepSize) {
        return new Vector2d(currentPosition.getX()+(currentVelocity.getX()*stepSize),currentPosition.getY()+(currentVelocity.getY()*stepSize));
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
