import org.lwjgl.opengl.GL11;
/**
 * Created by feinte on 02/04/2015.
 */
public class Grid extends Entity {

    public final static int OPENGLDRAWINGMETHOD = GL11.GL_LINES;

    private float[] vertices;
    private byte[] indices;

    public Grid(Army army){
        this.army = army;
        generateGrid(800, 600, 10);
    }

    public void generateGrid(int width, int height, int span){

        int size = (width/span + height/span) * 8;
        int indice = 0;
        vertices = new float[size];
        for(int i = 0; i < width/span; i++)
        {
            vertices[indice] = i * span;
            vertices[indice+1] = 0f;
            vertices[indice+2] = 0f;
            vertices[indice+3] = 1f;
            vertices[indice+4] = i * span;
            vertices[indice+5] = height;
            vertices[indice+6] = 0f;
            vertices[indice+7] = 1f;
            indice += 8;
        }

        for(int i = 0; i < height/span; i++)
        {
            vertices[indice] = 0f;
            vertices[indice+1] = i * span;
            vertices[indice+2] = 0f;
            vertices[indice+3] = 1f;
            vertices[indice+4] = width;
            vertices[indice+5] = i * span;
            vertices[indice+6] = 0f;
            vertices[indice+7] = 1f;
            indice += 8;
        }

        indices = new byte[vertices.length / 4];
        for(int i = 0; i < indices.length; i+=2)
        {
            indices[i] = (byte)i;
            indices[i+1] = (byte)(i+1);
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
