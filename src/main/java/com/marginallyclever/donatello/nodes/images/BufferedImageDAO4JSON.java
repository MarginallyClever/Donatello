package com.marginallyclever.donatello.nodes.images;

import com.marginallyclever.nodegraphcore.AbstractDAO4JSON;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.image.BufferedImage;

public class BufferedImageDAO4JSON extends AbstractDAO4JSON<BufferedImage> {
    public BufferedImageDAO4JSON() {
        super(BufferedImage.class);
    }

    @Override
    public Object toJSON(Object value) throws JSONException {
        BufferedImage image = (BufferedImage)value;
        JSONObject v = new JSONObject();
        v.put("width",image.getWidth());
        v.put("height",image.getHeight());
        v.put("type",image.getType());
        // for a complete snapshot, capture all the instance details, too.
        return v;
    }

    @Override
    public BufferedImage fromJSON(Object object) throws JSONException {
        JSONObject v = (JSONObject)object;
        int w = v.getInt("width");
        int h = v.getInt("height");
        int type = v.getInt("type");
        // for a complete snapshot, restore all the instance details, too.
        return new BufferedImage(w,h,type);
    }
}
