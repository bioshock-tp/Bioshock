package org.bioshock.rendering.renderers;

import static org.bioshock.rendering.RenderManager.getRenHeight;
import static org.bioshock.rendering.RenderManager.getRenWidth;
import static org.bioshock.rendering.RenderManager.getRenX;
import static org.bioshock.rendering.RenderManager.getRenY;

import org.bioshock.entities.LabelEntity;
import org.bioshock.main.App;

import javafx.scene.canvas.GraphicsContext;

public class LabelRenderer implements Renderer {
    
    public static <E extends LabelEntity> void render(
            GraphicsContext gc,
            E label
        ) {
        if(label.isDisplay()) {
            gc.save();
            gc.setFont(label.getFont());            
            gc.setFill(label.getColor());
            
            String textToDisplay = label.getStringBuilder().toString();
            App.logger.debug(textToDisplay);
            int charsPerLine = label.getCharsPerLine();
            
            for(int i=0;i*charsPerLine<textToDisplay.length();i++) {
                int endIndex = (i+1)*charsPerLine;
                
                if(endIndex > textToDisplay.length()) {
                    endIndex = textToDisplay.length();
                }
                
                gc.fillText(
                    textToDisplay.substring(
                        i*charsPerLine, 
                        endIndex), 
                    getRenWidth(label.getX()), 
                    getRenHeight(label.getY() + i*(label.getFont().getSize() + label.getLineSpacing())));
            }
            
            
            gc.restore();
        }
    }
}
