package com.jetpack.libnetwork.cache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * @author dhl
 * @version V1.0
 * @Title: Cache
 * @Package $
 * @Description: Cache
 * @date 2022 0425
 */

@Entity(tableName = "cache")
public class Cache  implements Serializable {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    public String key ;

    public byte[] data;


}
