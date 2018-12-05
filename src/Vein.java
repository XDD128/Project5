import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Vein extends AbstractActiveEntity
{
    private final Random rand = new Random();
    private final String ORE_KEY = "ore";
    private final String ORE_ID_PREFIX = "ore -- ";
    private final int ORE_CORRUPT_MIN = 20000;
    private final int ORE_CORRUPT_MAX = 30000;


    private String id;


    public Vein( String id, Point position,
                     List<PImage> images, int actionPeriod)
    {
        super(position, images, actionPeriod);
        this.id = id;


    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler){
        Optional<Point> openPt = world.findOpenAround(this.getPosition());

        if (openPt.isPresent())
        {   //Change Entity to ActiveEntity
            Ore ore = openPt.get().createOre(ORE_ID_PREFIX + this.id,
                    ORE_CORRUPT_MIN +
                            rand.nextInt(ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                    imageStore.getImageList( ORE_KEY));
            world.addEntity( ore);
            ore.scheduleActions( scheduler, world, imageStore);
        }

        scheduler.scheduleEvent( this,
                new Activity(this, world, imageStore),
                this.getActionPeriod());
    }


}