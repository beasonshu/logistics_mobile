package com.ia.logistics.uitis;

import android.os.Build;

/**
 * Created by xuetenglong on 2017/12/13.
 */

public class VersionsUtils {


    public static boolean beyondMVersion(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)return true;
        return false;
    }



}
