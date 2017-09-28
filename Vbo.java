package designer.nakata.lightseeker;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Vbo {
    final private int vbos[] = new int[1];
    public int usage = GLES20.GL_STATIC_DRAW;

    public Vbo(){
        //Generate VBO
        GLES20.glGenBuffers(1, vbos, 0);
    }

    public void ChangeVbo(float[] data, int offset){

        //Load array to ByteBuffer
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                (data.length * 4));
        bb.order(ByteOrder.nativeOrder());

        FloatBuffer Buffer = bb.asFloatBuffer();
        Buffer.put(data);
        Buffer.position(0);


        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbos[0]);

        GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, offset*4, Buffer.capacity() * 4, Buffer);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }
    public void LoadVbo(float[] data){
        //Load array to ByteBuffer
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                (data.length * 4));
        bb.order(ByteOrder.nativeOrder());

        FloatBuffer Buffer = bb.asFloatBuffer();
        Buffer.put(data);
        Buffer.position(0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbos[0]);

        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, Buffer.capacity() * 4,
                Buffer, usage);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    public void LoadVbo(short[] data){
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                (data.length * 2));
        bb.order(ByteOrder.nativeOrder());
        ShortBuffer indexBuffer = bb.asShortBuffer();
        indexBuffer.put(data);
        indexBuffer.position(0);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, vbos[0]);

        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBuffer.capacity() * 2,
                indexBuffer, usage);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
    }
    public void getAttribute(int handle, int stride){
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbos[0]);
        GLES20.glVertexAttribPointer(
                handle, stride,
                GLES20.GL_FLOAT, false,
                0,0);
        GLES20.glEnableVertexAttribArray(handle);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

    }

    public void bind(int type){
        GLES20.glBindBuffer(type, vbos[0]);
    }
    public void unbind(int type){
        GLES20.glBindBuffer(type, 0);
    }
    public void delete(){
        GLES20.glDeleteBuffers(1, vbos, 0);
    }
}
