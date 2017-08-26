package com.awok.movieslist.popular;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.awok.movieslist.HomeActivity;
import com.awok.movieslist.R;
import com.awok.movieslist.db.DatabaseHandler;
import com.awok.movieslist.moviesDetails.MoviesDetailModel;
import com.awok.movieslist.networkManager.ServiceManager;
import com.awok.movieslist.utilities.AppConstants;
import com.awok.movieslist.utilities.AppUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Manu on 8/24/2017.
 */

public class FragmentPopular extends Fragment {
    private RecyclerView recyclerView;
    private TextView noResultTV;
    private View rootView;
    private HomeActivity homeActivity;
    ProgressDialog progressDialog;
    DatabaseHandler databaseHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity) getActivity();
        databaseHandler = new DatabaseHandler(homeActivity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if (rootView != null) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_popular, null);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleView);
        noResultTV = (TextView) rootView.findViewById(R.id.noResultFoundTV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("Movie List"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        // Display Progress Dialog
        if (AppUtils.isConnected(homeActivity)) {
            getPopularMovies();
        } else {
            List<PopularMoviesModel.ResultsEntity> popularMoviesModels = new ArrayList<PopularMoviesModel.ResultsEntity>();
            for (MoviesDetailModel moviesDetailModel : databaseHandler.getFavouriteMovieList(0)) {
                PopularMoviesModel.ResultsEntity resultsEntity = new PopularMoviesModel.ResultsEntity();
                resultsEntity.setId(moviesDetailModel.getId());
                resultsEntity.setTitle(moviesDetailModel.getTitle());
                resultsEntity.setRelease_date(moviesDetailModel.getRelease_date());
                resultsEntity.setOriginal_title(moviesDetailModel.getOriginal_title());
                resultsEntity.setOverview(moviesDetailModel.getOverview());
                resultsEntity.setPoster_path(moviesDetailModel.getPoster_path());
                resultsEntity.setBackdrop_path(moviesDetailModel.getBackdrop_path());
                resultsEntity.setPopularity(moviesDetailModel.getPopularity());
                popularMoviesModels.add(resultsEntity);
            }
            if(popularMoviesModels.size()>0) {
                MovieListAdapter adapter = new MovieListAdapter(getActivity(), popularMoviesModels, 0);
                recyclerView.setAdapter(adapter);
            }else{
                noResultTV.setVisibility(View.VISIBLE);
                noResultTV.setText(getString(R.string.noResultFound));
                recyclerView.setVisibility(View.GONE);
            }
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        homeActivity.updateToolbarTitle(getString(R.string.popular));
    }

    private void getPopularMovies() {
        progressDialog.show();
        ServiceManager serviceManager = new ServiceManager();
        Call<PopularMoviesModel> call = serviceManager.apiService.getPopularMovies(AppConstants.API_KEY);
        call.enqueue(new Callback<PopularMoviesModel>() {
            @Override
            public void onResponse(Call<PopularMoviesModel> call, Response<PopularMoviesModel> response) {
                //  Log.e("Service Length: ", response.body().getResults().size() + "");
                //  if (response.isSuccessful() && response.body() != null) {
                if (response.body().getResults().size() > 0) {
                    MovieListAdapter adapter = new MovieListAdapter(getActivity(), response.body().getResults(),0);
                    recyclerView.setAdapter(adapter);
                    insertDataToDB(response.body());
                } else {
                    noResultTV.setVisibility(View.VISIBLE);
                    noResultTV.setText(getString(R.string.noResultFound));
                    recyclerView.setVisibility(View.GONE);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<PopularMoviesModel> call, Throwable t) {
                Log.e("Error", "" + t.toString());
                noResultTV.setVisibility(View.VISIBLE);
                noResultTV.setText(getString(R.string.servicenotAvalibale));
                recyclerView.setVisibility(View.GONE);
                progressDialog.dismiss();
            }
        });
    }

    private void insertDataToDB(PopularMoviesModel popularMoviesModel) {

        for (PopularMoviesModel.ResultsEntity resultsEntity : popularMoviesModel.getResults()) {
            MoviesDetailModel moviesDetailModel = new MoviesDetailModel();
            moviesDetailModel.setId(resultsEntity.getId());
            moviesDetailModel.setTitle(resultsEntity.getTitle());
            moviesDetailModel.setRelease_date(resultsEntity.getRelease_date());
            moviesDetailModel.setOriginal_title(resultsEntity.getOriginal_title());
            moviesDetailModel.setOverview(resultsEntity.getOverview());
            moviesDetailModel.setVote_count(resultsEntity.getVote_count());
            moviesDetailModel.setVote_average(resultsEntity.getVote_average());
            //  moviesDetailModel.setBudget(resultsEntity.getBudget());
            //  moviesDetailModel.setRevenue(resultsEntity.getTitle());
            //   moviesDetailModel.setGenres(resultsEntity.getTitle());
            //  moviesDetailModel.setStatus(resultsEntity.getTitle());
            moviesDetailModel.setPopularity(resultsEntity.getPopularity());
            // moviesDetailModel.setHomepage(resultsEntity.getTitle());
            moviesDetailModel.setPoster_path(resultsEntity.getPoster_path());
            moviesDetailModel.setBackdrop_path(resultsEntity.getBackdrop_path());

            databaseHandler.addMovies(moviesDetailModel,0);
        }

    }


}
