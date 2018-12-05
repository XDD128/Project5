import processing.core.PImage;

import java.util.List;

public abstract class AbstractActiveEntity extends AbstractEntity implements ActiveEntity {
    private int actionPeriod;

    public AbstractActiveEntity(Point position,
                                List<PImage> images, int actionPeriod) {

    super(position, images);
    this.actionPeriod = actionPeriod;

    }
    public int getActionPeriod() {return actionPeriod;}

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                actionPeriod);
    }
    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
}