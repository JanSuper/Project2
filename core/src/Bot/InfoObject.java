package Bot;

import Model.Vector2d;

import java.util.ArrayList;
import java.util.Arrays;

public class InfoObject {
	int i;
	int j;
	static BlockInfo[][] maze;
	ArrayList<BlockInfo> steps;
	ArrayList<String> strings;
	static ArrayList<int[]> botSteps;
	boolean delete = false; // if the prior action was deleting a wrong step
	int lasti;
	int lastj;
	
	
	public InfoObject() {
		maze = new BlockInfo[17][17];
		i = 0;
		j = 0;
		lasti = 1;
		lastj = 1;
	}
	
	public void addTopWall() {
		maze[i][j] = new BlockInfo(true, i, j);
		nextStep();
	}
	
	public void addWall() {
		maze[i][j] = new BlockInfo(true, i, j);
		if (i>0)
		maze[i-1][j].down = false;
		if(j>0)
			maze[i][j-1].right=false;
		
		nextStep();
	}
	
	public void addCorridor() {
		maze[i][j] = new BlockInfo(i, j);
		maze[i-1][j].down = true;
		maze[i][j].up=!maze[i-1][j].wall;
		if(j>0) {
			if(!maze[i][j-1].wall) {
				maze[i][j].left=true;
				maze[i][j-1].right=true;
			}
			else {
				maze[i][j].left=false;
			}
		}
		nextStep();
	}
	
	public ArrayList<BlockInfo> getSteps(Vector2d start, Vector2d finish){
		System.out.println("yohahhahahahahhha");
		steps = new ArrayList();
		strings = new ArrayList();
		botSteps = new ArrayList();
		int mynamejeff = MazeGenerator.getInstance().BLOCK_SIZE;
		BlockInfo hold = maze[(int)(start.getX()-1)/ mynamejeff][(int)(start.getY()-1)/ mynamejeff];
		maze[(int)(finish.getX()-1)/ mynamejeff][(int)(finish.getY()-1)/ mynamejeff].finish = true;
		this.lasti = (int)(start.getX()-1)/ mynamejeff;
		this.lastj = (int)(start.getY()-1)/ mynamejeff;
		steps = recursion(hold);
		System.out.println(steps.size());
		System.out.println(strings.size());
		return steps;
	}
	
	private ArrayList<BlockInfo> recursion(BlockInfo step){
//		System.out.println(step.i + " " + step.j);
		if(step.up) {
//			System.out.println("up");
			if(delete) {
				steps.remove(steps.size()-1);
				strings.remove(strings.size()-1);
				delete = false;
			}
			maze[step.i][step.j].up=false; 					// checking off step
			BlockInfo hold = new BlockInfo(step.i, step.j);	// temporary object
			hold.left=false;								// saving only one step
			hold.down=false;
			hold.right=false;
			hold.up=true;
			steps.add(hold);								// add this place to stepslist
			if (maze[step.i-1][step.j].finish) {			// if the next step is the finish
				steps.add(maze[step.i-1][step.j]);			// add finish to stepslist
				strings.add("up");							// add next step in string form
				return steps;
			}
			else {
				strings.add("up");							// add next step in string form
				maze[step.i-1][step.j].down=false;
				return recursion(maze[step.i-1][step.j]);	// next step
			}
		}
		
		
		else if(step.right) {
			if(delete) {
				steps.remove(steps.size()-1);
				strings.remove(strings.size()-1);
				delete = false;
			}
//			System.out.println("right");
			maze[step.i][step.j].right=false; 				// checking off step
			BlockInfo hold = new BlockInfo(step.i, step.j);	// temporary object
			hold.left=false;								// saving only one step
			hold.down=false;
			hold.up=false;
			hold.right=true;
			steps.add(hold);								// add this place to stepslist
			if (maze[step.i][step.j+1].finish) {			// if the next step is the finish
				steps.add(maze[step.i][step.j+1]);			// add finish to stepslist
				strings.add("right");						// add next step in string form
				return steps;
			}
			else {
				strings.add("right");						// add next step in string form
				maze[step.i][step.j+1].left=false;
				return recursion(maze[step.i][step.j+1]);	// next step
			}
		}
		
		else if(step.down) {
			if(delete) {
				steps.remove(steps.size()-1);
				strings.remove(strings.size()-1);
				delete = false;
			}
//			System.out.println("down");
			maze[step.i][step.j].down=false; 				// checking off step
			BlockInfo hold = new BlockInfo(step.i, step.j);	// temporary object
			hold.left=false;								// saving only one step
			hold.down=true;
			hold.up=false;
			hold.right=false;
			steps.add(hold);								// add this place to stepslist
			if (maze[step.i+1][step.j].finish) {			// if the next step is the finish
				steps.add(maze[step.i+1][step.j]);			// add finish to stepslist
				strings.add("down");						// add next step in string form
				return steps;
			}
			else {
				strings.add("down");						// add next step in string form
				maze[step.i+1][step.j].up=false;
				return recursion(maze[step.i+1][step.j]);	// next step
			}
		}
		
		else if(step.left) { 
			if(delete) {
				steps.remove(steps.size()-1);
				strings.remove(strings.size()-1);
				delete = false;
			}
//			System.out.println("left");
			maze[step.i][step.j].left=false; 				// checking off step
			BlockInfo hold = new BlockInfo(step.i, step.j);	// temporary object
			hold.left=true;								// saving only one step
			hold.down=false;
			hold.up=false;
			hold.right=false;
			steps.add(hold);								// add this place to stepslist
			if (maze[step.i][step.j-1].finish) {			// if the next step is the finish
				steps.add(maze[step.i][step.j-1]);			// add finish to stepslist
				strings.add("left");						// add next step in string form
				return steps;
			}
			else {
				strings.add("left");
				maze[step.i][step.j-1].right=false;// add next step in string form
				return recursion(maze[step.i][step.j-1]);	// next step
			}
		}
//		System.out.println("nope");
		delete = true;
		steps.remove(steps.size()-1);
		strings.remove(strings.size()-1);
		BlockInfo hold = steps.get(steps.size()-1);
		return recursion(maze[hold.i][hold.j]);
	}
	
	public void printSteps() {
		for (int i = 0; i <= strings.size()-1; i++) {
			System.out.println(strings.get(i));
		}
		for (int i = 0; i <= botSteps.size()-1; i++) {
			System.out.println(Arrays.toString(botSteps.get(i)));
		}
	}
	
	private void nextStep() {
		if(j<maze[0].length - 1) {
			j++;
		}
		else {
			j = 0;
			i++;
		}
	}
	
	public ArrayList<int[]> getBotSteps(){
		if(botSteps.size() != 0) {
			return botSteps;
		}
		int maxTravel = (int)WigerToods.getInstance().maxDistance / 3 - 1;
		boolean tooFar = false;
		boolean startOver = false;
		int count = 0;
		boolean newStep = true;
		boolean[] hold = {};
		for(int i = 0; i <= steps.size()-2; i++) {
			if ((newStep && !tooFar)|| startOver) {
				//System.out.println("new");

				hold = steps.get(i).getSteplist();
				count = 1;
				newStep = false;
				startOver = false;
				
			}

			if (Arrays.equals(hold, steps.get(i+1).getSteplist())) {
		//		System.out.println("same");
				count++;
				if (count >= maxTravel) {
					tooFar = true;
				}
			}
			else {
				if(tooFar) startOver = true;
				if (hold[0]) {
					int[] holdStep = {lasti,lastj,lasti-count,lastj};
					lasti-=count;
					botSteps.add(holdStep);
				}
				else if (hold[1]) {
					int[] holdStep = {lasti,lastj,lasti+count,lastj};
					lasti+=count;
					botSteps.add(holdStep);
				}
				else if (hold[2]) {
					int[] holdStep = {lasti,lastj,lasti,lastj-count};
					lastj-=count;
					botSteps.add(holdStep);
				}
				else {
					int[] holdStep = {lasti,lastj,lasti,lastj+count};
					lastj+=count;
					botSteps.add(holdStep);
				}
				newStep = true;
				tooFar = false;
			}	
		}
		return botSteps;
		
	}
}
