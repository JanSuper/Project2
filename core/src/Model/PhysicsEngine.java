package Model;

public interface PhysicsEngine {
	
	
	public double get_height(double x, double y);
	public void nextStep();
	public void setPosition(Vector2d v);
	public void setPosZ(double d);
	public void setVelocity(Vector2d v);
	public Vector2d getPosition();
	public double getPosZ();
	public Vector2d getVelocity();
	public boolean finish();
	public abstract void set_step_size(double h);

}
