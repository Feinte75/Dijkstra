import entities.Army;
import logic.Board;
import entities.Troop;
import entities.Village;
import graphic.opengl.renderer.EntityRenderer;
import graphic.opengl.Primitive;
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
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
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
    private DoubleBuffer xPosBuffer, yPosBuffer;
    private int xPos, yPos;
    private int WIDTH = 800;
    private int HEIGHT = 600;

    EntityRenderer renderer;

    private LinkedList<Army> armies;
    private Board board;
    private ArrayList<Primitive> gridPrimitives;

    public void run() {
        System.loadLibrary("lwjgl");
        System.loadLibrary("OpenAL32");
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

        armies = new LinkedList<Army>();
        armies.add(new Army(Color.BLUE));
        armies.get(0).buildVillage(305, 305);
        armies.add(new Army(Color.GREEN));
        armies.get(1).buildVillage(505,305);

        xPosBuffer = BufferUtils.createDoubleBuffer(1);
        yPosBuffer = BufferUtils.createDoubleBuffer(1);
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
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
    }

    private void getCursorPosition(){
        glfwGetCursorPos(window, xPosBuffer, yPosBuffer);
        xPos = ((int) xPosBuffer.get(0));
        yPos = HEIGHT - ((int) yPosBuffer.get(0));
        System.out.println("xPos : " + xPos + "  yPos : " + yPos);

        // Dirty Dirty : compute the center of a square grid
        if(xPos%50>=25)xPos-=25;
        else xPos+=25;
        if(yPos%50>=25)yPos-=25;
        else yPos+=25;
        board.drawSquare((((xPos)/50)*50)+25, (((yPos)/50)*50)+25);
    }

    private void resize(){
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);

        glfwGetWindowSize(window, width, height);
        WIDTH = width.get(0);
        HEIGHT = height.get(0);
        GL11.glViewport(0,0,WIDTH, HEIGHT);
        renderer.setOrthoMatrix(WIDTH, HEIGHT);
        board.redrawGrid(WIDTH,HEIGHT);
        System.out.println("width : " + WIDTH + "   Height : " + HEIGHT);
    }

    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the ContextCapabilities instance and makes the OpenGL
        // bindings available for use.
        GLContext.createFromCurrent();

        renderer = new EntityRenderer();
        board = new Board(WIDTH, HEIGHT);

        //we want to modify the projection matrix (without this, mesh normals will break)
        //glMatrixMode(GL_PROJECTION);
        //TODO Adapt glMatrixMode to graphic.opengl 3.2
        // Set the clear color
        glClearColor(0.5f, 0.0f, 0.0f, 0.0f);
        GL11.glViewport(0, 0, WIDTH, HEIGHT);

        glfwSetWindowSizeCallback(window, windowSizeCallback =  new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long l, int i, int i1) {
                resize();
            }
        });
        final double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        long currentTime = System.currentTimeMillis();
        long lastTime = System.nanoTime();
        int update = 0;
        double delta = 0;
        long now;


        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( glfwWindowShouldClose(window) == GL_FALSE ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if(delta >= 1){
                update();
                delta--;
            }
            render();
            if (glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == 1) {
                getCursorPosition();
            }

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
        renderer.destroyOpenGL();
    }

    int computeRandom(int range){
        Random random = new Random();
        return (random.nextInt(range+1)-(range/2));
    }

    void update(){

        for(Army army : armies){
            for(Village village : army.getVillages()){
                village.update();
            }
            for(Troop troop : army.getTroops()){
                troop.move(computeRandom(6),computeRandom(6));
            }

        }

    }

    void render(){

        board.update();
        for(Army army : armies){
            for(Troop troop : army.getTroops())renderer.drawEntity(troop);
            for(Village village : army.getVillages())renderer.drawEntity(village);
        }

    }

    public static void main(String[] args) {
        new Game().run();
    }

}