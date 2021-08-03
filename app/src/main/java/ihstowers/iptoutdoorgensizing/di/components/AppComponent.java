package ihstowers.iptoutdoorgensizing.di.components;


import android.app.Application;



import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import ihstowers.iptoutdoorgensizing.MyApplication;
import ihstowers.iptoutdoorgensizing.di.builder.ActivityBuilderModule;
import ihstowers.iptoutdoorgensizing.di.module.AppModule;
import ihstowers.iptoutdoorgensizing.di.module.DaoModule;
import ihstowers.iptoutdoorgensizing.di.module.RepositoryModule;

@Singleton
@Component(
        modules = {AndroidSupportInjectionModule.class,
                AppModule.class,
                ActivityBuilderModule.class,
                RepositoryModule.class,
                DaoModule.class}
)
public interface AppComponent {


    void inject(MyApplication htaApplication);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }


}