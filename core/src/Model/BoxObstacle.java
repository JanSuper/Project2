package Model;

import Model.Vector2d;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Intersector;

public class BoxObstacle implements Obstacle {
    Intersector;

    // WARNING !! xyz is not the same as in libgdx (in libgdx y is the height!!)
    private Vector2d position; //position of the CG before rotation
    private double posZ;//position of the CG before rotation
    private Vector2d size;// x size and y size (separate, not the vector from center to edge x 2 !!!!)
    private double height; // height (z)
    private double angle; //angle of rotation -> the rotation is the last thing performed on obstacle (rotation around (0,0,0) coordinate of the obstacle )
    //in degree

    public BoxObstacle(Vector2d position,double z,Vector2d size,double height,double angle){
        this.position = position;
        this.posZ = z;
        this.size=size;
        this.height = height;
        this.angle = angle;
        //computePlanes();?
    }

    public boolean contains(Vector2d position){
        Vector2d temp = position.subtract(this.position);
        //rotate point around the (0,0,0) coordinate
        /*
         *	cos ang		-sin ang	0
         *	sin ang		cos ang 	0
         *	0			0			1
         */
        double angle = this.angle/180*Math.PI*(-1), cos = Math.cos(angle), sin = Math.sin(angle) ;
        temp = temp.multiplyBy(cos, -sin,sin, cos);
        // substact position from pos
        //apply reverse rotation on pos
        if(Math.abs(temp.getX())<=this.size.getX()/2 && Math.abs(temp.getY())<=this.size.getY()/2
                && height <= this.posZ+ this.height && height >=this.posZ) return true;
        return false;
    }

    /**
     * Obstacle needs to implement collision logic
     *The Solver positions/velocities need changing And/Or the game needs to stop/reset
     * Game is not needed as a parameter because of the usage of the singleton creational pattern so we can act directly
     * on the game here if the implementation requires
     * @param solver
     */
    @Override
    public void collide(Solver solver) {
        //TODO implement
    }

    public ModelInstance getModelInstance(ModelBuilder modelBuilder, Material material){

        ModelInstance instance= new ModelInstance(modelBuilder.createBox((float)size.getX(),(float)height,(float) size.getY(),material, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal));
        instance.transform.setToTranslation((float)this.position.getX(),(float)this.height,(float)this.position.getY());
        instance.transform.rotate(0,1,0,(float)this.angle);
        return instance;
    }

    public Vector2d[] direction(Vector2d prevPos, Vector2d pos, Vector2d velocity){
        //which plane does it touch? either the closest to the pos or?
        // compute the intersection with the plane = w(t+h/2)
        // compute the orthogonal projection onto that plane
        // the new speed is the old velocity - (velocity + orthogonal)* some constant
        return new Vector2d[0];
    }




    //normal 3 points (p,q,r)
    // find v1=pq v2=pr (p-q, p-r)
    // a=(v1y*v2z-v1z*v2y) b=-(v1x*v2z-v1z*v2x) c=(v1x*v2y-v1y*v2x)
}
