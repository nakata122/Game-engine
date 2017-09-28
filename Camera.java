package designer.nakata.lightseeker;

import android.opengl.Matrix;

/**
 * Created by User on 12.9.2017 г..
 */

public class Camera {
    final float[] mViewMatrix = new float[16];
    final float[] mProjectionMatrix = new float[16];
    final float[] mMVPMatrix = new float[16];
    public void Lookat(float sx, float sy, float sz,float  ex, float ey, float  ez, float ux , float uy , float uz){
        Matrix.setLookAtM(mViewMatrix, 0, sx, sy, sz, ex, ey, ez, ux, uy, uz);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
    }
    public void projection3D(int width, int height){
        float top = (float) Math.tan(30 * Math.PI / 360.0f);
        int ratio = width/height;
        Matrix.frustumM(mProjectionMatrix, 0, -ratio*top, ratio*top, -top, top, 1, 1000);
    }
    public void projection2D(int width, int height){
        int ratio = width/height;
        Matrix.orthoM(mProjectionMatrix,0, -ratio, ratio, 1, -1, 1, 100);
    }
}

