
package designer.nakata.lightseeker.Geometry;

import android.content.Context;

import designer.nakata.lightseeker.Geometry.Entity;


public class Model extends Entity {

    public Model(Context ctx,int texId, String type) {
        super(ctx, type);
        loadTexture(ctx, texId);
    }
}