package byow.Core;

import byow.TileEngine.TETile;

import java.io.Serializable;

public class World implements Serializable {
    private TETile[][] world;
    //private Point position;
    private Point avatar;

    public World(TETile[][] world) {
        this.world = world;
    }

    public World(TETile[][] world, Point avatar) {
        this.world = world;
        this.avatar = avatar;
    }

    public TETile[][] getWorld() {
        return this.world;
    }


    public Point getAvatar() {
        return avatar;

    }

    public void setAvatar(Point avatar) {
        this.avatar = avatar;
    }

    public void setWorld(TETile[][] world) {
        this.world = world;
    }

}
