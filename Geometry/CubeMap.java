package designer.nakata.lightseeker.Geometry;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import designer.nakata.lightseeker.R;

public class CubeMap extends Entity {

    public CubeMap(Context ctx, String type) {
        super(ctx, type);
    }
    public void loadCubeMap(Context ctx){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;

        // Bind to the texture in OpenGL
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, id);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        int[] res = {R.drawable.right, R.drawable.left, R.drawable.top, R.drawable.bottom,R.drawable.front ,R.drawable.back};
        // Load the bitmap into the bound texture.
        for(int i=0;i<6;i++) {
            texture = BitmapFactory.decodeResource(ctx.getResources(), res[i], options);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, texture, 0);
        }

        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, 0);
        texture.recycle();

    }
}
