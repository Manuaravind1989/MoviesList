package com.awok.movieslist.moviesDetails;

import android.app.ProgressDialog;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.awok.movieslist.R;
import com.awok.movieslist.networkManager.ServiceManager;
import com.awok.movieslist.utilities.AppConstants;
import com.facebook.drawee.view.SimpleDraweeView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Manu on 8/25/2017.
 */

public class MovieDetailsActivity extends AppCompatActivity {
    private SimpleDraweeView posterImageView, backDropImageView;
    private TextView titleTV, releaseDateTV, overviewTV, voteCountTV, voteAverageTV, originalTitleTV, popularityTV,
    budgetTv, revenueTV,genresTV, statusTV, homePageTV;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.awok.movieslist.R.layout.activity_moviedetails);


        posterImageView = (SimpleDraweeView) findViewById(R.id.posterImage);
        backDropImageView = (SimpleDraweeView) findViewById(R.id.backDropImage);
        titleTV = (TextView) findViewById(R.id.titleTV);
        releaseDateTV = (TextView) findViewById(R.id.releaseDateTV);
        overviewTV = (TextView) findViewById(R.id.overViewTV);
        voteCountTV = (TextView) findViewById(R.id.voteCountTV);
        voteAverageTV = (TextView) findViewById(R.id.voteAverageTV);
        originalTitleTV = (TextView) findViewById(R.id.originalTitleTV);
        popularityTV = (TextView) findViewById(R.id.popularityTV);
        budgetTv = (TextView) findViewById(R.id.budgetTV);
        revenueTV = (TextView)findViewById(R.id.revenueTV);
        genresTV = (TextView)findViewById(R.id.genresTV);
        statusTV = (TextView)findViewById(R.id.statusTV);
        homePageTV = (TextView)findViewById(R.id.homePageTV);
        homePageTV.setPaintFlags(homePageTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        Uri uri = Uri.parse(AppConstants.IMAGE_URL + getIntent().getStringExtra("IMAGE_URL"));
        posterImageView.setImageURI(uri);
        titleTV.setText(getIntent().getStringExtra("TITLE"));
        releaseDateTV.setText(getIntent().getStringExtra("RELEASE_DATE"));


        //  popularityTV.setText(getIntent().getStringExtra("POPULARITY")+"");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("Movie List"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        getPopularMovies();
    }


    private void getPopularMovies() {
        progressDialog.show();
        ServiceManager serviceManager = new ServiceManager();
        Call<MoviesDetailModel> call = serviceManager.apiService.getMovieDetails(getIntent().getStringExtra("ID"), AppConstants.API_KEY);
        call.enqueue(new Callback<MoviesDetailModel>() {
            @Override
            public void onResponse(Call<MoviesDetailModel> call, Response<MoviesDetailModel> response) {
               // Log.e("Service Length: ", response.body() + "");
                //  if (response.isSuccessful() && response.body() != null) {
                overviewTV.setText(response.body().getOverview());
                voteCountTV.setText(response.body().getVote_count() + "");
                voteAverageTV.setText(response.body().getVote_average() + "");
                originalTitleTV.setText(response.body().getOverview());
                popularityTV.setText(response.body().getPopularity()+"");
                budgetTv.setText(response.body().getBudget()+"");
                revenueTV.setText(response.body().getRevenue()+"");
                for(MoviesDetailModel.GenresEntity genresEntity : response.body().getGenres()){
                    genresTV.setText(genresTV.getText().toString()+"- "+genresEntity.getName()+"\n");
                }
                statusTV.setText(response.body().getStatus());
                homePageTV.setText(response.body().getHomepage());
                Uri uri1 = Uri.parse(AppConstants.IMAGE_URL + response.body().getBackdrop_path());
                backDropImageView.setImageURI(uri1);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<MoviesDetailModel> call, Throwable t) {
                Log.e("Error", "" + t.toString());

                progressDialog.dismiss();
            }
        });
    }
}
