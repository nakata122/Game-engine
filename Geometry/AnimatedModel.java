package designer.nakata.lightseeker.Geometry;

import android.content.Context;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import designer.nakata.lightseeker.Rigging.Animation;
import designer.nakata.lightseeker.Rigging.Joint;
import designer.nakata.lightseeker.Vbo;

import static designer.nakata.lightseeker.Physics.EulertoQuat;
import static designer.nakata.lightseeker.Physics.lerp;

/**
 * Created by Nakata on 27.7.2017 г..
 *
 */

public class AnimatedModel extends Entity{
    public List<Animation> animator = new ArrayList<>();
    public Joint[] joints;
    public Vbo move = new Vbo();
    private float time = 0;
    public float[] movement;
    public boolean done = true;
    public AnimatedModel(Context ctx,int texId, String type) {
        super(ctx, type);
        loadTexture(ctx, texId);
    }
    public void Init(){
        movement = new float[vertices.length];
        movement = vertices.clone();
    }

    public void loadAnimation(Animation animation){
        animator.add(animation);
    }

    public Animation getAnim(int id){
        return animator.get(id);
    }

    public float[] getMatrices(){
        float[] matrices = new float[12 * 16];
        for(int i=0; i<12; i++){
            for(int j=0;j<16;j++){
                matrices[i * 16 + j] = joints[i].mJointMatrix[j];
            }
            Matrix.invertM(matrices, i*16, matrices, i*16);
        }
        return matrices;
    }

    public void doAnimation(int id){
        Animation anim = animator.get(id);
        Stack<Integer> next = new Stack<>();
        int joint = anim.jointId;
        float[] rotateMat = EulertoQuat(0,0,lerp(anim.pos1, anim.pos2, time));
        next.push(joint);
        while(!next.empty()) {
            Joint curr = joints[next.peek()];
            curr.mJointMatrix = rotateMat.clone();
            next.pop();
            for (int i = 0; i < curr.children.size(); i++) {
                next.add(curr.children.get(i));
            }
            done = false;
        }
        if(time>=1) {
            movement = vertices.clone();
            time = 0;
        }
        time+=0.01;
    }

}
