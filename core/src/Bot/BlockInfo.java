package Bot;

public class BlockInfo {
	
	boolean start;
	boolean finish=false;
	boolean wall;
	boolean up=false;
	boolean down=false;
	boolean left=false;
	boolean right=false;
	String answer;
	int i;
	int j;
	
	public BlockInfo(int i, int j) {
		this.wall=false;
		this.i = i;
		this.j = j;
	}
	
	public BlockInfo(boolean wall, int i, int j) {
		this.wall = wall;
		this.i = i;
		this.j = j;
	}
	
	public boolean[] getSteplist() {
		boolean[] hold = {up, down, left, right};
		return hold;
	}

}
