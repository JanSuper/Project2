package Bot;

import Model.*;

public class WigerToods {
    Vector2d startPos;
    static Solver solver = new RKSolver(" sin (x) + y ^ 2");
    double tol = 0.02;

    public Vector2d search(){
        Vector2d testFin = startPos.clone();
        Vector2d testVelocity = solver.getGoalPosition().subtract(startPos);
        testFin = solver.takeShot(startPos, testVelocity);

        while((testFin.getX()<solver.getGoalPosition().getX()+tol)||(testFin.getX()>solver.getGoalPosition().getX()-tol)||
                (testFin.getY()<solver.getGoalPosition().getY()+tol)||(testFin.getY()>solver.getGoalPosition().getY()-tol))
        {//difference within a tolerance from the goalPosition
            testVelocity = calibrateVel(testVelocity, testFin);
            testFin= solver.takeShot(startPos, testVelocity);
        }
        return testFin;
    }
    public Vector2d calibrateVel(Vector2d prevVel, Vector2d prevFin){
        // newVel= prevVel+(prevVel*((goal-prevFin)/abs(prevFin-startPos)))
        return   prevVel.add(prevVel.multiplyBy ((solver.getGoalPosition().subtract(prevFin) ).divideBy(  (  solver.getGoalPosition().absDifference(startPos) )  )  )) ;//need to make vector multiply and add methods
    }

    public static void main (String [] arg){
        WigerToods blah = new WigerToods();
        blah.solver.setGoalPosition(new Vector2d(20.420352248333,0));//z should be -1 here the ball just needs to stop at the bottom of a hump
        blah.startPos= new Vector2d(0,0);
        Vector2d result = blah.search();
        System.out.print(result.getX() + " wohoo " +result.getY());
    }
}
