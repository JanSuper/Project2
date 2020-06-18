package Model;

import Model.Sides.Side;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.LinkedList;

public class Obstacle {
    private LinkedList<Side> sides = new LinkedList<Side>();
    private Array<Vector2> polygon = new Array<Vector2>();
    public ModelInstance mi;
    public int i;
    public int j;

    public Array<Vector2> getPolygon() {
        return polygon;
    }

    public void addVertex(Vector2 vertex) {
        polygon.add(vertex);
    }

    public LinkedList<Side> getSides() {
        return sides;
    }

    public void addSide(Side side) {
        sides.add(side);
    }

    public String toString(){
        String res="i: "+i+"-j: "+j+"\n";
        for(Vector2 v: polygon) res+=v.toString()+" ";
        return res;
    }

}
