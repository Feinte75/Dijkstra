package utils;

import org.lwjgl.BufferUtils;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Created by feinte on 01/06/2015.
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

    public static FloatBuffer colorToFloatBuffer(Color color){
        FloatBuffer buf = BufferUtils.createFloatBuffer(4);
        buf.put(color.getRGBComponents(null));
        buf.flip();
        return buf;
    }

    public static FloatBuffer colorToFloatBuffer(Color color, int nbIndices){
        FloatBuffer buf = BufferUtils.createFloatBuffer(4 * nbIndices);
        for(int i = 0; i < nbIndices ; i++){
            buf.put(color.getRGBComponents(null));
        }
        buf.flip();
        return buf;
    }
}
