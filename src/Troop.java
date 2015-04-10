import org.lwjgl.opengl.GL11;

/**
 * Created by feinte on 23/12/2014.
 */
public class Troop extends Entity {

    public final static int OPENGLDRAWINGMETHOD = GL11.GL_TRIANGLES;

    private final float vertices[] = {
            -2.5f , 2.5f, 0f, 1f,
            -2.5f, -2.5f, 0f, 1f,
            2.5f, -2.5f, 0f, 1f,
            2.5f, 2.5f, 0f, 1f
    };
    private final byte[] indices = {
            // Left bottom triangle
            0, 1, 2,
            // Right top triangle
            2, 3, 0
    };

    Troop(Army army, float x, float y){

        this.army = army;
        this.x = x;
        this.y = y;
    }

    public void move(float dx, float dy){
        this.x += dx;
        this.y += dy;
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
