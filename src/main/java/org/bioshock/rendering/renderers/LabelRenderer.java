package org.bioshock.rendering.renderers;

import static org.bioshock.rendering.RenderManager.getRenHeight;
import static org.bioshock.rendering.RenderManager.getRenWidth;

import org.bioshock.entities.LabelEntity;

import javafx.scene.canvas.GraphicsContext;

public class LabelRenderer implements Renderer {
    private LabelRenderer() {}

    public static <E extends LabelEntity> void render(
        GraphicsContext gc,
        E label
    ) {
        if (label.isDisplayed()) {
            gc.save();
            gc.setFont(label.getFont());
            gc.setFill(label.getColour());

            String textToDisplay = label.getString();
            int charsPerLine = label.getCharsPerLine();

            for (int i = 0; i * charsPerLine < textToDisplay.length(); i++) {
                int endIndex = (i + 1) * charsPerLine;

                if (endIndex > textToDisplay.length()) {
                    endIndex = textToDisplay.length();
                }

                gc.fillText(
                    textToDisplay.substring(
                        i * charsPerLine,
                        endIndex
                    ),
                    getRenWidth(label.getX()),
                    getRenHeight(
                        label.getY() + i * (
                            label.getFont().getSize() + label.getLineSpacing()
                        )
                    )
                );
            }

            gc.restore();
        }
    }
}
