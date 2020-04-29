package Model;

public interface PhysicsEngine {
	
	
	public double get_height(double x, double y);
	public void nextStep();
	

	public void setPosition(Vector2d v);
	public void setPosZ(double d);
	

	public void setVelocity(Vector2d v);
	
	public void setMu(double mu);
	public void setVMax (double vMax);
	

	public Vector2d getPosition();
	public double getPosZ();
	
	public Vector2d getVelocity();
	
	public void setGoalPosition (Vector2d newGoal);
	public boolean finish();
}
