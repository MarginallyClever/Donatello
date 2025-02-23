package com.marginallyclever.donatello.curveeditor;

import java.util.EventListener;

public interface CurveChangedListener extends EventListener {
    /**
     * Called when the curve has been changed.
     * @param curveEditor the curve editor that has changed.
     */
    void curveChanged(CurveEditor curveEditor);
}
