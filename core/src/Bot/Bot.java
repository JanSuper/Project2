package Bot;

import Model.Solver;
import Model.Vector2d;

public interface Bot {
    public Vector2d search();
    public void setSolver(Solver x);
}
