import processing.core.PImage;

import java.util.List;

public class BallHit extends AbstractAnimatedEntity
{


    private int health;
    public BallHit( Point position,
                  List<PImage> images,
                  int actionPeriod, int animationPeriod, int health)
    {

        super(position, images, actionPeriod, animationPeriod);
        this.health = health;
    }



    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler){
        SmashBall weakenedSmashBall = new SmashBall(getPosition(), imageStore.getImageList("ball"), 100, 20, health);

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(weakenedSmashBall);
        weakenedSmashBall.scheduleActions( scheduler, world, imageStore);
    }



}