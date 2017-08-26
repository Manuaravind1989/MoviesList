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
import com.awok.movieslist.networkManager.ServiceManager;
import com.awok.movieslist.utilities.AppConstants;

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
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if(rootView!=null){
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
        getPopularMovies();

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
                    MovieListAdapter adapter = new MovieListAdapter(getActivity(), response.body().getResults());
                    recyclerView.setAdapter(adapter);
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
}
