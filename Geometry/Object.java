package designer.nakata.lightseeker.Geometry;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import designer.nakata.lightseeker.EntityManager;
import designer.nakata.lightseeker.Geometry.Entity;

import static designer.nakata.lightseeker.Physics.lerp;


public class Object {
    int lineIndex, temp, br=0;
    private float lastTime=0, vel;
    Stack<Integer> next = new Stack<>();
    String mesh;
    private boolean reverse = false, onemore=false;
    public float[]  mMVPMatrix = new float[16];
    final float[] mTranslationMatrix = new float[16];
    float[] look = new float[3];
    public float rotationX = 0, rotationY = 0, rotationZ = 0, posX = 0, posY = 0, posZ = 0;
    public Object(String mesh){
        this.mesh = mesh;
        Matrix.setIdentityM(mTranslationMatrix,0);
    }
    public Entity getEntity(){
        EntityManager eMag = EntityManager.getInstance();
        return eMag.getEntity(mesh);
    }
    public void setLineIndex(int i){
        lineIndex = i;
        temp = i;
    }
    public void setPos(float[] pos){
        posX = pos[0];
        posY = pos[1];
        posZ = pos[2];
        Matrix.translateM(mTranslationMatrix,0, posX, posY, posZ);
        Matrix.multiplyMM(mMVPMatrix,0, getEntity().mMVPMatrix,0, mTranslationMatrix,0);
        Matrix.scaleM(mMVPMatrix,0, 3, 3, 0);
        Matrix.setIdentityM(mTranslationMatrix,0);
    }
    public void follow(float time){
        float x=0, y=0, z=0;
        Line line = CircuitManager.getLine(temp);
        time-=br*10;
        if(time >= 10) br++;
        time/=10f;

        //Move along a line
        if(lastTime - time>0.9){
            if(onemore) {
                reverse = false;
                onemore = false;
                temp = lineIndex;
            }else {
                if (reverse) {
                    int p = next.peek();
                    next.pop();
                    temp = p;
                    line = CircuitManager.getLine(temp);
                    if (next.isEmpty()) onemore = true;
                } else {
                    next.add(temp);
                    temp = line.pointer;
                    if (temp == lineIndex && next.size() > 1) {
                        reverse = true;
                        temp = next.peek();
                        next.pop();
                    }
                    line = CircuitManager.getLine(temp);
                }
            }
        }

        //Smooth movement
        if(!reverse) {
            x = lerp(line.start[0]+2, line.end[0]+2, time);
            y = lerp(line.start[1], line.end[1], time)+3;
            z = lerp(line.start[2], line.end[2], time);
        }else{
            x = lerp(line.end[0]+2, line.start[0]+2, time);
            y = lerp(line.end[1], line.start[1], time)+3;
            z = lerp(line.end[2], line.start[2], time);
        }
        posX=x;
        posY=y;
        posZ=z;
        Matrix.translateM(mTranslationMatrix,0, x, y, z);
        Matrix.multiplyMM(mMVPMatrix,0, getEntity().mMVPMatrix,0, mTranslationMatrix,0);

        //Rotation
        float diffX = Math.abs(line.end[0] - line.start[0]),
                diffY = Math.abs(line.end[1] - line.start[1]),
                diffZ = Math.abs(line.end[2] - line.start[2]);
        float diagonal = 45, straight = 0;
        if(reverse) {
            diagonal=-145;
            straight=-180;
        }
        float rot=0;
        if (diffX != 0 && diffZ != 0) {
            rot = lerp(rotationY, diagonal, time);
            Matrix.rotateM(mMVPMatrix, 0, rot, 0, 1, 0);
            rotationY = rot;
        }
        if (diffX == 0 && diffY == 0) {
            rot = lerp(rotationY, straight, time);
            Matrix.rotateM(mMVPMatrix, 0, rot, 0, 1, 0);
            rotationY = rot;
        }

        Matrix.setIdentityM(mTranslationMatrix,0);
        lastTime = time;
    }
    public float[] getLookAt(){
        Line line = CircuitManager.getLine(temp);
        float speed = 0.1f;
        float rot;
        float[] diff = {line.end[0] - line.start[0],
                line.end[1] - line.start[1],
                line.end[2] - line.start[2]};
        if(reverse) {
            for (int i = 0; i < 3; i++) {
                rot = lerp(look[i], line.end[i]-diff[i]*2, lastTime);
                look[i] = rot;
            }
        }
        else {
            for (int i = 0; i < 3; i++) {
                rot = lerp(look[i], line.start[i]+diff[i]*2, lastTime);
                look[i] = rot;
            }
        }
        return look;
    }
}
