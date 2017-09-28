package designer.nakata.lightseeker.Geometry;

import android.util.Log;

import java.util.List;
import java.util.Random;

import designer.nakata.lightseeker.MapCoords;


public class CircuitManager {
    private static float[] vertices = new float[10000];
    private static short[] indices =  new short[10000];
    private static float[] textureCoords = new float[10000];
    private static int offset = 0, indOff = 0, texOff, br = 0;
    public static Line[] lines = new Line[10000];

    public static Line getLine(int i){
        return lines[i];
    }

    public static void level1(Entity curr){

        lines[0] = new Line(6,3,2, 6,3,22, 1);
        lines[1] = new Line(6,3,22, 16,3,32, 2);
        lines[2] = new Line(16,3,32, 16,3,50, 0);
        lines[3] = new Line(9,3,2, 9,3,22, 4);
        lines[4] = new Line(9,3,22, 19,3,32, 5);
        lines[5] = new Line(19,3,32, 19,3,50, 3);
        for(int i=0;i<6;i++)
            Circiut(lines[i]);


        MapCoords.getMatrix();
        curr.vcount = 120;
        curr.loadIndBuff(indices);
        curr.loadTexBuff(textureCoords);
        curr.loadVertBuff(vertices);
    }
    private static void Circiut(Line line){
        float diffX = Math.abs(line.end[0] - line.start[0]),
              diffY = Math.abs(line.end[1] - line.start[1]),
              diffZ = Math.abs(line.end[2] - line.start[2]);

        //Set up matrix for collision detection
        if(diffX==0 && diffY==0)
            for(int i=0;i<diffZ;i++){
                for(int j=0;j<4;j++) {
                    int x = Math.round(line.start[0])+j, z = Math.round(line.start[2]) + i;
                    MapCoords.xz[x][z] = line.index;
                }
            }
        else if(diffZ==0 && diffY==0)
            for(int i=0;i<diffX;i++){
                for(int j=-4;j<0;j++) {
                    int x = Math.round(line.start[0] + i), z = Math.round(line.start[2])+j;
                    MapCoords.xz[x][z] = line.index;
                }
            }
        else if(diffZ!=0 && diffY==0 && diffX!=0)
            for(int i=0;i<diffX;i++){
                for(int j=6;j<10;j++) {
                    int x = Math.round(line.start[0]+i)+j, z = Math.round(line.start[2]+i);
                    MapCoords.xz[x][z] = line.index;
                }
            }
        AddIndices();
        AddTexs();
        br+=4;
        float[] weights = {0, 0, line.width};
        if(diffZ>=diffX) {
            weights[0] = line.width;
            weights[2] = 0;
        }
        for(int j=0;j<3;j++) {
            vertices[offset] = line.start[j];
            offset++;
        }
        for(int j=0;j<3;j++) {
            vertices[offset] = line.start[j] + weights[j];
            offset++;
        }
        for (int j = 0; j < 3; j++) {
            vertices[offset] = line.end[j];
            offset++;
        }
        for (int j = 0; j < 3; j++) {
            vertices[offset] = line.end[j] + weights[j];
            offset++;
        }
    }
    private static void AddIndices(){
        for(int i=0;i<3;i++) {
            indices[indOff] = (short)(i+br);
            indOff++;
        }
        for(int i=3;i>0;i--) {
            indices[indOff] = (short)(i+br);
            indOff++;
        }
    }
    private static void AddTexs(){
        float[] textureVertices = {
                0.0f, 1.0f,
                1.0f, 1.0f,
                0.0f,  0.0f,

                1.0f,  0.0f,
                0.0f,  0.0f,
                1.0f,  1.0f
        };
        for(int i=0;i<12;i++) {
            textureCoords[texOff] = textureVertices[i];
            texOff++;
        }
    }
}
