import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class Ore extends AbstractActiveEntity
{


    private final Random rand = new Random();

    private final String BLOB_KEY = "blob";
    private final String BLOB_ID_SUFFIX = " -- blob";
    private final int BLOB_PERIOD_SCALE = 4;
    private final int BLOB_ANIMATION_MIN = 50;
    private final int BLOB_ANIMATION_MAX = 150;



    private String id;
    public Ore( String id, Point position,
                 List<PImage> images,
                 int actionPeriod)
    {
        super(position,images,actionPeriod);
        this.id = id;

    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler){
        Point pos = this.getPosition();  // store current position before removing

        world.removeEntity( this);
        scheduler.unscheduleAllEvents(this);
        //Change below Entity blob to ActiveEntity blob
        Entity blob = pos.createOreBlob(this.id + BLOB_ID_SUFFIX,
                getActionPeriod() / BLOB_PERIOD_SCALE,
                BLOB_ANIMATION_MIN +
                        rand.nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN),
                imageStore.getImageList( BLOB_KEY));

        world.addEntity( blob);

        ((OreBlob)blob).scheduleActions( scheduler, world, imageStore);
    }


    //ACTIVITYSECTIONEND
}