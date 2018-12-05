import processing.core.PImage;

import java.util.List;

public abstract class AbstractEntity implements Entity
{

    private Point position;
    private List<PImage> images;
    private int imageIndex;


    public AbstractEntity(  Point position,
                        List<PImage> images ) {


        this.position = position;
        this.images = images;
        this.imageIndex = 0;

    }

    public Point getPosition() {return position;}
    public void setPosition(Point other) {position = other;}
    public List<PImage> getImages() {return images;}
    public int getImageIndex() {return imageIndex;}
    public void setImageIndex(int i) {this.imageIndex = i;}
    public PImage getCurrentImage() {
        return images.get(imageIndex);
    }

}