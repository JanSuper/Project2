package Model.Sides;

import Model.Solver;
import Model.*;
import com.badlogic.gdx.math.Vector2;

public abstract class Side implements Collide{
   Vector2d [] vertices = new Vector2d[2];

    /**
     * must create a side so the the direction of vertex1 to vertex2 is within the first 180 degrees
     * anticlockwise from the x-axis
     * @param vertex1
     * @param vertex2
     */
    public Side(Vector2d vertex1, Vector2d vertex2) {
        //the following logic is to ensure that the direction is within the first 180 degrees
        Vector2d tmp = new Vector2d( vertex2.getX()-vertex1.getX(),vertex2.getY()-vertex1.getY());
        Vector2 tmp1 = new Vector2( (float)(vertex2.getX()-vertex1.getX()),(float)(vertex2.getY()-vertex1.getY()));
        if(tmp1.angle()>=180.0){
            tmp=vertex1;
            vertex1=vertex2;
            vertex2=tmp;
        }

        vertices[0] = vertex1;
        vertices[1] = vertex2;
    }

    public boolean collideIfCollision(Solver solver) {
        boolean result = false;
        int currentPosSide = Vector2d.pointLineSide(vertices[0], vertices[1], new Vector2d(solver.getPosition().getX() +(float) Math.signum(solver.getVelocity().getX())*.75f, solver.getPosition().getY()+ Math.signum(solver.getVelocity().getY()*.75f)));
        int prevPosSide = Vector2d.pointLineSide(vertices[0], vertices[1], new Vector2d(solver.getPrevPos().getX(), solver.getPrevPos().getY()));
        if (currentPosSide != prevPosSide) {
            result = true;
            collide(currentPosSide, prevPosSide, solver);
        }
        return result;
    }


}

