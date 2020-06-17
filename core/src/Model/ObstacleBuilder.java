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
     * -Creates the Model instance and everything needed to render
     * -Creates the Obstacle for collision
     * width is negligible
     * @param startPos
     * @param endPos
     */
    public static void makeSolidVerticalPlane(Vector2 startPos, Vector2 endPos){

        Vector2 v1 = new Vector2(startPos.x + MAX_DIS_STEP, startPos.y) ;
        Vector2 v2 =new Vector2(endPos.x + MAX_DIS_STEP, endPos.y);

        Obstacle tmp = new Obstacle();
        tmp.addVertex(startPos);
        tmp.addVertex(endPos);
        tmp.addVertex(v1);
        tmp.addVertex(v2);

        Side side1 = new VerticalSide( startPos, endPos);
        tmp.addSide(side1);
        PuttingCourse.getInstance().obstacles.add(tmp);


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
     * @param length length of the face
     * @param width
     */
    public static void makeBox(Vector2d startPos, double length, double width){

    }

}

