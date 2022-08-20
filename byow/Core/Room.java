package byow.Core;

public class Room {
    private Point bottomLeft;
    private Point topRight;
    private boolean connected;

    public Room(Point bottomLeft, Point topRight, boolean connected) {
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
        this.connected = connected;
    }

    public Point getPositionBL() {
        return bottomLeft;
    }

    public Point getPositionTR() {
        return topRight;
    }
}
