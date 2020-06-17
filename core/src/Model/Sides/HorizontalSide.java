package Model.Sides;

import Model.Solver;
import com.badlogic.gdx.math.Vector2;

public class HorizontalSide extends Side {
    /**
     * must create a side so the the direction of vertex1 to vertex2 is within the first 179 degrees
     * anticlockwise from the x-axis
     *
     * @param vertex1
     * @param vertex2
     */
    public HorizontalSide(Vector2 vertex1, Vector2 vertex2) {
        super(vertex1, vertex2);
    }

    /**
     * the cur and prev parameters are for checking if it is the left or right side the ball colllides with
     * for some sides it matter which side the ball will collide with and for others it won't
     * this method will be implemented on the different types of sides and will be called if collideIfCollision is true.
     *
     * @param cur
     * @param prev
     * @param solver
     */
    @Override
    public void collide(int cur, int prev, Solver solver) {
        solver.getVelocity().setY(solver.getVelocity().getY()*-1);
    }

    /**
     * Using the Intersector class for a line you can be on the left and right of a line
     * if the previous prosition and current position are on two different sides of the line a collision occurs
     * then depending on the side of collision a left or right collision occurs if the side is of a solid object
     * only one of the sides needs to be implemented and it should only be possible to collide from one side
     *
     * @param solver
     */
    @Override
    public void collideLeft(Solver solver) {

    }

    @Override
    public void collideRight(Solver solver) {

    }
}
