package Bot;

import Model.*;

import java.util.ArrayList;
import java.util.Arrays;

import com.mygdx.game.PuttingCourse;
import com.mygdx.game.PuttingSimulator;

public class WigerToods implements Bot{

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
    }

    /**
     * Search method return the Vector2d of shot to make a hole in one
     * @return Vector2d vector of shot(x and y strength)
     */
    public Vector2d search(){
    	boolean recalibrateY=true;
        boolean recalibrateX=true;
        double scalar = 0.9; 
        Vector2d testFin;
        Vector2d testVelocity = (PuttingCourse.getInstance().get_flag_position().subtract(PuttingSimulator.getInstance().get_ball_position())).multiplyBy(new Vector2d(.5,.5));
        Vector2d distanceToFlag = PuttingSimulator.getInstance().get_ball_position().absDifference(PuttingCourse.getInstance().get_flag_position());
        while(recalibrateX||recalibrateY){
            testFin = solver.takeShot(PuttingSimulator.getInstance().get_ball_position(), testVelocity);
            Vector2d shotDistance = PuttingSimulator.getInstance().get_ball_position().absDifference(testFin);

            if(Math.abs(testFin.subtract(PuttingCourse.getInstance().get_flag_position()).getX())<Math.sqrt(PuttingCourse.getInstance().get_hole_tolerance())){
                recalibrateX=false;
            }else{
                scalar = distanceToFlag.getX()/shotDistance.getX();
                testVelocity.setX(testVelocity.getX()*Math.cbrt(scalar));
            }
            if(Math.abs(testFin.subtract(PuttingCourse.getInstance().get_flag_position()).getY())<Math.sqrt(PuttingCourse.getInstance().get_hole_tolerance())) {
                recalibrateY = false;
            }else{
                scalar = distanceToFlag.getY()/shotDistance.getY();
                testVelocity.setY(testVelocity.getY()*Math.cbrt(scalar));
            }
        }
        return testVelocity;
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
}
