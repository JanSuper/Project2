package Model;

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

}
