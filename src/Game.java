import entities.*;
import graphic.opengl.renderer.EntityRenderer;
import logic.Army;
import logic.Board;
import logic.InputHandler;
import logic.Player;
import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.Random;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Game {

    // We need to strongly reference callback instances.
    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback   keyCallback;
    private GLFWWindowSizeCallback windowSizeCallback;

    // The window handle
    private long window;
    private int WIDTH = 800;
    private int HEIGHT = 600;

    private EntityRenderer renderer;
    private InputHandler inputHandler;

    private LinkedList<Army> armies;
    private Board board;

    private Player player;

    private EntityFactory entityFactory;

    public static void main(String[] args) {
        new Game().run();
    }

    public void run() {
        System.loadLibrary("lwjgl");
        System.loadLibrary("OpenAL");
        System.out.println("Hello LWJGL " + Sys.getVersion() + "!");

        try {
            init();
            loop();

            // Release window and window callbacks
            glfwDestroyWindow(window);
            keyCallback.release();
        } finally {
            // Terminate GLFW and release the GLFWerrorfun
            glfwTerminate();
            errorCallback.release();
        }
    }

    private void init() {

        setupOpenGl();
        setupGameContext();
    }

    void setupGameContext(){

        player = new Player();

        entityFactory = EntityFactory.getEntityFactory();

        armies = new LinkedList<Army>();
        armies.add(new Army(Color.BLUE));
        armies.get(0).buildVillage(305, 305);
        armies.add(new Army(Color.GREEN));
        armies.get(1).buildVillage(505, 305);

        armies.get(0).spawnTroop(200, 200);
        armies.get(1).spawnTroop(1, 1);
    }

    void setupOpenGl(){
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( glfwInit() != GL11.GL_TRUE )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure our window
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        // Create the window
        window = glfwCreateWindow(WIDTH, HEIGHT, "Hello World!", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                    glfwSetWindowShouldClose(window, GL_TRUE); // We will detect this in our rendering loop
            }
        });

        // Get the resolution of the primary monitor
        ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
                window,
                (GLFWvidmode.width(vidmode) - WIDTH) / 2,
                (GLFWvidmode.height(vidmode) - HEIGHT) / 2
        );

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Disable v-sync (set to 1 to enable)
        glfwSwapInterval(0);

        // Make the window visible
        glfwShowWindow(window);
    }

    private void resize(){
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);

        glfwGetWindowSize(window, width, height);
        WIDTH = width.get(0);
        HEIGHT = height.get(0);
        GL11.glViewport(0, 0, WIDTH, HEIGHT);
        renderer.setOrthoMatrix(WIDTH, HEIGHT);
        inputHandler.resize(WIDTH, HEIGHT);
        board.resize(WIDTH,HEIGHT);
        System.out.println(" New width : " + WIDTH + "   New Height : " + HEIGHT);
    }

    int computeRandom(int range){
        Random random = new Random();
        return (random.nextInt(range+1)-(range/2));
    }

    void update(){

        for(Army army : armies){
            for (Village village : army.getVillages()) {
                village.update();
            }
            for(Troop troop : army.getTroops()){
                troop.move(computeRandom(6), computeRandom(6));
            }
        }

    }

    void render(){

        board.drawGrid();

        /**
         * Batch rendering
         * First init the batch with different sizes
         * Load renderables
         * Draw
         */

        for (Army army : armies) {

            renderer.initPacking(army.getTroops().size(), entityFactory.createPlayerControlledEntity(EntityType.TROOP, army, 0, 0));
            for (Troop troop : army.getTroops()) {
                renderer.packRenderable(troop);
            }
            renderer.drawPackedRenderable(army.getTroops().get(0).getPrimitives().get(0).getOpenGLDrawingMethod());

            // Villages rendering
            renderer.initPacking(army.getVillages().size(), entityFactory.createPlayerControlledEntity(EntityType.VILLAGE, army, 0, 0));
            for (Village village : army.getVillages()) {
                renderer.packRenderable(village);
            }
            renderer.drawPackedRenderable(army.getVillages().get(0).getPrimitives().get(0).getOpenGLDrawingMethod());
        }

        Entity[][] tileMap = board.getTileMap();
        System.out.println("tilemap.length : " + tileMap.length + "   board.getNb : " + board.getNbTiles());
        renderer.initPacking(board.getNbTiles(), entityFactory.createEntity(EntityType.TILE, Color.BLUE, 0, 0));
        for(Entity[] absTile: tileMap){
            for(Entity ordTile : absTile){
                if(ordTile != null)
                    renderer.packRenderable(ordTile);
            }
        }
        renderer.drawPackedRenderable(entityFactory.createEntity(EntityType.TILE, Color.BLUE, 0, 0).getPrimitives().get(0).getOpenGLDrawingMethod());
    }

    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the ContextCapabilities instance and makes the OpenGL
        // bindings available for use.
        GLContext.createFromCurrent();

        int errorCheckValue = GL11.glGetError();
        if (errorCheckValue != GL11.GL_NO_ERROR) {
            System.out.println("ERROR - OpenGL setup went wrong:");
            System.exit(-1);
        }

        renderer = new EntityRenderer();
        board = new Board(WIDTH, HEIGHT);
        // Set the input so that a key pressed stays pressed until polled by glfwGetKey
        glfwSetInputMode(window, GLFW_STICKY_KEYS, 1);
        inputHandler = new InputHandler(board, window, HEIGHT);
        inputHandler.loadPlayerCommandMap(player);

        //we want to modify the projection matrix (without this, mesh normals will break)
        //glMatrixMode(GL_PROJECTION);
        //TODO Adapt glMatrixMode to graphic.opengl 3.2
        // Set the clear color
        glClearColor(0.5f, 0.0f, 0.0f, 0.0f);
        GL11.glViewport(0, 0, WIDTH, HEIGHT);

        glfwSetWindowSizeCallback(window, windowSizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long l, int i, int i1) {
                resize();
            }
        });

        final double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        long currentTime = System.nanoTime();
        long lastTime = System.nanoTime();
        int update = 0;
        int render = 0;
        double delta = 0;
        long now;

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( glfwWindowShouldClose(window) == GL_FALSE ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            if((now - currentTime) > 1000000000) { //1 Second
                glfwSetWindowTitle(window, "Drijkstra : Updates : " + update + "  Render : " + render);
                update = 0;
                render = 0;
                currentTime = System.nanoTime();
            }
            inputHandler.handlePlayerInput(player);
            if(delta >= 1) {
                update();
                update++;
                delta--;
            }

            render();
            render++;

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
            errorCheckValue = GL11.glGetError();
            if (errorCheckValue != GL11.GL_NO_ERROR) {
                System.out.println("ERROR - Main Loop:");
                System.exit(-1);
            }
        }
        exit_cleanup();
    }

    private void exit_cleanup(){
        renderer.destroyOpenGL();
    }

}