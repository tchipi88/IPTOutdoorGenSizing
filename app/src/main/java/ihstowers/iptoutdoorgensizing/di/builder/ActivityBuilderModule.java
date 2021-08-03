package ihstowers.iptoutdoorgensizing.di.builder;



import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import ihstowers.iptoutdoorgensizing.MainActivity;

@Module
public abstract class ActivityBuilderModule {


    @ContributesAndroidInjector(modules = FragmentBuilderModule.class)
    abstract MainActivity mainActivity();


}
