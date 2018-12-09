import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import processing.core.*;

public final class VirtualWorld
   extends PApplet
{
   public static final int TIMER_ACTION_PERIOD = 100;

   public static final int VIEW_WIDTH = 640;
   public static final int VIEW_HEIGHT = 480;
   public static final int TILE_WIDTH = 32;
   public static final int TILE_HEIGHT = 32;
   public static final int WORLD_WIDTH_SCALE = 2;
   public static final int WORLD_HEIGHT_SCALE = 2;

   public static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
   public static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
   public static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
   public static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

   public static final String IMAGE_LIST_FILE_NAME = "imagelist";
   public static final String DEFAULT_IMAGE_NAME = "background_default";
   public static final int DEFAULT_IMAGE_COLOR = 0x808080;

   public static final String LOAD_FILE_NAME = "gaia.sav";

   public static final String FAST_FLAG = "-fast";
   public static final String FASTER_FLAG = "-faster";
   public static final String FASTEST_FLAG = "-fastest";
   public static final double FAST_SCALE = 0.5;
   public static final double FASTER_SCALE = 0.25;
   public static final double FASTEST_SCALE = 0.10;

   public int clickCount = 0;

   public static double timeScale = 1.0;

   public ImageStore imageStore;
   public WorldModel world;
   public WorldView view;
   public EventScheduler scheduler;

   public long next_time;

   public void settings()
   {
      size(VIEW_WIDTH, VIEW_HEIGHT);
   }

   /*
      Processing entry point for "sketch" setup.
   */
   public void setup()
   {
      this.imageStore = new ImageStore(
         createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
      this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
         createDefaultBackground(imageStore));
      this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
         TILE_WIDTH, TILE_HEIGHT);
      this.scheduler = new EventScheduler(timeScale);

      loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
      loadWorld(world, LOAD_FILE_NAME, imageStore);

      scheduleActions(world, scheduler, imageStore);

      next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
   }

   public void draw()
   {
      long time = System.currentTimeMillis();
      if (time >= next_time)
      {
         scheduler.updateOnTime( time);
         next_time = time + TIMER_ACTION_PERIOD;
      }

      view.drawViewport();
   }

   public void keyPressed()
   {
      if (key == CODED)
      {
         int dx = 0;
         int dy = 0;

         switch (keyCode)
         {
            case UP:
               dy = -1;
               break;
            case DOWN:
               dy = 1;
               break;
            case LEFT:
               dx = -1;
               break;
            case RIGHT:
               dx = 1;
               break;
         }
         view.shiftView( dx, dy);
      }
   }

   public static Background createDefaultBackground(ImageStore imageStore)
   {
      return new Background(DEFAULT_IMAGE_NAME,
         imageStore.getImageList( DEFAULT_IMAGE_NAME));
   }

   public static PImage createImageColored(int width, int height, int color)
   {
      PImage img = new PImage(width, height, RGB);
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         img.pixels[i] = color;
      }
      img.updatePixels();
      return img;
   }

   private static void loadImages(String filename, ImageStore imageStore,
      PApplet screen)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
       imageStore.loadImages(in, screen);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   public static void loadWorld(WorldModel world, String filename,
      ImageStore imageStore)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         world.load(in, imageStore);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   public static void scheduleActions(WorldModel world,
      EventScheduler scheduler, ImageStore imageStore)
   {        //changed to ActiveEntity
      for (Entity entity : world.getEntities())
      {  if (entity instanceof ActiveEntity)
         ((ActiveEntity)entity).scheduleActions(scheduler, world, imageStore);
      }
   }

   public static void parseCommandLine(String [] args)
   {
      for (String arg : args)
      {
         switch (arg)
         {
            case FAST_FLAG:
               timeScale = Math.min(FAST_SCALE, timeScale);
               break;
            case FASTER_FLAG:
               timeScale = Math.min(FASTER_SCALE, timeScale);
               break;
            case FASTEST_FLAG:
               timeScale = Math.min(FASTEST_SCALE, timeScale);
               break;
         }
      }
   }

   public void mouseClicked(){
//      System.out.println(mouseX);
//      System.out.println(mouseY);


      Point pt = mouseToPoint(mouseY, mouseX);
      Background none = new Background("none", imageStore.getImageList("space"));

      if (world.isOccupied(pt)) {
//         System.out.println(world.getOccupancyCell(pt));
         scheduler.unscheduleAllEvents(world.getOccupancyCell(pt));
         world.removeEntity(world.getOccupancyCell(pt));
      }

      MinerNotFull miner = new MinerNotFull("miner", pt, imageStore.getImageList("miner"), 4, 0, 5, 6);
//      world.tryAddEntity(miner);
//      miner.scheduleActions( scheduler, world, imageStore);







//      world.setBackgroundCell(pt, none);
      // double for loop through every point in the world (tilewidth and tileheight, if the point is 7 manhattan distance away from click
      for (int i = 0; i < world.getNumRows(); i++){
         for (int j = 0; j < world.getNumCols(); j++){
            Point newPoint = new Point(j, i);


            if (manhattan(newPoint, pt) < 5) {
               world.setBackgroundCell(newPoint, none);
               if (world.isOccupied(newPoint)){
                  if(world.getOccupancyCell(newPoint).getClass().equals(MinerNotFull.class) ||
                     world.getOccupancyCell(newPoint).getClass().equals(MinerFull.class)){
                     System.out.println(world.getOccupancyCell(newPoint).getClass());
                     world.removeEntity(world.getOccupancyCell(newPoint));
                     scheduler.unscheduleAllEvents(world.getOccupancyCell(pt));

                     SmashBall ball2 = new SmashBall(newPoint, imageStore.getImageList("ball"), 0, 20);
                     world.tryAddEntity(ball2);
                     ball2.scheduleActions(scheduler, world, imageStore);
                  }
               }
            }
         }
      }

      SmashBall ball = new SmashBall(pt, imageStore.getImageList("ball"), 0, 20);
      world.tryAddEntity(ball);
      ball.scheduleActions(scheduler, world, imageStore);

      //when u remove an entity, u have to unschedule to events
   }

   public int manhattan(Point a, Point b){
      int distance = Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
      return distance;
   }


   public Point mouseToPoint(int x, int y){
      return view.getViewport().viewportToWorld(y/view.getTileHeight(), x/view.getTileWidth());
   }

      

   public static void main(String [] args)
   {
      parseCommandLine(args);
      PApplet.main(VirtualWorld.class);
   }
}
