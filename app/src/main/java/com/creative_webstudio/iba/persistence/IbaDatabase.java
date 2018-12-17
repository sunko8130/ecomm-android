package com.creative_webstudio.iba.persistence;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class IbaDatabase extends RoomDatabase {

    private static final String DB_NAME = "HealthCare_DB";
    private static IbaDatabase INSTANCE;
    public IbaDatabase() {

    }

//    public abstract IbaDatabase HCInfoDao();

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {
    }
    
    public class Cart {
        private int productId;
        private int unitId;
        private int itemQuantity;
    }

}
