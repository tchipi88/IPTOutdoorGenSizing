package ihstowers.iptoutdoorgensizing.di.module;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ihstowers.iptoutdoorgensizing.dao.AppDatabase;
import ihstowers.iptoutdoorgensizing.dao.SiteDao;
import ihstowers.iptoutdoorgensizing.dao.SiteReleveDao;

@Module
public class DaoModule {
    @Provides
    @Singleton
    public SiteDao provideSiteDao(Application app) {
        AppDatabase db = AppDatabase.getAppDatabase(app);
        return db.siteDao();
    }

    @Provides
    @Singleton
    public SiteReleveDao provideSiteReleveDao(Application app) {
        AppDatabase db = AppDatabase.getAppDatabase(app);
        return db.siteReleveDao();
    }

}