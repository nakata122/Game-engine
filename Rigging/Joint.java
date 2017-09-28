package designer.nakata.lightseeker.Rigging;

import android.opengl.Matrix;

import java.util.ArrayList;
import java.util.List;


public class Joint {
    public int name;
    public float[] mJointMatrix = new float[16];
    public List<Integer> children = new ArrayList<>();
    public List<Integer> vertices = new ArrayList<>();

    public Joint(){
        Matrix.setIdentityM(mJointMatrix, 0);
    }
}
