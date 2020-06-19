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
import com.badlogic.gdx.math.Vector2;
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
    public static ModelInstance makeBox(Vector2 startPos, float length, float width, ModelBuilder mb){
        Vector2 vstartPos = new Vector2(startPos.x-BALL_RADIUS,startPos.y-BALL_RADIUS);
        Vector2 v1 = new Vector2(startPos.x + length+BALL_RADIUS, startPos.y-BALL_RADIUS);
        Vector2 v2 = new Vector2(startPos.x-BALL_RADIUS, startPos.y+width+BALL_RADIUS);
        Vector2 v3 = new Vector2(startPos.x+length+BALL_RADIUS, startPos.y+width+BALL_RADIUS);

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
        mi.transform.setToTranslation(startPos.x+length/2,0, startPos.y+width/2);
        tmp.mi=mi;
        return mi;

    }

    /**
     * Creates the Model for rendering a Box obstacle
     * Creates the Box Object with collision logic implemented
     * @param centerGrav Center of gravity
     * @param length length of the face, always along x axis
     * @param width length of the side along the y axis
     * @param angle angle of rotation around CG
     * @param mb modelBuilder
     * @return model instance to render
     */
    public static ModelInstance makeTiltedBox(Vector2 centerGrav, float length, float width,float angle, ModelBuilder mb){
        Vector2 v1 = new Vector2(-length/2,-width/2).rotate(angle).add(centerGrav);
        Vector2 v2 = new Vector2(length/2,-width/2).rotate(angle).add(centerGrav);
        Vector2 v3 = new Vector2(length/2,width/2).rotate(angle).add(centerGrav);
        Vector2 v4 = new Vector2(-length/2,width/2).rotate(angle).add(centerGrav);

        Obstacle tmp = new Obstacle();
        tmp.addVertex(v1);
        tmp.addVertex(v2);
        tmp.addVertex(v3);
        tmp.addVertex(v4);

        Side s1 = new AngleSide(v1, v2);
        Side s2 = new AngleSide(v2,v3);
        Side s3 = new AngleSide(v3,v4);
        Side s4 = new AngleSide(v4,v1);

        tmp.addSide(s1);
        tmp.addSide(s2);
        tmp.addSide(s3);
        tmp.addSide(s4);

        PuttingCourse.getInstance().obstacles.add(tmp);

        ModelInstance mi = new ModelInstance(mb.createBox(length,OBSTACLE_HEIGHT, width, new Material(ColorAttribute.createDiffuse(Color.GRAY)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal) );
        mi.transform.setToTranslation(centerGrav.x,0, centerGrav.y);
        mi.transform.rotate(0,1,0,angle);
        //for(float f: mi.transform.getValues())System.out.println(f);

        return mi;

    }

}

