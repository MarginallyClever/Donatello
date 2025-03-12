package com.marginallyclever.donatello.ports;

import com.marginallyclever.nodegraphcore.AbstractDAO4JSON;
import org.json.JSONException;
import org.json.JSONObject;

public class FilenameDAO4JSON extends AbstractDAO4JSON<Filename> {
    public FilenameDAO4JSON() {
        super(Filename.class);
    }

    @Override
    public Object toJSON(Object value) throws JSONException {
        Filename f = (Filename)value;
        JSONObject v = new JSONObject();
        v.put("filename",f.get());
        return v;
    }

    @Override
    public Filename fromJSON(Object object) throws JSONException {
        JSONObject v = (JSONObject)object;
        return new Filename(v.optString("filename",""));
    }
}
