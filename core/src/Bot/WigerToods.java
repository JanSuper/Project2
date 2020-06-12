package Bot;

import Model.*;
import com.mygdx.game.PuttingCourse;

public class WigerToods {

    private static WigerToods ai=null;
    static Solver solver = new RKSolver();

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
        boolean recalibrateY=true;
        boolean recalibrateX=true;
        double scalar = 0.9;
        Vector2d testFin;
        Vector2d testVelocity = (PuttingCourse.getInstance().get_flag_position().subtract(PuttingCourse.getInstance().get_start_position())).multiplyBy(new Vector2d(.5,.5));
        Vector2d distanceToFlag = PuttingCourse.getInstance().get_start_position().absDifference(PuttingCourse.getInstance().get_flag_position());
        while(recalibrateX||recalibrateY){
            testFin = solver.takeShot(PuttingCourse.getInstance().get_start_position(), testVelocity);
            Vector2d shotDistance = PuttingCourse.getInstance().get_start_position().absDifference(testFin);


            if(Math.abs(testFin.subtract(PuttingCourse.getInstance().get_flag_position()).getX())<PuttingCourse.getInstance().get_hole_tolerance()){
                recalibrateX=false;
            }else{
                scalar = distanceToFlag.getX()/shotDistance.getX();
                testVelocity.setX(testVelocity.getX()*Math.cbrt(scalar));
            }
            if(Math.abs(testFin.subtract(PuttingCourse.getInstance().get_flag_position()).getY())<PuttingCourse.getInstance().get_hole_tolerance()) {
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
    public static void main (String [] arg){
        WigerToods blah = new WigerToods();
        Vector2d result = blah.search();
        System.out.print("finish X co-ordinate: "+ result.getX() + "\n finish Y co-ordinate: " +result.getY());
    }

}
