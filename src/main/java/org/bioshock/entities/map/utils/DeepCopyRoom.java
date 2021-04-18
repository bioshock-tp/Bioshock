package org.bioshock.entities.map.utils;

import org.bioshock.entities.map.Room;
import org.bioshock.utils.DeepCopy;

public class DeepCopyRoom extends DeepCopy<Room>{

    @Override
    public Room deepCopy(Room obj) {
        return obj.deepCopy();
    }

}
