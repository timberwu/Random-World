package byow.Core;

import byow.TileEngine.TERenderer;


public class TestMain {
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(80, 30);
//        Engine engine = new Engine();
//        engine.interactWithInputString("ladds");
//
//        engine.interactWithInputString("ldsaswssw");
//        ter.renderFrame(engine.getWorld());
//        engine.interactWithInputString("n8702095859193238354ssswadswwds");
//        ter.renderFrame(engine.getWorld());

        Engine engine = new Engine();
        engine.interactWithKeyboard();

    }



}
