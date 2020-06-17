package Model.Sides;

import Model.Solver;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

public abstract class Side implements Collide{
   Vector2 [] vertices = new Vector2[2];

    /**
     * must create a side so the the direction of vertex1 to vertex2 is within the first 180 degrees
     * anticlockwise from the x-axis
     * @param vertex1
     * @param vertex2
     */
    public Side(Vector2 vertex1, Vector2 vertex2) {
        //the following logic is to ensure that the direction is within the first 180 degrees
        Vector2 tmp = new Vector2( vertex2.x-vertex1.x,vertex2.y-vertex1.y);
        if(tmp.angle()>=180.0){
            tmp=vertex1;
            vertex1=vertex2;
            vertex2=tmp;
        }

        vertices[0] = vertex1;
        vertices[1] = vertex2;
    }

    public boolean collideIfCollision(Solver solver) {
        boolean result = false;
        int currentPosSide = Intersector.pointLineSide(vertices[0], vertices[1], new Vector2((float) solver.getPosition().getX(), (float) solver.getPosition().getY()));
        int prevPosSide = Intersector.pointLineSide(vertices[0], vertices[1], new Vector2((float) solver.getPrevPos().getX(), (float) solver.getPrevPos().getY()));
        if (currentPosSide != prevPosSide) {
            result = true;
            collide(currentPosSide, prevPosSide, solver);
        }
        return result;
    }


}

