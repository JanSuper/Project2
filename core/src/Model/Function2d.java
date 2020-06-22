package Model;

public interface Function2d {
    public double evaluate(Vector2d p);
    public Vector2d gradient(Vector2d p);
    public void setFunction(String s);
    public void setFunction(String s, boolean DEBUG);
}
