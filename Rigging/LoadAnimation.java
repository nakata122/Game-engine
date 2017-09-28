package designer.nakata.lightseeker.Rigging;

import android.content.Context;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;

import designer.nakata.lightseeker.Geometry.AnimatedModel;
import designer.nakata.lightseeker.Geometry.Model;
import designer.nakata.lightseeker.XmlParser.Node;

/**
 * Created by User on 27.7.2017 г..
 */

public class LoadAnimation {

    public static void load(Context ctx, String name, AnimatedModel current){
        try {
            XmlPullParserFactory pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            InputStream in_s = ctx.getAssets().open(name);
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);

            parseXML(parser, current);

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }
    private static void parseXML(XmlPullParser parser, AnimatedModel curr) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();

        short[] vcount = new short[1];
        Joint[] joints = new Joint[1];
        Node[] arrays = new Node[1000];
        int size = 10000, br=0, last, index=0;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name;
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();

                    if (name.matches("vcount")) {
                        String count = parser.nextText();
                        String[] counts = count.split(" ");
                        size = counts.length;

                        joints = new Joint[size+2];
                        vcount = new short[size+2];

                        for(int i=0;i<size;i++) {
                            joints[i] = new Joint();
                            vcount[i] = Short.parseShort(counts[i]);
                        }

                    } else if (name.matches("v")) {
                        String count = parser.nextText();
                        String[] vTex = count.split(" ");
                        int temp=0;

                        short[] v = new short[vTex.length+1];
                        float[] kon = new float[size];


                        for(int i=0;i<vTex.length;i++) {
                            v[i] = Short.parseShort(vTex[i]);
                        }
                        for(int i=0;i<size;i++){
                            for(int j=0;j<vcount[i];j++){
                                if(v[temp]!=-1) {
                                    joints[v[temp]].vertices.add(i);
                                    kon[i] = v[temp];
                                }
                                temp+=2;
                            }
                        }
                        curr.move.LoadVbo(kon);

                    } else if(name.matches("node")) {
                        if(parser.getAttributeValue(null, "type").matches("JOINT")){
                            int id = Integer.parseInt(parser.getAttributeValue(null, "sid"));
                            int xmlId = Integer.parseInt(parser.getAttributeValue(null, "id").substring(2));

                            int child = Integer.parseInt( parser.getAttributeValue(null, "name"));
                            if(child != -1) {
                                joints[child].children.add(id);
                            }
                            for(int i=0;i<curr.animator.size(); i++) {
                                Animation temp = curr.getAnim(i);
                                if (temp.jointId == xmlId) temp.jointId = id;
                            }
                            br++;
                        }
                    } else if(name.matches("input")){
                        int id;
                        if(parser.getAttributeValue(null, "semantic").matches("OUTPUT")){
                            String source = parser.getAttributeValue(null, "source").substring(3);
                            id = Integer.parseInt(source);
                            Animation temp = new Animation();
                            for(int i=index-1;i>0;i--){
                                if(arrays[i].id == id+1){
                                    String[] data  = arrays[i].data.split(" ");

                                    temp.pos1 = Float.parseFloat(data[0]);
                                    temp.pos2 = Float.parseFloat(data[1]);
                                    break;
                                }
                            }
                            curr.loadAnimation(temp);
                        }
                    }
                    else if(name.matches("channel")){
                        String target = parser.getAttributeValue(null, "target");
                        int i;
                        for(i = 0; i<=target.length(); i++){
                            if(target.charAt(i) == '/') break;
                        }
                        String id = target.substring(2,i);

                        Animation temp = curr.getAnim( curr.animator.size()-1 );
                        temp.jointId = Integer.parseInt(id);
                    }
                    else if (name.matches("float_array")) {
                        String id = parser.getAttributeValue(null, "id").substring(2);
                        arrays[index] = new Node();
                        arrays[index].data = parser.nextText();
                        arrays[index].id = Integer.parseInt(id);
                        index++;
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;

            }
            eventType = parser.next();
        }
        curr.joints = joints.clone();
    }
}