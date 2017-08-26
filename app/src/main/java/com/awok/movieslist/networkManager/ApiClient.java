package com.awok.movieslist.networkManager;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Manu on 8/24/2017.
 */
public class ApiClient {
    private static Retrofit retrofit = null;
    public static Retrofit getClient(String baseUrl) {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

//    public static Retrofit getClient() {
//       // if (retrofit == null) {
//            if(URLUtil.isHttpUrl(AppConstants.BASE_URL)){
//                Log.e("BASE URL++++++> ",""+AppConstants.BASE_URL);
//            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//             // set your desired log level
//            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//            // add your other interceptors …
//            // add logging as last interceptor
//            httpClient.addInterceptor(logging);  // <-- this is the important line!
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(AppConstants.BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .client(httpClient.build())
//                    .build();
//        }
//        return retrofit;
//    }
}
