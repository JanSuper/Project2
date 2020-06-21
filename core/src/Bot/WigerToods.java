package Bot;

import Model.*;

import java.util.ArrayList;
import java.util.Arrays;

import com.mygdx.game.PuttingCourse;
import com.mygdx.game.PuttingSimulator;

public class WigerToods {

    private static WigerToods ai=null;
    static Solver solver = new RKSolver();
    public ArrayList<int[]> botSteps;
    public static int stepcount;
    public double maxDistance;

    /**
     * Before using AI, be sure to set the current solver
     * solver MUST have the right goal position
     * @param s solver
     */
    public void setSolver(Solver s){
        solver = s;
        maxDistance = PuttingSimulator.getInstance().get_ball_position().subtract(solver.takeShot(PuttingSimulator.getInstance().get_ball_position(), new Vector2d(15, 0))).evaluateVector();
        System.out.println(maxDistance);
    }

    /**
     * Search method return the Vector2d of shot to make a hole in one
     * @return Vector2d vector of shot(x and y strength)
     */
    public Vector2d search(){
    	// TODO new method allowing for multiple shots
    	boolean recalibrateY=true;
        boolean recalibrateX=true;
        double scalar = 0.9; 
        Vector2d testFin;
        Vector2d testVelocity = (PuttingCourse.getInstance().get_flag_position().subtract(PuttingSimulator.getInstance().get_ball_position())).multiplyBy(new Vector2d(.5,.5));
        Vector2d distanceToFlag = PuttingSimulator.getInstance().get_ball_position().absDifference(PuttingCourse.getInstance().get_flag_position());
        while(recalibrateX||recalibrateY){
            testFin = solver.takeShot(PuttingSimulator.getInstance().get_ball_position(), testVelocity);
            Vector2d shotDistance = PuttingSimulator.getInstance().get_ball_position().absDifference(testFin);


            if(Math.abs(testFin.subtract(PuttingCourse.getInstance().get_flag_position()).getX())<PuttingCourse.getInstance().get_hole_tolerance()/100){
                recalibrateX=false;
            }else{
                scalar = distanceToFlag.getX()/shotDistance.getX();
                testVelocity.setX(testVelocity.getX()*Math.cbrt(scalar));
            }
            if(Math.abs(testFin.subtract(PuttingCourse.getInstance().get_flag_position()).getY())<PuttingCourse.getInstance().get_hole_tolerance()/100) {
                recalibrateY = false;
            }else{
                scalar = distanceToFlag.getY()/shotDistance.getY();
                testVelocity.setY(testVelocity.getY()*Math.cbrt(scalar));
            }
        }
        return testVelocity;
    }
    
    public Vector2d mazeSearch() {
    	PuttingCourse.getInstance().botUse = true;
    	int[] currentStep = nextStep();
    	System.out.println(PuttingSimulator.getInstance().get_ball_position().toString());
    	System.out.println(Arrays.toString(currentStep));
    	Vector2d currenStep = new Vector2d(currentStep[0]*MazeGenerator.BLOCK_SIZE + 1 + .5f, currentStep[1]*MazeGenerator.BLOCK_SIZE + 1 + .5f);
    	Vector2d flag = new Vector2d(currentStep[2]*MazeGenerator.BLOCK_SIZE + 1 + .5f, currentStep[3]*MazeGenerator.BLOCK_SIZE + 1 + .5f);
    	boolean recalibrateY=true;
        boolean recalibrateX=true;
        double lastX = 0;
        double lastY = 0;
        boolean otherwayX = false;
        boolean otherwayY = false;
        
    	double scalar = 0.9; 
        Vector2d testFin;
        Vector2d testVelocity = (flag.subtract(currenStep)).multiplyBy(new Vector2d(.25,.25));
        Vector2d distanceToFlag = currenStep.absDifference(flag);
        
        if(distanceToFlag.evaluateVector() > maxDistance)
        	System.out.println("too far");
        
        while(recalibrateX||recalibrateY){
            testFin = solver.takeShot(currenStep, testVelocity);
            Vector2d shotDistance = currenStep.absDifference(testFin);
            
            if ((lastX != 0)&&(Math.abs(lastX) < Math.abs(testFin.absDifference(flag).getX()))) {
            	otherwayX = true;
            } else if (otherwayX && Math.abs(lastX) > Math.abs(testFin.absDifference(flag).getX())){
            	otherwayX = false;
            }
            
            if ((lastY != 0)&&(Math.abs(lastY) < Math.abs(testFin.absDifference(flag).getY()))) {
            	otherwayY = true;
            } else if (otherwayY && Math.abs(lastY) > Math.abs(testFin.absDifference(flag).getY())){
            	otherwayY = false;
            }

            if(testX(testFin, flag)){
            	System.out.println("testX1");
                recalibrateX=false;
            }else if (!otherwayX){
            	System.out.println("testX2");
            	recalibrateX=true;
                scalar = distanceToFlag.getX()/shotDistance.getX();
                testVelocity.setX(testVelocity.getX()*Math.cbrt(scalar));
                lastX = testFin.absDifference(flag).getX();
            }
            else {
            	System.out.println("testX3");
            	recalibrateX=true;
            	scalar = distanceToFlag.getX()/(shotDistance.getX() + 2 * Math.abs(distanceToFlag.getX()-shotDistance.getX()));
                testVelocity.setX(testVelocity.getX()*Math.cbrt(scalar));
                lastX = testFin.absDifference(flag).getX();
            }
            
            if(testY(testFin, flag)) {
            	System.out.println("testY1");
                recalibrateY = false;
            }else if(!otherwayY){
            	System.out.println("testY2");
            	recalibrateY = true;
                scalar = distanceToFlag.getY()/shotDistance.getY();
                testVelocity.setY(testVelocity.getY()*Math.cbrt(scalar));
                lastY = testFin.absDifference(flag).getY();
            }
            else {
            	System.out.println("testY3");
            	recalibrateY = false;
            	scalar = distanceToFlag.getY()/(shotDistance.getY() + 2 * Math.abs(distanceToFlag.getY()-shotDistance.getY()));
                testVelocity.setY(testVelocity.getY()*Math.cbrt(scalar));
                lastY = testFin.absDifference(flag).getY();
            }    
            
            if(!recalibrateX&&!recalibrateY) {
            	double holdx = flag.getX() - testFin.getX();
            	double holdy = flag.getY() - testFin.getY();
            	System.out.println("x; " + holdx + " y; " + holdy);
            }
        }
//        PuttingCourse.getInstance().botUse = false;
        return testVelocity;
    }
    
    public int[] nextStep() {
    	return botSteps.get(stepcount++);
    }

    /**
     * Singleton
     * @return return the AI
     */
    public static WigerToods getInstance(){
        if (ai==null) ai=new WigerToods();
        return ai;
    }
    
    public boolean testX(Vector2d testFin, Vector2d flag) {
    	return testFin.absDifference(flag).getX()<Math.sqrt(PuttingCourse.getInstance().get_hole_tolerance());
    }
    
    public boolean testY(Vector2d testFin, Vector2d flag) {
    	return testFin.absDifference(flag).getY()<Math.sqrt(PuttingCourse.getInstance().get_hole_tolerance());
    	
    }

    /**
     * FOR TESTING PURPOSES
     * @param arg
     */
    public static void main (String [] arg){
        WigerToods blah = new WigerToods();
        Vector2d result = blah.search();
        System.out.print("finish X co-ordinate: "+ result.getX() + "\n finish Y co-ordinate: " +result.getY());
    }

}
