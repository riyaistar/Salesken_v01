package ai.salesken.v1.utils;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ai.salesken.v1.pojo.ContactPojo;

@Database(entities = {ContactPojo.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;
    public abstract ContactDao contactDao();
    public static  synchronized  AppDatabase getInstance(Context context){
        if(instance == null ){
            instance = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,"salesken_v1").fallbackToDestructiveMigration().build();
        }
        return instance;
    }

}
