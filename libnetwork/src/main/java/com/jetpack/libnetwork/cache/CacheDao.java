package com.jetpack.libnetwork.cache;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

/**
 * @author dhl
 * @version V1.0
 * @Title: CacheDao
 * @Package
 * @Description: CacheDao
 * @date 2022 0425
 */
@Dao
public interface CacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long save(Cache cache);

    @Query("select *from cache where `key`= :key")
    Cache getCache(String key);

    @Delete
    int delete(Cache cache);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(Cache cache);


}
