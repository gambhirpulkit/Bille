package in.bille.app.merchant;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

/**
 * Created by SHUBHAM on 27-01-2016.
 */
public class MyApplication extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


}
