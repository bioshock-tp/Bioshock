package org.bioshock.rendering.renderers;

import static org.bioshock.rendering.RenderManager.getRenHeightUnzoomed;
import static org.bioshock.rendering.RenderManager.getRenWidthUnzoomed;

import org.bioshock.entities.LabelEntity;

import javafx.scene.canvas.GraphicsContext;

public class LabelRenderer implements Renderer {
    
    public static <E extends LabelEntity> void render(
            GraphicsContext gc,
            E label
        ) {
        if(label.isDisplayed()) {
            gc.save();
            gc.setFont(label.getFont());            
            gc.setFill(label.getColour());
            
            String textToDisplay = label.getStringBuilder().toString();
            String lines[] = textToDisplay.split("\\r?\\n");
//            App.logger.debug(textToDisplay);
            int charsPerLine = label.getCharsPerLine();
            
            int j=0;
            for(String line: lines) {
                if(line.charAt(0) == ' ') {
                    line = line.substring(1);
                }
                for(int i=0;i*charsPerLine<line.length();i++) {
                    int endIndex = (i+1)*charsPerLine;
                    
                    if(endIndex > line.length()) {
                        endIndex = line.length();
                    }
                    gc.setFont(label.getFont());
                                        
                    gc.fillText(
                        line.substring(
	                        i*charsPerLine, 
	                        endIndex), 
                        getRenWidthUnzoomed(label.getX()), 
                        getRenHeightUnzoomed(label.getY() + j*(label.getFont().getSize() + label.getLineSpacing())));
                    j++;
                }
            }
            
            
            gc.restore();
        }
    }
}
