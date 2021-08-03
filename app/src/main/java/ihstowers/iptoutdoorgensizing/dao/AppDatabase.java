package ihstowers.iptoutdoorgensizing.dao;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.databinding.adapters.Converters;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;



import java.util.concurrent.Executors;

import ihstowers.iptoutdoorgensizing.domain.Site;
import ihstowers.iptoutdoorgensizing.domain.SiteReleve;
import ihstowers.iptoutdoorgensizing.util.IHSConstants;

@Database(entities = {Site.class, SiteReleve.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;


    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, IHSConstants.DATABASE_NAME)
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            //TODO remove
                            .fallbackToDestructiveMigration()
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Executors.newSingleThreadScheduledExecutor().execute(() -> {
                                        INSTANCE.siteDao().insertAll(Site.readData(context));
                                    });
                                }
                            })
                            .build();
        }
        return INSTANCE;
    }


    public abstract SiteDao siteDao();

    public abstract SiteReleveDao siteReleveDao();

}