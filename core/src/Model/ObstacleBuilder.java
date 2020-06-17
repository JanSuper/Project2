package Model;

import Model.Sides.Side;
import Model.Sides.VerticalSide;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.PuttingCourse;

/**
 * A class for quickly building objects/obstacles to add to the course
 */
public class ObstacleBuilder {

    private static final float MAX_DIS_STEP= .01f;

    /**
     * Sets the flag pole object in the course and ready to render
     */
    public static void makeFlagPole(){

    }

    /**
     * Creates the Model for rendering the lake
     * Creates the Obstacle with Lake logic implemented for collision
     * @param middle
     * @param radius
     */
    public static void makeLake(Vector2d middle, double radius){


    }

    /**
     * Creates the Model for rendering a Box obstacle
     * Creates the Box Object with collision logic implemented
     * @param startPos front left corner
     * @param length length of the face, always along x axis
     * @param width length of the side along the y axis
     */
    public static void makeBox(Vector2 startPos, float length, float width){
        Vector2 v1 = new Vector2(startPos.x+ length, startPos.y);
        Vector2 v2 = new Vector2(startPos.x, startPos.y+width);
        Vector2 v3 = new Vector2(startPos.x+length, startPos.y+width);

        Obstacle tmp = new Obstacle();
        tmp.addVertex(startPos);
        tmp.addVertex(v1);
        tmp.addVertex(v2);
        tmp.addVertex(v3);

        Side s1 = new VerticalSide(startPos, v2);
        Side s2 = new VerticalSide(v1,v3);
        Side s3 = new



    }

}

