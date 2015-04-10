import org.lwjgl.opengl.GL11;

/**
 * Created by feinte on 14/01/2015.
 */
public class Village extends Entity {

    public final static int OPENGLDRAWINGMETHOD = GL11.GL_TRIANGLES;
    private int tick;

    private final float vertices[] = {
            0f , 5f, 0f, 1f,
            5f, -5f, 0f, 1f,
            -5f, -5f, 0f, 1f
    };
    private final byte[] indices = {
            // Left bottom triangle
            0, 1, 2
    };

    public Village(Army army, float x, float y){
        this.army = army;
        this.x = x;
        this.y = y;
    }

    public void update(){
        tick++;
        if(tick > 50){
            army.spawnTroop(x, y);
            tick = 0;
        }
    }

    @Override
    public int getOpenGLDrawingMethod() {
        return OPENGLDRAWINGMETHOD;
    }

    @Override
    public float[] getEntityVertices() {
        return vertices;
    }

    @Override
    public byte[] getEntityIndices() {
        return indices;
    }
}
