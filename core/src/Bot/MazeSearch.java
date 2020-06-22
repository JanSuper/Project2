package Bot;

import Model.RKSolver;
import Model.Solver;
import Model.Vector2d;
import com.mygdx.game.PuttingCourse;
import com.mygdx.game.PuttingSimulator;

import java.util.ArrayList;
import java.util.Arrays;

public class MazeSearch implements Bot {

    private Solver solver = new RKSolver();
    public ArrayList<int[]> botSteps;
    public static int stepcount;
    public double maxDistance;
    private static MazeSearch singleton = null;

    private MazeSearch(){

    }

    public static MazeSearch getInstance(){
        if(singleton ==null){
            singleton = new MazeSearch();
        }
        return singleton;
    }

    public boolean testX(Vector2d testFin, Vector2d flag) {
        return testFin.absDifference(flag).getX()<Math.sqrt(PuttingCourse.getInstance().get_hole_tolerance());
    }

    public boolean testY(Vector2d testFin, Vector2d flag) {
        return testFin.absDifference(flag).getY()<Math.sqrt(PuttingCourse.getInstance().get_hole_tolerance());

    }
    @Override
    public Vector2d search() {

        PuttingCourse.getInstance().botUse = true;
        int[] currentStep = nextStep();
        System.out.println(PuttingSimulator.getInstance().get_ball_position().toString());
        System.out.println(Arrays.toString(currentStep));
        Vector2d currenStep = PuttingSimulator.getInstance().get_ball_position();
        
        Vector2d flag;
        if (!MazeGenerator.getInstance().mazeBlocks.maze[currentStep[2]][currentStep[3]].finish) {
        	flag = new Vector2d(currentStep[2]*MazeGenerator.BLOCK_SIZE + 1 + .5f, currentStep[3]*MazeGenerator.BLOCK_SIZE + 1 + .5f);
        }
        else {
        	flag = PuttingCourse.getInstance().get_flag_position();
        }
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
        if(distanceToFlag.evaluateVector() > maxDistance) {
            System.out.println("too far");
            stepcount--;
            return (flag.subtract(currenStep)).multiplyBy(new Vector2d(15,15));
        }

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
                recalibrateX=false;
            }else if (!otherwayX){
                recalibrateX=true;
                scalar = distanceToFlag.getX()/shotDistance.getX();
                testVelocity.setX(testVelocity.getX()*Math.cbrt(scalar));
                lastX = testFin.absDifference(flag).getX();
            }
            else {
                recalibrateX=true;
                scalar = distanceToFlag.getX()/(shotDistance.getX() + 2 * Math.abs(distanceToFlag.getX()-shotDistance.getX()));
                testVelocity.setX(testVelocity.getX()*Math.cbrt(scalar));
                lastX = testFin.absDifference(flag).getX();
            }

            if(testY(testFin, flag)) {
                recalibrateY = false;
            }else if(!otherwayY){
                recalibrateY = true;
                scalar = distanceToFlag.getY()/shotDistance.getY();
                testVelocity.setY(testVelocity.getY()*Math.cbrt(scalar));
                lastY = testFin.absDifference(flag).getY();
            }
            else {
                recalibrateY = false;
                scalar = distanceToFlag.getY()/(shotDistance.getY() + 2 * Math.abs(distanceToFlag.getY()-shotDistance.getY()));
                testVelocity.setY(testVelocity.getY()*Math.cbrt(scalar));
                lastY = testFin.absDifference(flag).getY();
            }

            if(!recalibrateX&&!recalibrateY) {
                double holdx = flag.getX() - testFin.getX();
                double holdy = flag.getY() - testFin.getY();
//                System.out.println("x; " + holdx + " y; " + holdy);
            }
        }
//        PuttingCourse.getInstance().botUse = false;
        return testVelocity;
    }
    public int[] nextStep() {
        return botSteps.get(stepcount++);
    }

    @Override
    public void setSolver(Solver x) {
    	solver = x;
    	 maxDistance = PuttingSimulator.getInstance().get_ball_position().subtract(solver.takeShot(PuttingSimulator.getInstance().get_ball_position(), new Vector2d(15, 0))).evaluateVector();
    }
}
