package com.jetpack.libnetwork.cache;

import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.jetpack.libcommon.utils.AppGlobals;

/**
 * @author dhl
 * @version V1.0
 * @Title: CacheDataBase
 * @Package $
 * @Description: CacheDataBase
 * @date 2022 0425
 */
@Database(entities = {Cache.class},version = 1,exportSchema = false)
public abstract class CacheDataBase extends RoomDatabase {

    private static  final CacheDataBase database;

    static {
        database =   Room.databaseBuilder(AppGlobals.getApplication(),CacheDataBase.class,"ppjoke_cache")
                 .allowMainThreadQueries().build();

    }


    public abstract  CacheDao getCacheDao();

    public static CacheDataBase get(){
        return database;
    }
}
