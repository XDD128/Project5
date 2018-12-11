import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class OreBlob extends AbstractDestroyer
{
    private final String QUAKE_KEY = "quake";



    public OreBlob ( Point position,
                List<PImage> images,
                int actionPeriod, int animationPeriod)
    {

        super(position,images,actionPeriod, animationPeriod);

    }


    protected Optional<Entity> getNearestTarget(WorldModel world)
    {
        return world.findNearest(
                this.getPosition(), Vein.class);
    }


}