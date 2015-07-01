package utils;

import org.lwjgl.BufferUtils;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by feinte on 01/06/2015.
 * Utilities to facilitate buffer and Arrays manipulation
 */
public class Utils {

    public static FloatBuffer getFloatBufFromArr(float[] array){

        FloatBuffer buf = BufferUtils.createFloatBuffer(array.length);
        buf.put(array);
        buf.flip();

        return buf;
    }

    public static void updateFloatBuffer(FloatBuffer buf, float[] array){
        buf.clear();
        buf.put(array);
        buf.flip();
    }

    public static ByteBuffer getByteBufFromArr(byte[] array){

        ByteBuffer buf = BufferUtils.createByteBuffer(array.length);
        buf.put(array);
        buf.flip();

        return buf;
    }

    public static void updateByteBuffer(ByteBuffer buf, byte[] array){
        buf.clear();
        buf.put(array);
        buf.flip();
    }

    public static IntBuffer getIntBufFromArr(int[] array) {

        IntBuffer buf = BufferUtils.createIntBuffer(array.length);
        buf.put(array);
        buf.flip();

        return buf;
    }

    public static void updateIntBuffer(IntBuffer buf, int[] array) {
        buf.clear();
        buf.put(array);
        buf.flip();
    }

    public static float[] getColorArrayFromColor(Color color) {
        float[] rgbComponents = color.getRGBComponents(null);
        return new float[]{rgbComponents[0], rgbComponents[1], rgbComponents[2], 1f};
    }

    public static float[] getColorArrayFromColor(Color color, int nbVertices) {
        float[] rgbComponents = color.getRGBComponents(null);
        float[] colorArray = new float[nbVertices * 4];
        for (int i = 0, offset; i < nbVertices; i++) {
            offset = i * 4;
            colorArray[offset] = rgbComponents[0];
            colorArray[offset + 1] = rgbComponents[1];
            colorArray[offset + 2] = rgbComponents[2];
            colorArray[offset + 3] = 1f;   // Alpha
        }
        return colorArray;
    }

    public static FloatBuffer colorToFloatBuffer(Color color){
        FloatBuffer buf = BufferUtils.createFloatBuffer(4);
        buf.put(color.getRGBComponents(null));
        buf.flip();
        return buf;
    }

    public static FloatBuffer colorToFloatBuffer(Color color, int nbVertices) {
        FloatBuffer buf = BufferUtils.createFloatBuffer(4 * nbVertices);
        for (int i = 0; i < nbVertices; i++) {
            buf.put(color.getRGBComponents(null));
        }
        buf.flip();
        return buf;
    }
}
