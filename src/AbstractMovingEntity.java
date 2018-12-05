import processing.core.PImage;

import java.util.List;
import java.util.Optional;
public abstract class AbstractMovingEntity extends AbstractAnimatedEntity implements ActiveEntity, AnimatedEntity {


    public AbstractMovingEntity(Point position,
                                List<PImage> images,
                                int actionPeriod, int animationPeriod) {
        super(position, images, actionPeriod, animationPeriod);

    }
   public boolean moveTo( WorldModel world,
                                   Entity target, EventScheduler scheduler) {
        if (getPosition().adjacent( target.getPosition())) {
            _moveTo(world, target, scheduler);
            return true;
        } else {
            Point nextPos = nextPosition( world, target.getPosition());

            if (!getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }
    public abstract void _moveTo(WorldModel world, Entity target, EventScheduler scheduler);
    public Point nextPosition( WorldModel world,
                               Point destPos) {

        List<Point> points;

        PathingStrategy strategy = new AStarPathingStrategy();
        points = strategy.computePath(getPosition(), destPos,
                p ->  world.withinBounds(p) && getOccupance(world, destPos) && !world.isOccupied(p),
                (p1, p2) -> neighbors(p1,p2),
                PathingStrategy.CARDINAL_NEIGHBORS);

        if (points.size() == 0)
        {

            return getPosition();

        }

        return points.get(0);
        //DIAGONAL_NEIGHBORS);
        //DIAGONAL_CARDINAL_NEIGHBORS);





        /*
        int horiz = Integer.signum(destPos.x - getPosition().x);
        Point newPos = new Point(getPosition().x + horiz,
                getPosition().y);



        if (horiz == 0 ||                                 //occupant.get().getClass() == Ore.Class
                getOccupance(world, newPos)) {
            int vert = Integer.signum(destPos.y - getPosition().y);
            newPos = new Point(getPosition().x, getPosition().y + vert);


            if (vert == 0 ||                                //occupant.get().getClass() == Ore.class
                    getOccupance(world, newPos)) {
                newPos = getPosition();
            }
        }

        return newPos;
        */
    }
    private static boolean neighbors(Point p1, Point p2)
    {
        return p1.x+1 == p2.x && p1.y == p2.y ||
                p1.x-1 == p2.x && p1.y == p2.y ||
                p1.x == p2.x && p1.y+1 == p2.y ||
                p1.x == p2.x && p1.y-1 == p2.y;
    }

    public abstract boolean getOccupance(WorldModel world, Point newPos);
}