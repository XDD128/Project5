import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class SmashBall extends AbstractDestroyer
{


    private int health;


    public SmashBall ( Point position,
                       List<PImage> images,
                       int actionPeriod, int animationPeriod)
    {

        super(position,images,actionPeriod, animationPeriod);
        this.strategy = new RandomPathingStrategy();
        this.SELECTED_NEIGHBORS = PathingStrategy.DIAGONAL_CARDINAL_NEIGHBORS;
        this.health = 10;

    }





    public int getHealth()
    {
        return health;
    }
    public void damage(int amount)
    {
        health -= amount;

    }

    protected Optional<Entity> getNearestTarget(WorldModel world)
    {
        return world.findNearestOtherThan(getPosition(), Ore.class);
    }

}