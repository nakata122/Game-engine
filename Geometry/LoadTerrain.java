package designer.nakata.lightseeker.Geometry;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * Created by User on 27.7.2017 г..
 */

public class LoadTerrain {

    public static void load(Context ctx, String name, float s, Entity current){
        try {
            XmlPullParserFactory pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            InputStream in_s = ctx.getAssets().open(name);
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);

            parseXML(parser, current, s);

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }
    private static void parseXML(XmlPullParser parser, Entity curr, float size) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        float[] vertices = new float[1], vertsOrdered, textureCoords = new float[1];

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name;
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.matches("verts")) {
                        String vertext = parser.nextText();
                        String[] items = vertext.split(" ");

                        vertices = new float[items.length+1];

                        for(int i=0;i<items.length;i++){
                            vertices[i] = Float.parseFloat(items[i])*size;
                        }

                    } else if (name.matches("tex")){
                        String textext = parser.nextText();
                        String[] textureId = textext.split(" ");

                        textureCoords = new float[textureId.length];

                        for(int i=0;i<textureId.length;i++){
                            textureCoords[i] = Float.parseFloat(textureId[i]);
                        }

                    } else if (name.matches("p")) {
                        String vertext = parser.nextText();
                        String[] order = vertext.split(" ");
                        curr.vcount = order.length/3;

                        vertsOrdered = new float[order.length+1];
                        short[] f = new short[order.length+1];
                        float[] tex = new float[order.length+1];

                        int temp=0;
                        int texTemp=0;
                        for(int i=0;i<order.length;i+=3){
                            f[temp] = Short.parseShort(order[i]);

                            tex[texTemp] = textureCoords[Integer.parseInt(order[i + 2]) * 2];
                            tex[texTemp+1] = 1-textureCoords[Integer.parseInt(order[i + 2]) * 2 + 1];

                            vertsOrdered[i] = vertices[f[temp]*3];
                            vertsOrdered[i+1] = vertices[f[temp]*3+1];
                            vertsOrdered[i+2] = vertices[f[temp]*3+2];


                            temp++;
                            texTemp+=2;
                        }

                            Random rand = new Random();
                            float[] triCenter = new float[order.length+1];
                            //Find the centroid of tirangle and add offset
                            for(int i=0; i<order.length; i+=9){
                                float avX = (vertsOrdered[i] + vertsOrdered[i+3] + vertsOrdered[i+6])/3 + rand.nextInt(40)-20;
                                float avY = (vertsOrdered[i+1] + vertsOrdered[i+4] + vertsOrdered[i+7])/3 + rand.nextInt(40)-20;
                                float avZ = (vertsOrdered[i+2] + vertsOrdered[i+5] + vertsOrdered[i+8])/3 + rand.nextInt(40)-20;
                                for(int j=0;j<9;j+=3) {
                                    triCenter[i+j] = avX;
                                    triCenter[i+j + 1] = avY;
                                    triCenter[i+j + 2] = avZ;
                                }

                            }

                        //Load separate triangles
                        curr.loadVertBuff(vertsOrdered);
                        curr.loadTexBuff(tex);
                        curr.loadNormBuff(triCenter);
                    }
                    break;
            }
            eventType = parser.next();
        }
    }
}
