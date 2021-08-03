package ihstowers.iptoutdoorgensizing.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ihstowers.iptoutdoorgensizing.domain.Site;

@Dao
public interface SiteDao {

    @Insert
    void insert(Site Site);


    @Query("SELECT * FROM Site")
    LiveData<List<Site>> getAll();

    @Query("SELECT * FROM Site s where s.name like :searchtext")
    LiveData<List<Site>> getAll(String searchtext);


    @Insert
    void insertAll(Site... dataEntities);
}
