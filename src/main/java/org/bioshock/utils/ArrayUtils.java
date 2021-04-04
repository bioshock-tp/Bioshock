package org.bioshock.utils;

import java.util.Arrays;

import org.bioshock.main.App;

public class ArrayUtils {
    /***
     * copies the source array into the target array at the given position
     * @param target 
     * @param source
     * @param i the row to start copying into
     * @param y the column to start copying into
     */
    public static void copyInArray(boolean[][] target, boolean[][] source, int i, int j) {
        for(int a=0;a<source.length&&a+i<target.length;a++) {
            for(int b=0;b<source[0].length&&b+j<target[0].length;b++) {
                target[a+i][b+j] = source[a][b];
            }
        }
    }
    
    /***
     * Logs a 2d array with a line break between each nested array
     * @param traversable
     */
    public static void log2DArray(boolean[][] traversable) {
        StringBuilder sb = new StringBuilder();
        
        for(boolean[] os: traversable) {
            sb.append("\n");
            sb.append(Arrays.toString(os));
        }
        
        App.logger.debug(sb.toString());
    }
    
    /***
     * fill an entire 2D Array with a default value
     * @param <T>
     * @param array
     * @param val
     * @return the filled array
     */
    public static boolean[][] fill2DArray(boolean[][] array, boolean val) {
        for(int i=0;i<array.length;i++) {
            for(int j=0;j<array[0].length;j++) {
                array[i][j] = val;
            }
        }
        
        return array;
    }
}
