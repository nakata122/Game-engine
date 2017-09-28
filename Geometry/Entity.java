package designer.nakata.lightseeker.Geometry;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import designer.nakata.lightseeker.Vbo;

public class Entity {
    float[] vertices, textureCoords, normalCoords;
    //VBOS IBOS
    public Vbo vertex = new Vbo();
    public Vbo index = new Vbo();
    public Vbo texCoord = new Vbo();
    public Vbo normal = new Vbo();
    public String type;
    public int vcount;
    Bitmap texture;
    public float[] mMVPMatrix = new float[16];
    public float[] mTranslationMatrix = new float[16];
    public float[] pos = new float[3];
    public int id;
    public static int ident=1;
    public Entity(Context ctx, String type){
        id = ident;
        this.type = type;
        ident++;
        Matrix.setIdentityM(mTranslationMatrix,0);
    }
    public void loadTexture(Context ctx, int texId){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;

        texture = BitmapFactory.decodeResource(ctx.getResources(), texId, options);

        // Bind to the texture in OpenGL
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, id);
        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, texture, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        texture.recycle();
    }
    public void loadVertBuff(float[] modelCoords){
        vertices = new float[modelCoords.length];
        vertices = modelCoords.clone();

        vertex.LoadVbo(modelCoords);
    }

    public void loadTexBuff(float[] txCoords){
        textureCoords = new float[txCoords.length];
        textureCoords = txCoords.clone();

        texCoord.LoadVbo(txCoords);
    }

    public void loadNormBuff(float[] modelNormals){
        normalCoords = new float[modelNormals.length];
        normalCoords = modelNormals.clone();

        normal.LoadVbo(modelNormals);
    }
    public void loadIndBuff(short[] modelIndices){
        index.LoadVbo(modelIndices);
    }

    public void update(float[] mVPMatrix){
        Matrix.multiplyMM(mMVPMatrix,0, mVPMatrix,0, mTranslationMatrix,0);
        Matrix.setIdentityM(mTranslationMatrix,0);
    }

    public void move(float x, float y, float z){
        Matrix.translateM(mTranslationMatrix,0, x, y, z);
    }
}
