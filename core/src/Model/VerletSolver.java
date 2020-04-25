package Model;


public class VerletSolver extends Solver {
    double vy =0;
    int x =0;
    //update so that the only fields that reference velocity and directions are in solvers not menu classes
    public VerletSolver(String ab) {
        super(ab);
    }

    @Override
    public void nextStep(){
        acceleration = getNextAcceleration(position, velocity);
        velocity = getNextVelocity(velocity,acceleration, solverStepSize);
        position = getNextPosition(position, velocity, solverStepSize );
        acceleration=getNextAcceleration(position, velocity);
        velocity = getNextVelocity(velocity,acceleration, solverStepSize);

    }

    private Vector2d getNextVelocity(Vector2d velocity, Vector2d acceleration, double stepSize) {
        velocity.add(0.5*(acceleration.getX())*stepSize,
                0.5*(acceleration.getY())*stepSize);
        return velocity;
    }

    private Vector2d getNextPosition (Vector2d currentPosition, Vector2d currentVelocity, double stepSize){
       currentPosition.add( currentVelocity.getX()*stepSize ,
               currentVelocity.getY()*stepSize );
       return currentPosition;
    }

    @Override
    public void setPosition(Vector2d v) {
        position = v;
    }

    @Override
    public void setVelocity(Vector2d v) {
        velocity = v;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public double getPosZ(){
        return this.shape.evaluate(position);
    }
}
