package graphic.opengl.renderer;

import entities.Entity;
import graphic.opengl.Primitive;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import static utils.Utils.*;

/**
 * Created by feinte on 31/05/2015.
 * Renderer for entities
 */
public class EntityRenderer extends OpenGlRenderer {

    int indicesLength;
    int verticesArrayLength;
    int indicesArrayLength;
    int colorArrayLength;
    private FloatBuffer verticesBuffer;
    private FloatBuffer colorsBuffer;
    private IntBuffer indicesBuffer;
    private int offset = 0;


    public EntityRenderer(){
        setupShaders();
        setupVao();
    }

    @Override
    public void setupVao(){

        orthoBuffer = getFloatBufFromArr(ortho_matrix);

        // Create a new Vertex Array Object in memory and select it (bind)
        // A VAO can have up to 16 attributes (VBO's) assigned to it by default
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);

        // Create a new Vertex Buffer Object in memory and select it (bind)
        // A VBO is a collection of Vectors which in this case resemble the location of each vertex.
        vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, 0, 0); // Position
        // Deselect (bind to 0) the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        vbocId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbocId);
        GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, 0, 0); // Color
        // Deselect (bind to 0) the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        // Deselect (bind to 0) the VAO
        GL30.glBindVertexArray(0);

        vboiId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
        // Deselect (bind to 0) the VBO
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    @Override
    protected void setupShaders() {
        int errorCheckValue;

        // Load the vertex shader
        vsId = this.loadShader("vertex.glsl", GL20.GL_VERTEX_SHADER);
        // Load the fragment shader
        fsId = this.loadShader("fragment.glsl", GL20.GL_FRAGMENT_SHADER);

        // Create a new shader program that links both shaders
        pId = GL20.glCreateProgram();
        GL20.glAttachShader(pId, vsId);
        GL20.glAttachShader(pId, fsId);

        // Position information will be attribute 0
        GL20.glBindAttribLocation(pId, 0, "in_Position");
        // Color information will be attribute 1
        GL20.glBindAttribLocation(pId, 1, "in_Color");

        GL20.glLinkProgram(pId);
        GL20.glValidateProgram(pId);

        ortho_matrix_location = GL20.glGetUniformLocation(pId, "inOrthoMatrix");

        errorCheckValue = GL11.glGetError();
        if (errorCheckValue != GL11.GL_NO_ERROR) {
            System.out.println("ERROR - Could not create the shaders:");
            System.exit(-1);
        }
    }

    @Override
    public void destroyOpenGL()  {
        // Delete the shaders
        GL20.glUseProgram(0);
        GL20.glDetachShader(pId, vsId);
        GL20.glDetachShader(pId, fsId);

        GL20.glDeleteShader(vsId);
        GL20.glDeleteShader(fsId);
        GL20.glDeleteProgram(pId);

        // Select the VAO
        GL30.glBindVertexArray(vaoId);

        // Disable the VBO index from the VAO attributes list
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);

        // Delete the vertex VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(vboId);

        // Delete the color VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(vbocId);

        // Delete the index VBO
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(vboiId);

        // Delete the VAO
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoId);

    }

    public void initPacking(int nbUnits, Renderable renderableTemplate) {
        int errorCheckValue;

        this.verticesArrayLength = renderableTemplate.getPrimitives().get(0).getEntityVertices(0, 0).length;
        this.indicesArrayLength = renderableTemplate.getPrimitives().get(0).getIndices().length;
        this.colorArrayLength = renderableTemplate.getColorArray().length;

        verticesBuffer = BufferUtils.createFloatBuffer(nbUnits * verticesArrayLength);
        indicesBuffer = BufferUtils.createIntBuffer(nbUnits * indicesArrayLength);
        colorsBuffer = BufferUtils.createFloatBuffer(nbUnits * colorArrayLength);
        indicesLength = nbUnits * indicesArrayLength;

        offset = 0;
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_DYNAMIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbocId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorsBuffer, GL15.GL_DYNAMIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_DYNAMIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        System.out.println("Init Packing, verticesArrayLength : " + verticesArrayLength + "  indicesArrayLength : " + indicesArrayLength + "  colorArrayLength : " + colorArrayLength + "   Nb units : " + nbUnits);

        errorCheckValue = GL11.glGetError();
        if (errorCheckValue != GL11.GL_NO_ERROR) {
            System.out.println("ERROR - Could not init packing:");
            System.out.println("Error number : " + errorCheckValue);
            System.exit(-1);
        }
    }

    public void packRenderable(Renderable renderable) {

        int[] tempIndices = renderable.getIndices().clone();
        for (int i = 0; i < tempIndices.length; i++) {
            tempIndices[i] += (offset * renderable.getNbVertices());
        }

        System.out.println("Adding vertices : " + Arrays.toString(renderable.getVertices()) +
                "    indices : " + Arrays.toString(tempIndices) +
                "    color : " + Arrays.toString(renderable.getColorArray()) +
                "    at offset : " + offset);

        verticesBuffer.put(renderable.getVertices());
        indicesBuffer.put(tempIndices);
        colorsBuffer.put(renderable.getColorArray());

        verticesBuffer.flip();
        indicesBuffer.flip();
        colorsBuffer.flip();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, offset * verticesArrayLength * 4, verticesBuffer);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbocId);
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, offset * colorArrayLength * 4, colorsBuffer);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
        System.out.println("Allocated : " + indicesLength + " indices  " +
                "   for  " + indicesLength * 4 + "  bytes  " +
                "Buffering : " + indicesBuffer.remaining() + " indices" +
                "    at offset : " + offset * indicesArrayLength * 4);
        GL15.glBufferSubData(GL15.GL_ELEMENT_ARRAY_BUFFER, offset * indicesArrayLength * 4, indicesBuffer);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        offset++;

        int errorCheckValue = GL11.glGetError();
        if (errorCheckValue != GL11.GL_NO_ERROR) {
            System.out.println("ERROR - Could not pack renderable");
            System.out.println("Error number : " + errorCheckValue);
            System.exit(-1);
        }
    }

    public void drawPackedRenderable(int openGLDrawingMethod) {
        GL20.glUseProgram(pId);
        // Bind to the VAO that has all the information about the quad vertices
        GL30.glBindVertexArray(vaoId);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        GL20.glUniformMatrix4fv(ortho_matrix_location, true, orthoBuffer);

        System.out.println("Drawing with " + openGLDrawingMethod + "  NbIndices  " + indicesLength);

        System.out.println("Offset : " + offset);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
        GL11.glDrawElements(openGLDrawingMethod, indicesLength, GL11.GL_UNSIGNED_INT, 0);

        // Put everything back to default (deselect)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
        GL20.glUseProgram(0);

        int errorCheckValue = GL11.glGetError();
        if (errorCheckValue != GL11.GL_NO_ERROR) {
            System.out.println("ERROR - Could not draw packed renderable");
            System.exit(-1);
        }
    }

    public void drawDynamicRenderable(Entity entity, float xPos, float yPos){

        GL20.glUseProgram(pId);
        // Bind to the VAO that has all the information about the quad vertices
        GL30.glBindVertexArray(vaoId);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        GL20.glUniformMatrix4fv(ortho_matrix_location, true, orthoBuffer);

        for(Primitive primitive : entity.getPrimitives())
        {
            float[] vertices = primitive.getEntityVertices(entity.getX(), entity.getY());
            int[] indices = primitive.getIndices();
            float[] colors = entity.getColorArray();

            // Sending data to OpenGL requires the usage of (flipped) buffers
            updateFloatBuffer(verticesBuffer, vertices);
            updateFloatBuffer(colorsBuffer, colors);
            updateIntBuffer(indicesBuffer, indices);

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_DYNAMIC_DRAW);
            // Put the VBO in the attributes list at index 0
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbocId);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorsBuffer, GL15.GL_DYNAMIC_DRAW);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

            // Draw the vertices
            // Bind to the index VBO that has all the information about the order of the vertices
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
            GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_DYNAMIC_DRAW);

            GL11.glDrawElements(primitive.getOpenGLDrawingMethod(), indices.length, GL11.GL_UNSIGNED_INT, 0);
        }

        // Put everything back to default (deselect)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
        GL20.glUseProgram(0);
    }
}
