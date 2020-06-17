package Model.Sides;

import Model.Solver;

public interface Collide {
    /**
     * if there is a collision performs collision logic and returns true
     * else returns false
     * @param solver
     * @return whether or not collision took place
     */
    public boolean collideIfCollision(Solver solver);

    /**
     * the cur and prev parameters are for checking if it is the left or right side the ball colllides with
     * for some sides it matter which side the ball will collide with and for others it won't
     * this method will be implemented on the different types of sides and will be called if collideIfCollision is true.
     * @param cur
     * @param prev
     * @param solver
     */
    void collide(int cur, int prev, Solver solver);

    /**
     * Using the Intersector class for a line you can be on the left and right of a line
     * if the previous prosition and current position are on two different sides of the line a collision occurs
     * then depending on the side of collision a left or right collision occurs if the side is of a solid object
     * only one of the sides needs to be implemented and it should only be possible to collide from one side
     * @param solver
     */
    void collideLeft(Solver solver);
    void collideRight(Solver solver);
}
