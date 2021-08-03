package ihstowers.iptoutdoorgensizing.di.module;


import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;



import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import ihstowers.iptoutdoorgensizing.ui.viewmodel.SiteReleveViewModel;
import ihstowers.iptoutdoorgensizing.ui.viewmodel.SiteViewModel;
import ihstowers.iptoutdoorgensizing.ui.viewmodel.ViewModelFactory;


@Module
public abstract class ViewModelModule {





    @Binds
    @IntoMap
    @ViewModelKey(SiteReleveViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsSiteReleveViewModel(SiteReleveViewModel siteReleveViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SiteViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsSiteViewModel(SiteViewModel siteViewModel);


    @Binds
    @SuppressWarnings("unused")
    abstract ViewModelProvider.Factory bindsViewModelFactory(ViewModelFactory viewModelFactory);
}
