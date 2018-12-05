import processing.core.PImage;

import java.util.List;
import java.util.Optional;

final class Point
{

   private static final int QUAKE_ACTION_PERIOD = 1100;
   private static final int QUAKE_ANIMATION_PERIOD = 100;

   public final int x;
   public final int y;

   public Point(int x, int y)
   {
      this.x = x;
      this.y = y;
   }



   public String toString()
   {
      return "(" + x + "," + y + ")";
   }

   public boolean equals(Object other)
   {
      return other instanceof Point &&
         ((Point)other).x == this.x &&
         ((Point)other).y == this.y;
   }

   public int hashCode()
   {
      int result = 17;
      result = result * 31 + x;
      result = result * 31 + y;
      return result;
   }
   //FIX CREATE METHODS
   public Blacksmith createBlacksmith(String id,
                                         List<PImage> images)
   {
      return new Blacksmith(this, images);
   }

   public MinerFull createMinerFull(String id, int resourceLimit,
                                        int actionPeriod, int animationPeriod,
                                        List<PImage> images)
   {
      return new MinerFull( id, this, images,
              resourceLimit, actionPeriod, animationPeriod);
   }

   public MinerNotFull createMinerNotFull(String id, int resourceLimit,
                                          int actionPeriod, int animationPeriod,
                                           List<PImage> images)
   {
      return new MinerNotFull(id, this, images,
              resourceLimit, 0, actionPeriod, animationPeriod);
   }

   public Obstacle createObstacle(String id,
                                       List<PImage> images)
   {
      return new Obstacle(this, images);
   }

   public Ore createOre(String id,  int actionPeriod,
                                  List<PImage> images)
   {
      return new Ore( id, this, images, actionPeriod);
   }

   public OreBlob createOreBlob(String id,
                                      int actionPeriod, int animationPeriod, List<PImage> images)
   {
      return new OreBlob(this, images, actionPeriod, animationPeriod);
   }

   public Quake createQuake(List<PImage> images)
   {
      return new Quake( this, images, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
   }

   public Vein createVein(String id, int actionPeriod,
                                   List<PImage> images)
   {
      return new Vein( id, this, images, actionPeriod);
   }
   public boolean adjacent( Point p2) {
      return (x == p2.x && Math.abs(y - p2.y) == 1) ||
              (y == p2.y && Math.abs(x - p2.x) == 1);
   }

   public  Optional<Entity> nearestEntity(List<Entity> entities) {
      if (entities.isEmpty()) {
         return Optional.empty();
      } else {
         Entity nearest = entities.get(0);
         int nearestDistance = distanceSquared(nearest.getPosition());

         for (Entity other : entities) {
            int otherDistance = distanceSquared(other.getPosition());

            if (otherDistance < nearestDistance) {
               nearest = other;
               nearestDistance = otherDistance;
            }
         }

         return Optional.of(nearest);
      }
   }

   public  int distanceSquared( Point p2) {
      int deltaX = x - p2.x;
      int deltaY = y - p2.y;

      return deltaX * deltaX + deltaY * deltaY;
   }

}

