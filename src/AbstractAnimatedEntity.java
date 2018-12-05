import processing.core.PImage;

import java.util.List;

public abstract class AbstractAnimatedEntity extends AbstractActiveEntity implements AnimatedEntity, ActiveEntity{

    private int animationPeriod;
    public AbstractAnimatedEntity(Point position,
                                   List<PImage> images,
                                   int actionPeriod, int animationPeriod)
    {

        super(position,images,actionPeriod);
        this.animationPeriod = animationPeriod;
    }
    public int getAnimationPeriod()
    {
        return animationPeriod;
    }

    public void scheduleActions( EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        super.scheduleActions(scheduler, world, imageStore);
        scheduler.scheduleEvent(this, new Animation(this,0),
                getAnimationPeriod());
    }
    public void nextImage()
    {setImageIndex((getImageIndex() + 1) % getImages().size());}
}