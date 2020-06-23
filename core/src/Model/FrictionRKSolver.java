package Model;

import com.mygdx.game.PuttingCourse;
import static java.lang.Math.sqrt;

public class FrictionRKSolver extends RKSolver {
    double estimatedFriction;

    public FrictionRKSolver(double estimatedFriction){
        this.estimatedFriction = estimatedFriction;
    }

    @Override
    protected Vector2d getNextAcceleration(Vector2d position, Vector2d velocity) {
        Vector2d slopes = PuttingCourse.getInstance().get_height().gradient(position);
        double velY = velocity.getY();
        double velX = velocity.getX();
        double sqrtSpeeds = sqrt((velX * velX) + (velY * velY));
        return new Vector2d( (-1)*(g*slopes.getX()) - (estimatedFriction*g*(velX/ sqrtSpeeds)),
                (-1)*(g*slopes.getY()) - (estimatedFriction*g*(velY/ sqrtSpeeds)));
    }

}
