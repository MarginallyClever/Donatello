package com.marginallyclever.donatello.ports;

import com.marginallyclever.donatello.SwingProvider;
import com.marginallyclever.donatello.select.Select;
import com.marginallyclever.donatello.select.SelectFile;
import com.marginallyclever.nodegraphcore.port.Input;

import javax.swing.*;
import java.awt.*;

/**
 * A port that accepts a filename.
 */
public class InputFilename extends Input<Filename> implements SwingProvider {
    private SelectFile selectFile;
    private JFileChooser fileChooser;
    private boolean isSave=false;

    public InputFilename(String name) {
        this(name,"");
    }

    public InputFilename(String name, String startingValue) throws IllegalArgumentException {
        super(name, Filename.class, new Filename(startingValue));
    }

    @Override
    public Select getSwingComponent(Component parent) {
        if(selectFile==null) {
            selectFile = new SelectFile(name,name,getValue().get(),parent);
            selectFile.addSelectListener( evt -> {
                setValue(evt.getNewValue());
            });
        }
        if(fileChooser!=null) {
            selectFile.setFileChooser(fileChooser);
        }
        selectFile.setDialogType(isSave);
        return selectFile;
    }

    /**
     * Set the file chooser to use when selecting a file.
     * @param fileChooser the file chooser to use.  cannot be null.
     */
    public void setFileChooser(JFileChooser fileChooser) {
        this.fileChooser = fileChooser;
        if(selectFile!=null) {
            selectFile.setFileChooser(fileChooser);
        }
    }

    /**
     * @param isSave true for save dialog, false for load dialog.  Default is false.
     */
    public void setDialogType(boolean isSave) {
        this.isSave = isSave;
        if(selectFile!=null) {
            selectFile.setDialogType(isSave);
        }
    }

    @Override
    public void setValue(Object arg0) {
        if(arg0 instanceof String str) {
            setDirtyOnValueChange(arg0);
            value.set(str);
            return;
        }
        super.setValue(arg0);
    }
}
