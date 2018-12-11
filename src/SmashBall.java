import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class SmashBall extends AbstractDestroyer
{
    private final String QUAKE_KEY = "quake";

    private int health;
    private boolean damaged;

    public SmashBall ( Point position,
                       List<PImage> images,
                       int actionPeriod, int animationPeriod)
    {

        super(position,images,actionPeriod, animationPeriod);
        this.strategy = new RandomPathingStrategy();
        this.SELECTED_NEIGHBORS = PathingStrategy.DIAGONAL_CARDINAL_NEIGHBORS;
        this.health = 10;
        this.damaged = false;
    }
    public SmashBall ( Point position,
                       List<PImage> images,
                       int actionPeriod, int animationPeriod, int health)
    {

        super(position,images,actionPeriod, animationPeriod);
        this.strategy = new RandomPathingStrategy();
        this.SELECTED_NEIGHBORS = PathingStrategy.DIAGONAL_CARDINAL_NEIGHBORS;
        this.health = health;
        this.damaged = false;
    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> target = getNearestTarget(world);
        //change above to Vein.class
        long nextPeriod = this.getActionPeriod();
        System.out.println("Health:" + health);
        System.out.println(damaged);

        if (target.isPresent()) {                                   //changed position to .getPosition()
            Point tgtPos = target.get().getPosition();

         //change Entity to ActiveEntity


                Point nextPos = nextPosition( world, target.get().getPosition());

                if (!getPosition().equals(nextPos)) {
                    Optional<Entity> occupant = world.getOccupant(nextPos);
                    if (occupant.isPresent()) {
                        scheduler.unscheduleAllEvents(occupant.get());
                    }

                    world.moveEntity(this, nextPos);
                }

            }





            scheduler.scheduleEvent(this,
                    new Activity(this, world, imageStore),
                    nextPeriod);
        }




    public boolean damaged() {return damaged;}

    public int getHealth()
    {
        return health;
    }
    public void damage(int amount)
    {
        health -= amount;
        damaged = true;
    }
    public Point nextPosition
    protected Optional<Entity> getNearestTarget(WorldModel world)
    {
        return world.findNearestOtherThan(getPosition(), Ore.class);
    }

}