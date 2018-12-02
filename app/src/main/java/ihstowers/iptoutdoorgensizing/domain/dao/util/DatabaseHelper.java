package ihstowers.iptoutdoorgensizing.domain.dao.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import ihstowers.iptoutdoorgensizing.R;
import ihstowers.iptoutdoorgensizing.domain.Site;
import ihstowers.iptoutdoorgensizing.domain.SiteReleve;

import static ihstowers.iptoutdoorgensizing.IHSConstants.DATABASE_NAME;
import static ihstowers.iptoutdoorgensizing.IHSConstants.DATABASE_VERSION;

/**
 * Created by tchipi on 3/19/18.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private Dao<Site, String> siteDao;
    private Dao<SiteReleve, Long> siteReleveDao;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {

            // Create tables. This onCreate() method will be invoked only once of the application life time i.e. the first time when the application starts.
            TableUtils.createTable(connectionSource, Site.class);
            TableUtils.createTable(connectionSource, SiteReleve.class);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            // In case of change in database of next version of application, please increase the value of DATABASE_VERSION variable, then this method will be invoked
            //automatically. Developer needs to handle the upgrade logic here, i.e. create a new table or a new column to an existing table, take the backups of the
            // existing database etc.

            TableUtils.createTable(connectionSource, Site.class);
            TableUtils.createTable(connectionSource, SiteReleve.class);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version " + oldVersion + " to new "
                    + newVersion, e);
        }

    }

    public Dao<Site, String> getSiteDao() throws SQLException {
        if (siteDao == null) {
            siteDao = getDao(Site.class);
        }
        return siteDao;
    }

    public Dao<SiteReleve, Long> getSiteReleveDao() throws SQLException {
        if (siteReleveDao == null) {
            siteReleveDao = getDao(SiteReleve.class);
        }
        return siteReleveDao;
    }

}
