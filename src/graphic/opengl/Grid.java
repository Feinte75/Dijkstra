package graphic.opengl;

import org.lwjgl.opengl.GL11;

/**
 * Created by feinte on 02/04/2015.
 * Game grid
 */
public class Grid extends Primitive {

    private final static int OPENGLDRAWINGMETHOD = GL11.GL_LINES;

    public Grid(int width, int height, int span){
        super();
        generateGrid(width, height, span);
    }

    /**
     * Generate the opengl vertices and indices to display a grid with following parameter
     * Bug : a span of 10 won't display correctly
     * Hypothesis : too much indices sent to drawElements opengl function
     * @param width the width of the grid
     * @param height the height of the grid
     * @param span the span of the grid : determine dimensions of squares
     */
    public void generateGrid(int width, int height, int span){

        int size = (width/span + height/span) * 8;
        int indice = 0;
        vertices = new float[size];

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

        indices = new int[vertices.length / 4];

        for(int i = 0; i < indices.length; i++)
        {
            indices[i] = i;
        }
        System.out.println("Number of lines : " + size / 8 + " number of indices : " + indices[2] + "  size of array : " + vertices.length);

    }

    @Override
    public int getOpenGLDrawingMethod() {
        return OPENGLDRAWINGMETHOD;
    }

    @Override
    public float[] getEntityVertices(float x, float y) {
        return vertices;
    }

    @Override
    public int getNbVertices() {
        return 0;
    }

    @Override
    public int[] getIndices() {
        return indices;
    }
}
