package ihstowers.iptoutdoorgensizing.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;

import ihstowers.iptoutdoorgensizing.dao.repository.SiteRepository;
import ihstowers.iptoutdoorgensizing.domain.Site;

public class SiteViewModel extends AndroidViewModel {

    final SiteRepository siteRepository;
    private MutableLiveData<Site> siteMutableLiveData = new MutableLiveData<>();
    @Inject
    public SiteViewModel(@NonNull Application application, SiteRepository siteRepository) {
        super(application);
        this.siteRepository = siteRepository;
    }

    public void setSite(Site site) {
        siteMutableLiveData.setValue(site);
    }

    public LiveData<List<Site>> loadSites() {
        return siteRepository.loadSites();
    }

    public LiveData<List<Site>> loadSites(String searchText) {
        return siteRepository.loadSites(searchText);
    }
}
