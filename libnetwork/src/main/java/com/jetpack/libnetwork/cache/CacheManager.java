package com.jetpack.libnetwork.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author dhl
 * @version V1.0
 * @Title: CacheManger
 * @Package $
 * @Description: CacheManger
 * @date 2022 0425
 */
public class CacheManager {
    public static <T> void save(String key, T body) {
        Cache cache = new Cache();
        cache.key = key;
        cache.data = toByteArray(body);
        CacheDataBase.get().getCacheDao().save(cache);
    }

    public static Object getCache(String key) {
        Cache cache = CacheDataBase.get().getCacheDao().getCache(key);
        if (cache != null && cache.data != null) {
            return toObject(cache.data);
        }
        return null;
    }

    /**
     * byte[] --> object
     * @param data
     * @return
     */
    private static Object toObject(byte[] data) {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(data);
            ois = new ObjectInputStream(bais);
            return ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bais != null) {
                    bais.close();
                }
                if (ois != null) {
                    ois.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return null;


    }

    /**
     * object --> byte
     * @param body
     * @param <T>
     * @return
     */
    private static <T> byte[] toByteArray(T body) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {

            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(body);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new byte[0];

    }
}
