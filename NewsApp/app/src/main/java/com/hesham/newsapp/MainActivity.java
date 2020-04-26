package com.hesham.newsapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import java.util.List;
import android.app.LoaderManager;
import android.content.Loader;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements NewsAdapter.ListItemClickListener{

    private static  final String api_key = "5c2af159-90da-4de6-9e95-c691d20aab62";
    private static final String THEGUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search?&show-tags=contributor&api-key=" + api_key;
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private static final int NEWS_LADER_ID = 1;
    private View loadingIndicator;
    private TextView empty_state;
    private static boolean isConnected;
    private static List<News> news;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            isConnected = isNetworkAvailable();

            loadingIndicator = findViewById(R.id.loading_indicator);

            if (isConnected) {
                empty_state = findViewById(R.id.empty_view);

                recyclerView = findViewById(R.id.recyclerview);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

                recyclerView.setHasFixedSize(true);

                recyclerView.setLayoutManager(linearLayoutManager);

                LoaderManager loaderManager = getLoaderManager();

                loaderManager.initLoader(NEWS_LADER_ID, null, this);
            } else {
                loadingIndicator.setVisibility(View.GONE);
                empty_state = findViewById(R.id.empty_view);
                empty_state.setText(R.string.no_internet_connection);
            }

    }

    /**
     * check if device connected to the internet or not
     * @return
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String tobic = sharedPrefs.getString(
                getString(R.string.tobic_key),
                getString(R.string.most_recent));


        /*most recent is the default value*/
        if(tobic.equals("most recent")){
            return new NewsLoader(this, THEGUARDIAN_REQUEST_URL);
        }

        Uri baseUri = Uri.parse(THEGUARDIAN_REQUEST_URL);
        Uri.Builder builder = baseUri.buildUpon();

        builder.appendQueryParameter("q",tobic);

        return new NewsLoader(this, builder.toString());
    }


    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {

        news = data;
        loadingIndicator.setVisibility(View.GONE);

        if(newsAdapter != null)
           newsAdapter.clear();

        if (data != null && !data.isEmpty()) {
            newsAdapter = new NewsAdapter(data, data.size(), (NewsAdapter.ListItemClickListener) this);

            recyclerView.setAdapter(newsAdapter);
        }
        else
            empty_state.setText(R.string.no_news_found);
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        if(newsAdapter != null)
        newsAdapter.clear();
    }

    @Override
    public void onListItemClick(int position) {
        Uri newsUri = Uri.parse(news.get(position).getUrl());
        Intent website = new Intent(Intent.ACTION_VIEW, newsUri);
        startActivity(website);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
