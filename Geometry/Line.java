package designer.nakata.lightseeker.Geometry;

public class Line {
    public float[] start = new float[3], end = new float[3];
    public int index=0, width=4;
    public int pointer=0;
    private static int br=0;
    public Line(float sx, float sy, float sz, float ex, float ey, float ez, int p){
        br++;
        index=br;
        pointer = p;

        start[0] = sx;
        start[1] = sy;
        start[2] = sz;
        end[0] = ex;
        end[1] = ey;
        end[2] = ez;
    }
}
