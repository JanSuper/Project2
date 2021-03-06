package Model;

import Model.Sides.AngleSide;
import Model.Sides.HorizontalSide;
import Model.Sides.Side;
import Model.Sides.VerticalSide;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.mygdx.game.PuttingCourse;

/**
 * A class for quickly building objects/obstacles to add to the course
 */
public class ObstacleBuilder {
    private static final float BALL_RADIUS = 0.5f;
    private static final float MAX_DIS_STEP= .01f;

    private static final float OBSTACLE_HEIGHT = 1;

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
    public static ModelInstance makeBox(Vector2d startPos, float length, float width, ModelBuilder mb){
        Vector2d vstartPos = new Vector2d(startPos.getX()-BALL_RADIUS,startPos.getY()-BALL_RADIUS);
        Vector2d v1 = new Vector2d(startPos.getX() + length+BALL_RADIUS, startPos.getY()-BALL_RADIUS);
        Vector2d v2 = new Vector2d(startPos.getX()-BALL_RADIUS, startPos.getY()+width+BALL_RADIUS);
        Vector2d v3 = new Vector2d(startPos.getX()+length+BALL_RADIUS, startPos.getY()+width+BALL_RADIUS);

        Obstacle tmp = new Obstacle();
        tmp.addVertex(vstartPos);
        tmp.addVertex(v1);
        tmp.addVertex(v3);
        tmp.addVertex(v2);

        Side s1 = new VerticalSide(vstartPos, v2);
        Side s2 = new VerticalSide(v1,v3);
        Side s3 = new HorizontalSide(vstartPos, v1);
        Side s4 = new HorizontalSide(v2,v3);

        tmp.addSide(s1);
        tmp.addSide(s2);
        tmp.addSide(s3);
        tmp.addSide(s4);

        PuttingCourse.getInstance().obstacles.add(tmp);

        ModelInstance mi = new ModelInstance(mb.createBox(length,OBSTACLE_HEIGHT, width, new Material(ColorAttribute.createDiffuse(Color.GRAY)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal) );
        mi.transform.setToTranslation((float)startPos.getX()+length/2,0, (float)startPos.getY()+width/2);
        tmp.mi=mi;
        return mi;

    }

    

}

