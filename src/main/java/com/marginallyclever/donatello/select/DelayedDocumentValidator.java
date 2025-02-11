package com.marginallyclever.donatello.select;

import com.marginallyclever.donatello.simpleparser.SimpleParser;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

/**
 * {@link DelayedDocumentValidator} validates the text in a number field using a {@link SimpleParser} so it can
 * handle simple math expressions.
 */
public class DelayedDocumentValidator implements DocumentListener {
    private final JTextComponent field;
    private Timer timer=null;
    private final Consumer<Double> consumer;

    public DelayedDocumentValidator(JTextComponent field, Consumer<Double> consumer) {
        super();
        this.field = field;
        this.consumer = consumer;
    }

    @Override
    public void changedUpdate(DocumentEvent arg0) {
        if(arg0.getLength()==0) return;
        validate();
    }

    @Override
    public void insertUpdate(DocumentEvent arg0) {
        if(arg0.getLength()==0) return;
        validate();
    }

    @Override
    public void removeUpdate(DocumentEvent arg0) {
        if(arg0.getLength()==0) return;
        validate();
    }

    public void validate() {
        try {
            double newValue = SimpleParser.evaluate(field.getText());
            field.setForeground(UIManager.getColor("Textfield.foreground"));
            field.setToolTipText(null);

            if(timer!=null) timer.cancel();
            timer = new Timer("Delayed response");
            timer.schedule(new TimerTask() {
                public void run() {
                    consumer.accept(newValue);
                }
            }, 100L); // brief delay in case someone is typing fast
        } catch (IllegalArgumentException e) {
            field.setForeground(Color.RED);
            field.setToolTipText(e.getMessage());
        }
    }
}
