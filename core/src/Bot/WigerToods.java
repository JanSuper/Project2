package Bot;

import Model.*;

import java.util.ArrayList;

import com.mygdx.game.PuttingCourse;
import com.mygdx.game.PuttingSimulator;

public class WigerToods {

    private static WigerToods ai=null;
    static Solver solver = new RKSolver();
    public ArrayList<int[]> botSteps;
    static int stepcount;

    /**
     * Before using AI, be sure to set the current solver
     * solver MUST have the right goal position
     * @param s solver
     */
    public void setSolver(Solver s){
        solver = s;
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


            if(Math.abs(testFin.subtract(PuttingCourse.getInstance().get_flag_position()).getX())<PuttingCourse.getInstance().get_hole_tolerance()/10){
                recalibrateX=false;
            }else{
                scalar = distanceToFlag.getX()/shotDistance.getX();
                testVelocity.setX(testVelocity.getX()*Math.cbrt(scalar));
            }
            if(Math.abs(testFin.subtract(PuttingCourse.getInstance().get_flag_position()).getY())<PuttingCourse.getInstance().get_hole_tolerance()/10) {
                recalibrateY = false;
            }else{
                scalar = distanceToFlag.getY()/shotDistance.getY();
                testVelocity.setY(testVelocity.getY()*Math.cbrt(scalar));
            }
        }
        return testVelocity;
    }
    
    public Vector2d mazeSearch() {
    	int[] currentStep = nextStep();
    	PuttingCourse copyCourse = PuttingCourse.getInstance();
    	copyCourse.set_flag_positon(new Vector2d(currentStep[2], currentStep[3]));
    	boolean recalibrateY=true;
        boolean recalibrateX=true;
    	double scalar = 0.9; 
        Vector2d testFin;
        Vector2d testVelocity = (copyCourse.get_flag_position().subtract(PuttingSimulator.getInstance().get_ball_position())).multiplyBy(new Vector2d(.5,.5));
        Vector2d distanceToFlag = PuttingSimulator.getInstance().get_ball_position().absDifference(copyCourse.get_flag_position());
        while(recalibrateX||recalibrateY){
            testFin = solver.takeShot(PuttingSimulator.getInstance().get_ball_position(), testVelocity);
            Vector2d shotDistance = PuttingSimulator.getInstance().get_ball_position().absDifference(testFin);


            if(Math.abs(testFin.subtract(copyCourse.get_flag_position()).getX())<PuttingCourse.getInstance().get_hole_tolerance()/10){
                recalibrateX=false;
            }else{
                scalar = distanceToFlag.getX()/shotDistance.getX();
                testVelocity.setX(testVelocity.getX()*Math.cbrt(scalar));
            }
            if(Math.abs(testFin.subtract(copyCourse.get_flag_position()).getY())<PuttingCourse.getInstance().get_hole_tolerance()/10) {
                recalibrateY = false;
            }else{
                scalar = distanceToFlag.getY()/shotDistance.getY();
                testVelocity.setY(testVelocity.getY()*Math.cbrt(scalar));
            }
        }
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
