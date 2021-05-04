package org.bioshock.entities;

import org.bioshock.engine.core.ChatManager;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TextChat extends LabelEntity{

    public TextChat(Point3D p, Font font, int charsPerLine, Color color) {
        super(p, font, charsPerLine, color);
        setStringBuilder(ChatManager.getStrBuild());
    }
    
    @Override
    protected void tick(double timeDelta) {
        setStringBuilder(ChatManager.getStrBuild());
    }

}
