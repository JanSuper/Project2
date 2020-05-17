package Bot;

import Model.*;
//mynamejeff


public class WigerToods {
    private static WigerToods ai=null;
    Vector2d startPos;
    static Solver solver = new RKSolver(" sin (x) + y ^ 2");
    double tol = 0.02;

    /**
     * Before using AI, be sure to set the current solver
     * @param s solver
     */
    public void setSolver(Solver s){
        solver = s;
    }

    /**
     * mutator method to change the tolerance
     * @param t
     */
    public void setTol(double t){
        tol =t;
    }

    /**
     * Before using solver be sure to set the start position
     * @param pos
     */
    public void setStartPos(Vector2d pos){
        startPos=pos;
    }

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
        // newVel= prevVel+(prevVel*((goal-prevFin)/abs(goal-startPos)))


        return   prevVel.add(prevVel.multiplyBy ((solver.getGoalPosition().subtract(prevFin) ).divideBy(  (  solver.getGoalPosition().absDifference(startPos) )  )  )) ;//need to make vector multiply and add methods
    }

    /**
     * Search method return the Vector2d of shot to make a hole in one
     * @return Vector2d vector of shot(x and y strength)
     */
    public Vector2d search2(){
        boolean recalibrateY=true;
        boolean recalibrateX=true;
        double scaler = 0.9;
        Vector2d testFin = startPos.clone();
        Vector2d testVelocity = (solver.getGoalPosition().subtract(startPos)).multiplyBy(new Vector2d(.5,.5));
        Vector2d distanceToFlag = startPos.absDifference(solver.getGoalPosition());
        while(recalibrateX||recalibrateY){
            testFin = solver.takeShot(startPos, testVelocity);
            Vector2d shotDistance = startPos.absDifference(testFin);


            if(Math.abs(testFin.subtract(solver.getGoalPosition()).getX())<tol){
                recalibrateX=false;
            }else{
                scaler = distanceToFlag.getX()/shotDistance.getX();
                testVelocity.setX(testVelocity.getX()*Math.cbrt(scaler));
//                if(shotDistance.getX()>distanceToFlag.getX()){//Overshoot
//                    testVelocity.setX(testVelocity.getX()*scaler);
//                }else{
//                    testVelocity.setX(testVelocity.getX()/scaler);
//                }
            }
            if(Math.abs(testFin.subtract(solver.getGoalPosition()).getY())<tol) {
                recalibrateY = false;
            }else{
                scaler = distanceToFlag.getY()/shotDistance.getY();
                testVelocity.setY(testVelocity.getY()*Math.cbrt(scaler));
//                if(shotDistance.getY()>distanceToFlag.getY()){//Overshoot
//                    testVelocity.setY(testVelocity.getY()*scaler);
//                }else{
//                    testVelocity.setY(testVelocity.getY()/scaler);
//                }
            }

        }


        return testVelocity;
    }

    /**
     * Singleton
     * @return return the AI
     */
    public static WigerToods get(){
        if (ai==null) ai=new WigerToods();
        return ai;
    }

/*
    public static void main (String [] arg){
//        System.out.print(" wohoo " );

        WigerToods blah = new WigerToods();
        blah.solver.setGoalPosition(new Vector2d((Math.PI*9.0)/2.0,0));//z should be -1 here the ball just needs to stop at the bottom of a hump
        blah.startPos= new Vector2d(0.0,0.0);
        Vector2d result = blah.search2();
//        System.out.print(result.getX() + " wohoo " +result.getY());
    }
*/
}
