package com.awok.movieslist.networkManager;

import android.util.Log;

import com.awok.movieslist.utilities.AppConstants;

/**
 * Created by Manu on 8/24/2017.
 */
public class ServiceManager {

  public  ServiceInterface apiService;
    public ServiceManager(){
Log.e("Constructor", "sdfdf");
        apiService   =
                ApiClient.getClient(AppConstants.BASE_URL).create(ServiceInterface.class);
    }

}
