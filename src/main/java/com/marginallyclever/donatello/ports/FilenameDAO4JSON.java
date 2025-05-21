package com.marginallyclever.donatello.ports;

import com.marginallyclever.nodegraphcore.AbstractDAO4JSON;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.Nonnull;

public class FilenameDAO4JSON extends AbstractDAO4JSON<Filename> {
    public FilenameDAO4JSON() {
        super(Filename.class);
    }

    @Nonnull
    @Override
    public Object toJSON(Object value) throws JSONException {
        Filename f = (Filename)value;
        JSONObject v = new JSONObject();
        v.put("filename",f.get());
        return v;
    }

    @Nonnull
    @Override
    public Filename fromJSON(Object object) throws JSONException {
        if(object instanceof String v) {
            return new Filename(v);
        } else if(object instanceof JSONObject v) {
            return new Filename(v.optString("filename", ""));
        } else {
            throw new JSONException("FilenameDAO4JSON.fromJSON: object is not a JSONObject");
        }
    }
}
