package Model;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Vector2d {
    private double x;
    private double y;

    public Vector2d(double x, double y){
        this.x=x;
        this.y=y;
    }
    public double getX(){
        return this.x;
    }
    public double getX2(){
        return x*x;
    }
    public double getY(){
        return this.y;
    }
    public double getY2(){
        return y*y;
    }
    public void setX(double d){
        this.x=d;
    }
    public void setY(double d){
        this.y=d;
    }
    public Vector2d cloneAndAdd(double a, double b){return new Vector2d(this.x+a, this.y+b);}
    public Vector2d clone(){
        return new Vector2d(this.x, this.y);
    }
    public void add(double x, double y){this.x+=x;this.y+=y;}
    public double difference(Vector2d vector){
        double xDiff =this.x-vector.getX();
        double yDiff = this.y-vector.getY();
       return Math.sqrt(xDiff*xDiff + yDiff*yDiff);
    }
    public double evaluateVector(){
        return Math.sqrt(x*x+y*y);
    }


    public Vector2d add(Vector2d tmp){
        return new Vector2d(this.x+tmp.getX(), this.y+tmp.getY());
    }
    public Vector2d subtract(Vector2d tmp){
        return new Vector2d(this.x-tmp.getX(),this.y-tmp.getY());
    }
    public Vector2d absDifference(Vector2d tmp){
        return  new Vector2d(Math.abs(this.x-tmp.getX()),Math.abs(this.y-tmp.getY()));
    }
    public Vector2d multiplyBy(Vector2d tmp){
        return  new Vector2d(this.x*tmp.getX(),this.y*tmp.getY());
    }
    public Vector2d divideBy(Vector2d tmp){
        return new Vector2d(this.x/tmp.getX(), this.y/tmp.getY());
    }

    public String toString(){
        return "x: "+x+" y: "+y;
    }

    public void scaleDown(double maxSpeed){
        assert maxSpeed>0;
        double norm=evaluateVector();
        x=x/norm*maxSpeed;
        y=y/norm*maxSpeed;
    }
    public static int pointLineSide(Vector2d linePoint1, Vector2d linePoint2, Vector2d point) {
        return (int) Math.signum((linePoint2.getX() - linePoint1.getX()) * (point.getY() - linePoint1.getY()) - (linePoint2.getY() - linePoint1.getY()) * (point.getX() - linePoint1.getX()));
    }
    public static boolean isPointInPolygon(Array<Vector2d> polygon, Vector2d point) {
        Vector2d last = (Vector2d)polygon.peek();
        double x = point.getX();
        double y = point.getY();
        boolean oddNodes = false;

        for(int i = 0; i < polygon.size; ++i) {
            Vector2d vertex = (Vector2d)polygon.get(i);
            if ((vertex.getY() < y && last.getY() >= y || last.getY() < y && vertex.getY() >= y) && vertex.getX() + (y - vertex.getY()) / (last.getY() - vertex.getY()) * (last.getX() - vertex.getX()) < x) {
                oddNodes = !oddNodes;
            }

            last = vertex;
        }

        return oddNodes;
    }

}
