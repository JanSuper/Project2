package Model.Sides;

import Model.Solver;
import Model.Vector2d;
import Model.VectorConverter;
import com.badlogic.gdx.math.Vector2;

public class AngleSide extends Side {
    /**
     * must create a side so the the direction of vertex1 to vertex2 is within the first 179 degrees
     * anticlockwise from the x-axis
     *
     * @param vertex1
     * @param vertex2
     */
    public AngleSide(Vector2d vertex1, Vector2d vertex2) {
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

//    public void collide(int cur, int prev, Solver solver) {//new Vector2(-1f,(vertices[1].x-vertices[0].x)/(vertices[1].y-vertices[0].y))
//        Vector2 vel = VectorConverter.convert(solver.getVelocity());
//        Vector2 normal = new Vector2(1, -(vertices[1].x-vertices[0].x)/(vertices[1].y-vertices[0].y));
////        System.out.println(((vertices[1].y-vertices[0].y)/(vertices[1].x-vertices[0].x)));
////        System.out.println(vel);
////        System.out.println(normal);
//        float c = vel.dot(normal)/(normal.dot(normal));
//        vel=normal.scl(c);
////        System.out.println(c);
////        System.out.println(vel);
//        solver.getVelocity().add(-2*vel.x,-2*vel.y);
//        System.out.println(solver.getVelocity());
//    }

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
