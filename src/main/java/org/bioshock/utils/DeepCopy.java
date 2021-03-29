package org.bioshock.utils;

public abstract class DeepCopy<T> {
    /***
     * provides a deep copy of the given object
     * i.e. a new object with all the same values as the old one
     * @param the object to make a deep copy of
     * @return a copy of the object this is called on
     */
    public abstract T deepCopy(T obj);
}
