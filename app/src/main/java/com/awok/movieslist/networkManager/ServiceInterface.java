package com.awok.movieslist.networkManager;

import com.awok.movieslist.moviesDetails.MoviesDetailModel;
import com.awok.movieslist.popular.PopularMoviesModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by Manu on 8/24/2017.
 */
public interface ServiceInterface {


    /// POPULAR MOVIES
    @GET("movie/popular?")
    Call<PopularMoviesModel> getPopularMovies(@Query("api_key") String api_key);

    /// POPULAR MOVIES
    @GET("search/movie?")
    Call<PopularMoviesModel> getSearchMoviesResult(@Query("api_key") String api_key,@Query("query") String query);

    @GET("movie/{movie_id}?")
    Call<MoviesDetailModel> getMovieDetails(@Path("movie_id") String movie_id, @Query("api_key") String api_key);


    /// TOP MOVIES
    @GET("movie/top_rated?")
    Call<PopularMoviesModel> getTopMovies(@Query("api_key") String api_key);


}
