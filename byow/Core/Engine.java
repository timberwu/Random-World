package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Random;
import java.util.*;
import java.awt.image.BufferedImage;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private long seed;

    private String inputString = "";
    private Random rand = new Random(100);
    private World finalWorldFrame = new World(new TETile[WIDTH][HEIGHT]);
    private HashMap<Integer, Room> rooms = new HashMap<>();
    private LinkedList<Room> unconnectedRooms = new LinkedList<>();
    private TreeSet<Edge> allEdges = new TreeSet<>();

    private boolean partSight = false;

    private TETile[] avatarList = {Tileset.AVATAR, Tileset.TREE, Tileset.WATER};
    private TETile[] wallList = {Tileset.FLOWER, Tileset.SAND, Tileset.WALL};
    private TETile[] floorList = {Tileset.GRASS, Tileset.MOUNTAIN, Tileset.FLOOR};

    private TETile avatar = Tileset.AVATAR;
    private TETile wall = Tileset.WALL;
    private TETile floor = Tileset.FLOOR;
    private Random randEnv = new Random();

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        ter.initialize(WIDTH, HEIGHT);
        showMenu();
        render();

    }

    private void showMenu() {
        StdDraw.clear();
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.pink);
        StdDraw.text(WIDTH / 2, 2 * HEIGHT / 3, "Game Begins.");

        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 1, "Choose Avatar Appearance(0/1/2)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 3, "Random Environment(E)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 5, "New Game (N)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 7, "Load Game (L)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 9, "Quit Game (:Q)");


        StdDraw.show();
        Font font2 = new Font("Monaco", Font.PLAIN, 15);
        StdDraw.setFont(font2);

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                Character nextKey = StdDraw.nextKeyTyped();
                if (nextKey.equals('n') || nextKey.equals('N')) {
                    String s = getSeed();
                    inputString += s;
                    Point p = generateRandomPoint(WIDTH, HEIGHT);
                    seed = Long.parseLong(s.substring(1, s.length() - 1));
                    System.out.println(seed);
                    generateWorld(seed);
                    render();
                    addAvatar(finalWorldFrame, p);
                    playGame();
                    return;
                } else if (nextKey.equals('l') || nextKey.equals('L')) {
                    ter.initialize(WIDTH, HEIGHT);
                    loadGame();
                    render();
                    playGame();
                    return;

                } else if (nextKey.equals('0')
                        || nextKey.equals('1')
                        || nextKey.equals('2')) {
                    avatar = avatarList[Character.getNumericValue(nextKey)];
                    continue;
                } else if (nextKey.equals('E')
                        || nextKey.equals('e')) {
                    wall = wallList[randEnv.nextInt(wallList.length)];
                    floor = floorList[randEnv.nextInt(floorList.length)];
                    continue;

                } else if (nextKey.equals(':')) {
                    boolean stop = false;
                    while (!stop) {
                        if (StdDraw.hasNextKeyTyped()) {
                            Character nextKeyQ = StdDraw.nextKeyTyped();
                            if (!nextKeyQ.equals('q') && !nextKey.equals('Q')) {
                                continue;
                            } else {
                                System.exit(0);
                                return;

                            }
                        }
                    }
                }
            }
        }
    }


    private void render() {
        if (partSight) {
            ter.renderFrameLight(finalWorldFrame.getWorld(), finalWorldFrame.getAvatar());
        } else {
            ter.renderFrame(finalWorldFrame.getWorld());
        }
    }



    private void playGame() {

        render();

        while (true) {
            mouse(finalWorldFrame);
            if (StdDraw.hasNextKeyTyped()) {
                Character nextKey = StdDraw.nextKeyTyped();

                checkMove(nextKey);
            }
        }

    }



    private void checkMove(Character ch) {
        if (!ch.equals(':')) {
            oneStepMove(ch);
        } else {
            while (true) {
                if (StdDraw.hasNextKeyTyped()) {
                    Character nextKeyQ = StdDraw.nextKeyTyped();
                    if (!nextKeyQ.equals('Q') && !nextKeyQ.equals('q')) {
                        continue;
                    } else {
                        saveGame();
                        System.exit(0);
                        return;
                    }
                }
            }
        }
    }


    private void mouse(World world) {
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();

        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) {
            return;
        }


        if (world.getWorld()[x][y].equals(floor)) {
            render();
            StdDraw.setPenColor(Color.pink);
            StdDraw.text(2, HEIGHT - 1, "FLOOR");
        } else if (world.getWorld()[x][y].equals(Tileset.NOTHING)) {
            render();
            StdDraw.setPenColor(Color.pink);
            StdDraw.text(2, HEIGHT - 1, "NOTHING");
        } else if (world.getWorld()[x][y].equals(avatar)) {
            render();
            StdDraw.setPenColor(Color.pink);
            StdDraw.text(2, HEIGHT - 1, "AVATAR");
        } else {
            render();
            StdDraw.setPenColor(Color.pink);
            StdDraw.text(2, HEIGHT - 1, "WALL");
        }

        StdDraw.show();

    }


    public void showSeed(String string) {
        StdDraw.clear();
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.pink);

        StdDraw.text(WIDTH / 2, 2 * HEIGHT / 3, string);
        StdDraw.show();
        Font newfont = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setFont(newfont);

    }


    private String getSeed() {
        StringBuilder s = new StringBuilder();
        s.append('n');

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                Character nextKey = StdDraw.nextKeyTyped();
                if (!nextKey.equals('s') && !nextKey.equals('S')) {
                    if (nextKey > '9' || nextKey < '0') {
                        throw new IllegalArgumentException();
                    } else {
                        s.append(nextKey);
                        showSeed(s.toString().substring(1));
                    }
                } else {
                    s.append(nextKey);
                    return s.toString();
                }
            }
        }

    }



    private void addAvatar(World world, Point point) {
        while (!world.getWorld()[point.getX()][point.getY()].equals(floor)) {
            point = generateRandomPoint(WIDTH, HEIGHT);
        }
        world.getWorld()[point.getX()][point.getY()] = avatar;
        finalWorldFrame = new World(world.getWorld(), point);
    }

    private Point generateRandomPoint(int boundX, int boundY) {
        return new Point(rand.nextInt(boundX), rand.nextInt(boundY));
    }


    private void saveGame() {
        File f = new File("./saved_game.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(inputString);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadGame() {
        File f = new File("./saved_game.txt");
        String tmpString = "";
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                tmpString = os.readObject().toString();
            } catch (FileNotFoundException e) {
                throw new RuntimeException("file not found");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("class not found");
            }
        }
        TETile[][] lastTile = interactWithInputString(tmpString);
        System.out.println(inputString);
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (lastTile[i][j].equals(avatar)) {
                    finalWorldFrame.setAvatar(new Point(i, j));
                    break;
                }
            }
        }

    }


    private void oneStepMove(Character ch) {
        TETile[][] worldTile = finalWorldFrame.getWorld();
        ch = Character.toUpperCase(ch);

        int x = finalWorldFrame.getAvatar().getX();
        int y = finalWorldFrame.getAvatar().getY();

        switch (ch) {
            case 'W':
                if (y == HEIGHT - 1
                        || worldTile[x][y + 1].equals(wall)
                        || worldTile[x][y + 1].equals(Tileset.NOTHING)) {
                    System.out.println("Invalid Movement");
                } else {
                    worldTile[x][y + 1] = avatar;
                    worldTile[x][y] = floor;
                    finalWorldFrame.setAvatar(new Point(x, y + 1));
                }
                render();
                inputString += ch;
                break;
            case 'S':
                if (y == 0
                        || worldTile[x][y - 1].equals(wall)
                        || worldTile[x][y - 1].equals(Tileset.NOTHING)) {
                    System.out.println("Invalid Movement");
                } else {
                    worldTile[x][y - 1] = avatar;
                    worldTile[x][y] = floor;
                    finalWorldFrame.setAvatar(new Point(x, y - 1));
                }
                render();
                inputString += ch;
                break;
            case 'A':
                if (x == 0
                        || worldTile[x - 1][y].equals(wall)
                        || worldTile[x - 1][y].equals(Tileset.NOTHING)) {
                    System.out.println("Invalid Movement");
                } else {
                    worldTile[x - 1][y] = avatar;
                    worldTile[x][y] = floor;
                    finalWorldFrame.setAvatar(new Point(x - 1, y));
                }
                render();
                inputString += ch;
                break;
            case 'D':
                if (x == WIDTH - 1
                        || worldTile[x + 1][y].equals(wall)
                        || worldTile[x + 1][y].equals(Tileset.NOTHING)) {
                    System.out.println("Invalid Movement");
                } else {
                    worldTile[x + 1][y] = avatar;
                    worldTile[x][y] = floor;
                    finalWorldFrame.setAvatar(new Point(x + 1, y));
                }
                render();
                inputString += ch;
                break;
            case 'P':
                partSight = !partSight;
                render();
                break;
            default:
                break;
        }


    }



    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
//        int len = input.length();
//        if ((input.charAt(0) == 'N' || input.charAt(0) == 'n')
//                && (input.charAt(len - 1) == 'S' || input.charAt(len - 1) == 's')) {
//            input = input.substring(1, len - 2);
//            seed = Long.parseLong(input);
//            return new World(generateWorld(seed)).getWorld();
//        }
//        return null;

        ter.initialize(WIDTH, HEIGHT);


        LinkedList<Character> strQueue = new LinkedList<>();
        for (char c : input.toCharArray()) {
            strQueue.offer(c);
        }


        if (input.charAt(0) == 'N' || input.charAt(0) == 'n') {
            String s = getSeedFromInputString(strQueue);
            inputString += s;
            seed = Long.parseLong(s.substring(1, s.length() - 1));
            Point p = generateRandomPoint(WIDTH, HEIGHT);
            generateWorld(seed);
            addAvatar(finalWorldFrame, p);
            render();

        } else if (input.charAt(0) == 'L' || input.charAt(0) == 'l') {
            loadGame();
            render();
            strQueue.remove();
        }

        playGameWithInputString(strQueue);


        render();

        return finalWorldFrame.getWorld();

    }

    private void playGameWithInputString(LinkedList<Character> strQueue) {
        Character nextKey;
        while (!strQueue.isEmpty()) {
            nextKey = strQueue.remove();
            if (!nextKey.equals(':')) {
                oneStepMove(nextKey);
            } else {
                while (true) {
                    if (!strQueue.isEmpty()) {
                        nextKey = strQueue.remove();
                        if (!nextKey.equals('Q') && !nextKey.equals('q')) {
                            continue;
                        } else {
                            saveGame();
                            return;
                        }
                    }
                }
            }
        }
    }

    private String getSeedFromInputString(LinkedList<Character> strQueue) {
        StringBuilder s = new StringBuilder();
        Character nextKey = strQueue.remove();
        s.append(nextKey);
        while (!nextKey.equals('s') && !nextKey.equals('S')) {
            nextKey = strQueue.remove();
            s.append(nextKey);
        }
        return s.toString();
    }





    private TETile[][] generateWorld(long s) {
        rand = new Random(s);
        // draw background as NOTHING
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                finalWorldFrame.getWorld()[i][j] = Tileset.NOTHING;
            }
        }
        // add random number of rooms
        int numRooms = rand.nextInt(10) + 5;


        for (int i = 0; i < numRooms; i++) {
            addRoom(finalWorldFrame.getWorld(), i);
        }

        connectRooms(finalWorldFrame.getWorld(), numRooms);

        return finalWorldFrame.getWorld();
    }

    // add single room to world
    private Room addRoom(TETile[][] world, int sequence) {
        int w;
        int h;
        Point bottomLeft;
        // generate random width and height of each room
        do {
            w = rand.nextInt(WIDTH / 5) + 3;
            h = rand.nextInt(HEIGHT / 5) + 3;
            int newX = rand.nextInt(WIDTH - w);
            int newY = rand.nextInt(HEIGHT - h);
            bottomLeft = new Point(newX, newY);
        } while (overLapRoom(finalWorldFrame.getWorld(), bottomLeft, w, h));


        int x = bottomLeft.getX();
        int y = bottomLeft.getY();

        for (int i = 0; i <= h; i++) {
            addRow(world, new Point(x, y + i), w, wall);
        }
        for (int j = 0; j <= h - 2; j++) {
            addRow(world, new Point(x + 1, y + 1 + j), w - 2, floor);
        }

        Point topRight = new Point(x + w, y + h);
        Room newRoom = new Room(bottomLeft, topRight, false);
        rooms.put(sequence, newRoom);
        //unconnectedRooms.add(newRoom);
        return newRoom;
    }

    //p is the left point of row
    private void addRow(TETile[][] world, Point p, int length, TETile t) {
        int x = p.getX();
        int y = p.getY();
        for (int i = 0; i <= length; i += 1) {
            world[x + i][y] = t;
        }
    }

    //p is the top point of column
    private void addColumn(TETile[][] world, Point p, int length, TETile t) {
        int x = p.getX();
        int y = p.getY();
        for (int i = 0; i < length; i += 1) {
            world[x][y - i] = t;
        }
    }

    private boolean overLapRoom(TETile[][] world, Point point, int width, int height) {
        int x = point.getX();
        int y = point.getY();
        for (int i = 0; i <= width + 2; i++) {
            for (int j = 0; j <= height + 2; j++) {
                if ((x - 1 + i) >= 0
                        && (x - 1 + i) < WIDTH
                        && (y - 1 + j) >= 0
                        && (y - 1 + j) < HEIGHT) {
                    if (!world[x - 1 + i][y - 1 + j].equals(Tileset.NOTHING)) {
                        return true;
                    } else {
                        continue;
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    private void connectRooms(TETile[][] world, int numRooms) {
        double[][] dist = new double[numRooms][numRooms];
        UnionFind uf = new UnionFind(numRooms);

        // store edges: distance between rooms
        for (int i = 0; i < numRooms; i++) {
            for (int j = i + 1; j < numRooms; j++) {
                Edge newEdge = new Edge(i, j, (int) distance(rooms.get(i), rooms.get(j)));
                allEdges.add(newEdge);
            }
        }

        for (Edge edge: allEdges) {
            int u = edge.getSource();
            int v = edge.getDest();
            if (!uf.connected(u, v)) {
                uf.union(u, v);
                connect2Rooms(world, rooms.get(u), rooms.get(v));
            }
        }
    }

    private void connect2Rooms(TETile[][] world, Room r1, Room r2) {
        int blX1 = r1.getPositionBL().getX();
        int blY1 = r1.getPositionBL().getY();
        int trX1 = r1.getPositionTR().getX();
        int trY1 = r1.getPositionTR().getY();

        int blX2 = r2.getPositionBL().getX();
        int blY2 = r2.getPositionBL().getY();
        int trX2 = r2.getPositionTR().getX();
        int trY2 = r2.getPositionTR().getY();

//        world[blX1][blY1] = Tileset.FLOWER;
//        world[blX2][blY2] = Tileset.FLOWER;

        // draw hallways from (x1, y1) to (x2, y2)
        int x1, y1, x2, y2;
        if (blX1 >= blX2 && blY1 >= blY2) { //room2 is at left-bottom of room1
            if (trY2 - blY1 > 1) { // R2特别长
                x2 = trX2;
                y2 = rand.nextInt(Math.min(trY2, trY1) - blY1 - 1) + blY1 + 1;
                x1 = blX1;
                //y1 = y2;
                addHorizontalHall(world, new Point(x2, y2), x1 - x2 + 1);
            } else if (trX2 - blX1 > 1) { // R2特别扁
                x1 = rand.nextInt(Math.min(trX2, trX1) - blX1 - 1) + blX1 + 1;
                y1 = blY1;
                //x2 = x1;
                y2 = trY2;
                addVerticalHall(world, new Point(x1, y1), y1 - y2 + 1); // +1 or +2 ?
            } else { // R2缩在左下角
                x1 = blX1;
                y1 = rand.nextInt(trY1 - blY1 - 2) + blY1 + 1;
                x2 = rand.nextInt(Math.min(blX1, trX2) - blX2 -2) + blX2 + 1;
                y2 = trY2;
                addVerticalHall(world, new Point(x2, y1), y1 - y2 + 1);
                addHorizontalHall(world, new Point(x2, y1), x1 - x2);
                if (world[x2 - 1][y1 + 1].equals(Tileset.NOTHING)) {
                    world[x2 - 1][y1 + 1] = wall;
                }
            }
        } else if (blX1 < blX2 && blY1 < blY2) {
            connect2Rooms(world, r2, r1);
        } else if (blX1 >= blX2 && blY1 < blY2) { //room2 is at top-left of room1
            if (trY1 - blY2 > 1) { //R2特别长
                x1 = blX1;
                y1 = - rand.nextInt(trY1 - Math.max(blY2, blY1) - 1) + trY1 - 1;
                x2 = trX2;
                y2 = y1;
                addHorizontalHall(world, new Point(x2, y2), x1 - x2 + 1);
            } else if (trX2 - blX1 > 1) { //R2特别扁
                x1 = rand.nextInt(Math.min(trX2, trX1) - blX1 - 1) + blX1 + 1;
                y1 = trY1;
                x2 = x1;
                y2 = blY2;
                addVerticalHall(world, new Point(x2, y2), y2 - y1 +1);
            } else {
                x1 = rand.nextInt(trX1 - Math.max(blX1, trX2) - 1) + Math.max(blX1, trX2) + 1;
                y1 = trY1;
                x2 = trX2;
                y2 = rand.nextInt(trY2 - Math.max(blY2, trY1) - 1) + Math.max(blY2, trY1) + 1;
                addVerticalHall(world, new Point(x1, y2), y2 - y1 + 1);
                addHorizontalHall(world, new Point(x2, y2), x1 - x2);
                if (world[x1 + 1][y2 + 1].equals(Tileset.NOTHING)) {
                    world[x1 + 1][y2 + 1] = wall;
                }
            }
        } else {
            connect2Rooms(world, r2, r1);
        }
    }

    private void addVerticalHall(TETile[][] world, Point p, int length) {
        int x = p.getX();
        int y = p.getY();

        addColumn(world, p, length, floor);

        // to prevent hallways collide, check is the Tile is NOTHING first
        for (int i = 0; i <= length; i++) {
            if (world[x - 1][y - i].equals(Tileset.NOTHING)) {
                world[x - 1][y - i] = wall;
            }
            if (world[x + 1][y - i].equals(Tileset.NOTHING)) {
                world[x + 1][y - i] = wall;
            }
        }
    }

    private void addHorizontalHall(TETile[][] world, Point p, int length) {
        int x = p.getX();
        int y = p.getY();

        addRow(world, p, length, floor);

        // to prevent hallways collide, check is the Tile is NOTHING first
        for (int i = 0; i <= length; i++) {
            if (world[x + i][y - 1].equals(Tileset.NOTHING)) {
                world[x + i][y - 1] = wall;
            }
            if (world[x + i][y + 1].equals(Tileset.NOTHING)) {
                world[x + i][y + 1] = wall;
            }
        }
    }

    private double distance(Room r1, Room r2) {
        int x1 = (r1.getPositionBL().getX() + r1.getPositionTR().getX()) / 2;
        int y1 = (r1.getPositionBL().getY() + r1.getPositionTR().getY()) / 2;
        int x2 = (r2.getPositionBL().getX() + r2.getPositionTR().getX()) / 2;
        int y2 = (r2.getPositionBL().getY() + r2.getPositionTR().getY()) / 2;

        return Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2);
    }


    public TETile[][] getWorld() {
        return finalWorldFrame.getWorld();
    }
}
