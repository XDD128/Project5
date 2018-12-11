import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Master extends AbstractDestroyer
{




    public Master ( Point position,
                       List<PImage> images,
                       int actionPeriod, int animationPeriod)
    {

        super(position,images,actionPeriod, animationPeriod);
        this.SELECTED_NEIGHBORS = PathingStrategy.DIAGONAL_CARDINAL_NEIGHBORS;
    }










    protected Optional<Entity> getNearestTarget(WorldModel world)
    {
        return world.findNearestOtherThan(this.getPosition(), getClass());
    }

}