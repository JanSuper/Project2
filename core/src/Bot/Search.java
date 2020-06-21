package Bot;

import Model.PhysicsEngine;
import Model.Vector2d;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.mygdx.game.PuttingCourse;
import java.util.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import static Bot.MazeGenerator.BLOCK_SIZE;

public class Search {
    private PuttingCourse course;
    private PhysicsEngine engine;
    private double radius;

    /**
     * Constructor
     * @param course terrain on which we need to do the research
     * @param engine phyisics engine to simulate shots
     */
    public Search(PuttingCourse course, PhysicsEngine engine) {
        this.course = course;
        this.engine = engine;
    }

    public Node search(){
        Node start = new Node(course.get_start_position(), course.get_flag_position());
        LinkedList<Node> stack = new LinkedList<>();
        LinkedList<Node> explored = new LinkedList<>();
        stack.add(start);

        return start;
    }

    private double distance(Vector2d n, Vector2d p){
        double x = (n.getX()-p.getX()), y = (n.getY()-p.getY());
        return Math.sqrt(x*x+y*y);
    }

    public static void main(String[] args){
        MazeGenerator.createMaze(new ModelBuilder());
//        System.out.println(MazeGenerator.getInstance().mazeBlocks.getBotSteps());
//        MazeGenerator.getInstance().mazeBlocks.getBotSteps();
//
//        MazeGenerator.getInstance().mazeBlocks.printSteps();
        Vector2d start = new Vector2d( BLOCK_SIZE + 1,  BLOCK_SIZE + 1);
        ArrayList<BlockInfo> steps = MazeGenerator.getInstance().mazeBlocks.getSteps(start, new Vector2d(15 * BLOCK_SIZE + 1, 15*BLOCK_SIZE + 1));
        ArrayList<int[]> botsteps = MazeGenerator.getInstance().mazeBlocks.getBotSteps();//get the solution of the DFS

        /**
         * here is the method that will build the array with the score
         */

        int score =0;
        int[][] pathScore= //array that holds the score of each position in the maze
                new int[MazeGenerator.getInstance().mazeBlocks.maze.length][MazeGenerator.getInstance().mazeBlocks.maze[0].length];

        for(int[] s : botsteps) {
            if(s[0]==s[2]) {//if the step are vertical
                if(s[1]<s[3]) {//if it's going down
                    for (int i = s[1]; i <= s[3]; i++) if (pathScore[s[0]][i] == 0) pathScore[s[0]][i] = score++;
                }else{//if it's going up
                    for (int i = s[1]; i >= s[3]; i--) if (pathScore[s[0]][i] == 0) pathScore[s[0]][i] = score++;
                }
            }else if(s[1]==s[3]){//if the steps are horizontal
                if(s[0]<s[2]) {//if it's going right
                    for (int i = s[0]; i <= s[2]; i++) if (pathScore[i][s[1]] == 0) pathScore[i][s[1]] = score++;
                }else{//if it's going left
                    for (int i = s[0]; i >= s[2]; i--) if (pathScore[i][s[1]] == 0) pathScore[i][s[1]] = score++;
                }
            }
        }

        /**
         * parameters:
         * Vector2d currentPosition
         * Here in the test, i find the coordinates of the score which would be 12 with a bit of randomness
         */
        Random r= new Random();
        Vector2d currentPosition=new Vector2d(0,0);
        for (int i = 0; i < pathScore.length; i++) {
            for (int j = 0; j < pathScore[0].length; j++) {
                if(pathScore[i][j]==12) currentPosition=new Vector2d(i*BLOCK_SIZE+1+(r.nextDouble()-0.5)*BLOCK_SIZE, j*BLOCK_SIZE+1+(r.nextDouble()-0.5)*BLOCK_SIZE);
            }
        }
        System.out.println(currentPosition);
        //return
        System.out.println("answer:"+pathScore[(int)currentPosition.getX()/BLOCK_SIZE][(int)currentPosition.getY()/BLOCK_SIZE]);

        for(int[] arr : pathScore) System.out.println(Arrays.toString(arr));

    }
}

