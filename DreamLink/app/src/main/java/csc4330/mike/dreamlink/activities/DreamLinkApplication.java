package csc4330.mike.dreamlink.activities;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseUser;

/**
 * Created by Mike on 9/4/15.
 */
public class DreamLinkApplication extends Application {


    @Override
    public void onCreate(){

        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this,"2BpAZP02XxiKszInLiS1ZTdGRf83pCfGSFhDCFX2","2C8XUbmEYZLKoLejypoMKivrhVdVqWciE82PVHOA");

    }

}