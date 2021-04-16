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
     * copies the source array into the target array at the given position
     * @param target 
     * @param source
     * @param i the row to start copying into
     * @param y the column to start copying into
     */
    public static void copyInArray(Object[][] target, Object[][] source, int i, int j) {
        for(int a=0;a<source.length&&a+i<target.length;a++) {
            for(int b=0;b<source[0].length&&b+j<target[0].length;b++) {
                target[a+i][b+j] = source[a][b];
            }
        }
    }
    
    /***
     * Logs a 2d array with a line break between each nested array
     * @param array
     */
    public static void log2DArray(boolean[][] array) {
        StringBuilder sb = new StringBuilder();
        
        for(boolean[] os: array) {
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
    
    /***
     * attempt to get the value at the position [i][j] in the array
     * if no object exists or the position is ouut of bounds return null
     * @param <T> The type of the array
     * @param array 
     * @param i 
     * @param j
     * @return
     */
    public static <T> T safeGet(T[][] array, int i, int j) {
    	if(array == null ||
    		i<0 || array.length<=i ||
    		j<0 || array[0].length<=j) {
    		return null;
    	}
    	return array[i][j];
    }
}
