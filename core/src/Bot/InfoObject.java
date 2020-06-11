package Bot;

import java.util.ArrayList;

public class InfoObject {
	int i;
	int j;
	BlockInfo[][] maze;
	ArrayList<BlockInfo> steps;
	ArrayList<String> strings;
	
	public InfoObject() {
		maze = new BlockInfo[17][17];
		steps = new ArrayList();
		strings = new ArrayList();
		i = 0;
		j = 0;
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
	
	public ArrayList<BlockInfo> getSteps(){
		BlockInfo hold = maze[1][1];
		maze[15][15].finish = true;
		steps = recursion(hold);
		
		return steps;
	}
	
	private ArrayList<BlockInfo> recursion(BlockInfo step){
//		System.out.println(step.i + " " + step.j);
		if(step.up) {
//			System.out.println("up");
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
		steps.remove(steps.size()-1);
		strings.remove(strings.size()-1);
		BlockInfo hold = steps.get(steps.size()-1);
		return recursion(maze[hold.i][hold.j]);
	}
	
	public void printSteps() {
		for (int i = 0; i <= strings.size()-1; i++) {
			System.out.println(strings.get(i));
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

}
