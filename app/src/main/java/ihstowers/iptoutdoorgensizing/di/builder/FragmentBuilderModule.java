package ihstowers.iptoutdoorgensizing.di.builder;



import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import ihstowers.iptoutdoorgensizing.ui.fragment.GenSizingCalculFragment;
import ihstowers.iptoutdoorgensizing.ui.fragment.GenSizingResultFragment;
import ihstowers.iptoutdoorgensizing.ui.fragment.SiteListFragment;

@Module
public abstract class FragmentBuilderModule {

    @ContributesAndroidInjector
    abstract GenSizingCalculFragment genSizingCalculFragment();
    @ContributesAndroidInjector
    abstract GenSizingResultFragment genSizingResultFragment();
    @ContributesAndroidInjector
    abstract SiteListFragment siteListFragment();


}


