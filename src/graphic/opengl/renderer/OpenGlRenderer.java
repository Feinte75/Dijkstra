package graphic.opengl.renderer;

import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import static utils.Utils.updateFloatBuffer;

/**
 * Created by Glenn on 18/01/2015.
 * Abstract renderer
 */
public abstract class OpenGlRenderer {

    protected static int vboIndice = 0;
    protected static FloatBuffer orthoBuffer;
    //
    protected int vaoId = 0;
    protected int vboId = 0;
    protected int vbocId = 0;
    protected int vboiId = 0;
    // Shaders
    protected int vsId = 0;
    protected int fsId = 0;
    protected int pId = 0;
    protected int ortho_matrix_location;
    protected float ortho_matrix[] = {
            2/800f, 0, 0, -1,
            0, 2/600f, 0, -1,
            0, 0, 1, 0,
            0, 0, 0, 1
    };

    public OpenGlRenderer() {

    }

    protected abstract void setupVao();

    protected abstract void setupShaders();

    protected int loadShader(String filename, int type) {
        StringBuilder shaderSource = new StringBuilder();
        int shaderID;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Could not read file.");
            e.printStackTrace();
            System.exit(-1);
        }

        shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);

        return shaderID;
    }

    public abstract void destroyOpenGL();

    public void setOrthoMatrix(float width, float height){
        ortho_matrix[0] = 2/width;
        ortho_matrix[5] = 2/height;
        updateFloatBuffer(orthoBuffer, ortho_matrix);
    }
}
