package Model;

public interface Obstacle {
    /**
     * Checks if the ball is after colliding with the Obstacle
     * @param ballPosition
     * @return
     */
    public boolean contains(Vector2d ballPosition);

    /**
     * Obstacle needs to implement collision logic
     *The Solver positions/velocities need changing And/Or the game needs to stop/reset
     * Game is not needed as a parameter because of the usage of the singleton creational pattern so we can act directly
     * on the game here if the implementation requires
     * @param solver
     */
    public void collide(Solver solver);
}