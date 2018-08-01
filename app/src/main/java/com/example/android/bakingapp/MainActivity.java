package com.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.android.bakingapp.Adapters.RecipeAdapter;
import com.example.android.bakingapp.Models.Recipe;
import com.example.android.bakingapp.utils.JsonUtils;
import com.example.android.bakingapp.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener{

    Recipe[] recipes = {};
    RecipeAdapter mRecipeAdapter;
    private RecyclerView mRecipesList;

    boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecipesList = (RecyclerView) findViewById(R.id.rvRecipes);

        LinearLayoutManager recipeslayoutManager = new LinearLayoutManager(this);
        mRecipesList.setLayoutManager(recipeslayoutManager);
        mRecipesList.setHasFixedSize(false);

        //get recipes
        makeRecipeSearchQuery();

        mRecipeAdapter = new RecipeAdapter(this, recipes, this);
        // Attach the adapter to a RecyclerView
        mRecipesList.setAdapter(mRecipeAdapter);


    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        Recipe recipe = recipes[clickedItemIndex];
        Intent intent = new Intent(MainActivity.this, RecipeDetailsActivity.class);
        intent.putExtra("image", recipe.getimage());
        intent.putExtra("servings", recipe.getservings());
        intent.putExtra("steps", recipe.getsteps());
        intent.putExtra("ingredients", recipe.getingredients());
        intent.putExtra("name", recipe.getname());
        intent.putExtra("id", recipe.getId());
        startActivity(intent);

    }

    public void doWidgetUpdate(){
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(MainActivity.this);
        int[] appWidgetIds = widgetManager.getAppWidgetIds(new ComponentName(MainActivity.this, RecipeWidgetProvider.class));
        RecipeWidgetProvider.updateRecipeWidgets(MainActivity.this, widgetManager, recipes, appWidgetIds);
    }

    private void makeRecipeSearchQuery() {

        if(!NetworkUtils.isConnectedToInternet(MainActivity.this)){
            Toast toast = Toast.makeText(MainActivity.this, "Please check internet connection", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        URL SearchUrl = NetworkUtils.buildUrl();
        new RecipeQueryTask().execute(SearchUrl);
    }

    public class RecipeQueryTask extends AsyncTask<URL, Void, Recipe[]> {

        @Override
        protected Recipe[] doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String SearchResults = null;
            try {
                SearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //put items into a Recipe[]
            Recipe[] results = JsonUtils.parseRecipeJson(SearchResults);
            return results;
        }

        @Override
        protected void onPostExecute(Recipe[] result) {
            recipes = result;
            if (result != null) {
                if(mRecipeAdapter == null){
                    mRecipeAdapter = new RecipeAdapter(MainActivity.this, recipes, MainActivity.this);
                }
                mRecipeAdapter.updatetherecipes(result);
                doWidgetUpdate();
            }
        }
    }
}
