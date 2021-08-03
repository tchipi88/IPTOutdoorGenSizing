package ihstowers.iptoutdoorgensizing.dao.repository;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import ihstowers.iptoutdoorgensizing.dao.SiteDao;
import ihstowers.iptoutdoorgensizing.domain.Site;

public class SiteRepository {

    final private SiteDao dao;
    final private Executor executor;

    @Inject
    public SiteRepository(SiteDao dao, Executor executor) {
        this.dao = dao;
        this.executor = executor;
    }

    public LiveData<List<Site>> loadSites() {
        return dao.getAll();
    }

    public LiveData<List<Site>> loadSites(String searchText) {
        return dao.getAll(searchText);
    }


    public void insert(Site site) {
        dao.insert(site);
    }
}
