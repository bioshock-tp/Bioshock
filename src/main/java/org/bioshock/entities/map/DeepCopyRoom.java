package org.bioshock.entities.map;

import org.bioshock.utils.DeepCopy;

public class DeepCopyRoom extends DeepCopy<Room>{

    @Override
    public Room deepCopy(Room obj) {
        return obj.deepCopy();
    }

}
