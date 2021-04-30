package org.bioshock.rendering.renderers;

import static org.bioshock.rendering.RenderManager.getRenHeight;
import static org.bioshock.rendering.RenderManager.getRenHeightUnzoomed;
import static org.bioshock.rendering.RenderManager.getRenWidthUnzoomed;

import org.bioshock.entities.LabelEntity;
import org.bioshock.main.App;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

public class LabelRenderer implements Renderer {
    private LabelRenderer() {}

    /**
     * Method to render the given entity
     * @param <E>
     * @param gc The graphics context to render the label on
     * @param label The label to render
     */
    public static <E extends LabelEntity> void render(
        GraphicsContext gc,
        E label
    ) {
        try {
            //Only render the label if the label isDisplayed
            if (label.isDisplayed()) {
                gc.save();
                gc.setFont(label.getFont());
                gc.setFill(label.getRendererC().getColour());

                //Split the label on \r and \n
                String textToDisplay = label.getString();
                String[] lines = textToDisplay.split("\\r?\\n");

                int charsPerLine = label.getCharsPerLine();
                
                //For each line 
                for (int i = 0; i < lines.length; i++) {
                    String line = lines[i];
                    
                    if (line.isEmpty()) continue;

                    line = line.stripLeading();
                    
                    //Split the line into sections at maximum charsPerLine characters long
                    for (int j = 0; j * charsPerLine < line.length(); j++) {
                        gc.setFont(new Font(
                            getRenHeight(label.getFont().getSize())
                        ));

                        int endIndex = (j + 1) * charsPerLine > line.length() ? line.length() : (j + 1) * charsPerLine;
                        gc.fillText(
                            line.substring(
                                j * charsPerLine,
                                endIndex
                            ),
                            getRenWidthUnzoomed(label.getX()),
                            getRenHeightUnzoomed(
                                label.getY()
                                    + i * (label.getFont().getSize()
                                    + label.getLineSpacing())
                            )
                        );
                    }
                }

                gc.restore();
            }
        } catch (Exception e) {
            App.logger.debug("Error in Label Renderer: ", e);
        }
    }
}
