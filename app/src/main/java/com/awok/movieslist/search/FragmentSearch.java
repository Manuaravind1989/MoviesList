package com.awok.movieslist.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.awok.movieslist.HomeActivity;
import com.awok.movieslist.R;
import com.awok.movieslist.networkManager.ServiceManager;
import com.awok.movieslist.popular.MovieListAdapter;
import com.awok.movieslist.popular.PopularMoviesModel;
import com.awok.movieslist.utilities.AppConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Manu on 8/24/2017.
 */

public class FragmentSearch extends Fragment {

    HomeActivity homeActivity;
    private RecyclerView recyclerView;
    private TextView noResultFoundTV;
    private View rootView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if(rootView!=null){
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_search, null);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycleView);
        noResultFoundTV = (TextView)rootView.findViewById(R.id.noResultFoundTV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        noResultFoundTV.setText(getString(R.string.searchyourmovies));
        noResultFoundTV.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity) getActivity();
        setHasOptionsMenu(true);

    }
    @Override
    public void onResume() {
        super.onResume();
        homeActivity.updateToolbarTitle(getString(R.string.search));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            //Do whatever you want to do
            homeActivity.searchView.showSearch();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void searchResult(String queryText) {
        ServiceManager serviceManager = new ServiceManager();
        Call<PopularMoviesModel> call = serviceManager.apiService.getSearchMoviesResult(AppConstants.API_KEY, queryText);
        call.enqueue(new Callback<PopularMoviesModel>() {
            @Override
            public void onResponse(Call<PopularMoviesModel> call, Response<PopularMoviesModel> response) {
              //  Log.e("Service Length: ", response.body().getResults().size() + "");

                //  if (response.isSuccessful() && response.body() != null) {
                if(response.body().getResults().size()>0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    MovieListAdapter adapter = new MovieListAdapter(getActivity(), response.body().getResults());
                    recyclerView.setAdapter(adapter);
                    noResultFoundTV.setVisibility(View.GONE);
                }else{
                    noResultFoundTV.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }


            }

            @Override
            public void onFailure(Call<PopularMoviesModel> call, Throwable t) {
                Log.e("Error", "" + t.toString());
                noResultFoundTV.setVisibility(View.VISIBLE);
                noResultFoundTV.setText(getString(R.string.servicenotAvalibale));
                recyclerView.setVisibility(View.GONE);
            }
        });
    }
}
