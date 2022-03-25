package com.marginallyclever.donatello.nodes.color;

import com.marginallyclever.nodegraphcore.DAO4JSON;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;

/**
 * For dealing with {@link Color}
 * @author Dan Royer
 * @since 2022-03-19
 */
public class ColorDAO4JSON implements DAO4JSON<Color> {
    @Override
    public Object toJSON(Object value) throws JSONException {
        Color image = (Color)value;
        JSONObject v = new JSONObject();
        v.put("r",image.getRed());
        v.put("g",image.getGreen());
        v.put("b",image.getBlue());
        v.put("a",image.getAlpha());
        return v;
    }

    @Override
    public Color fromJSON(Object object) throws JSONException {
        JSONObject v = (JSONObject)object;
        int r = v.getInt("r");
        int g = v.getInt("g");
        int b = v.getInt("b");
        int a = v.getInt("a");
        // for a complete snapshot, restore all the instance details, too.
        return new Color(r,g,b,a);
    }
}
