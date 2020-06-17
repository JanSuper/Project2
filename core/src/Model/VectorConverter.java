package Model;

import com.badlogic.gdx.math.Vector2;

public class VectorConverter {
    public static Vector2 convert( Vector2d p){
        return new Vector2((float)p.getX(),(float)p.getY());
    }
    public static  Vector2d convert (Vector2 p){
        return new Vector2d(p.x , p.y);
    }
}
