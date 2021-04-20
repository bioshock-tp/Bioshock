package org.bioshock.rendering.renderers;

import static org.bioshock.rendering.RenderManager.getRenHeight;
import static org.bioshock.rendering.RenderManager.getRenWidth;

import javafx.scene.text.Font;
import org.bioshock.entities.LabelEntity;
import org.bioshock.main.App;

import javafx.scene.canvas.GraphicsContext;

public class LabelRenderer implements Renderer {
    private LabelRenderer() {}

    public static <E extends LabelEntity> void render(

            GraphicsContext gc,
            E label
        ) {
        try {
            if(label.isDisplayed()) {
                gc.save();
                gc.setFont(label.getFont());            
                gc.setFill(label.getColor());
                
                String textToDisplay = label.getStringBuilder().toString();
                String lines[] = textToDisplay.split("\\r?\\n");
//                App.logger.debug(textToDisplay);
                int charsPerLine = label.getCharsPerLine();
                
                int j=0;
                for(String line: lines) {
                    if(!line.isEmpty()) {
                        if(line.charAt(0) == ' ') {
                            line = line.substring(1);
                        }
                        for(int i=0;i*charsPerLine<line.length();i++) {
                            int endIndex = (i+1)*charsPerLine;
                            
                            if(endIndex > line.length()) {
                                endIndex = line.length();
                            }
    
                            gc.setFont(new Font(getRenHeight(label.getFont().getSize())));
                                        
                            gc.fillText(
                                    line.substring(
                                    i*charsPerLine, 
                                    endIndex), 
                                getRenWidth(label.getX()), 
                                getRenHeight(label.getY() + j*(label.getFont().getSize() + label.getLineSpacing())));
                            j++;
                        }
                    }
                }
                
                gc.restore();
            }
        } catch (Exception e) {
            App.logger.debug("Error in Label Rederer: " + e.toString());

        }
        
    
        
    }
}
