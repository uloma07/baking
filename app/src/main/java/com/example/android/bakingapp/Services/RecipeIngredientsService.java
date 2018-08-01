package com.example.android.bakingapp.Services;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.bakingapp.Models.Recipe;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RecipeWidgetProvider;
import com.example.android.bakingapp.utils.JsonUtils;
import com.example.android.bakingapp.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class RecipeIngredientsService extends IntentService {

    Recipe[] recipes = {};

    public static final String ACTION_GET_RECIPES = "com.example.android.bakingapp.action.update_recipe_widget";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public RecipeIngredientsService(String name) {
        super("RECIPE_INGR_SERVICE");
    }

    public RecipeIngredientsService() {
        super("RECIPE_INGR_SERVICE");
    }

    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, startId, startId);
        return START_STICKY;
    }


    public static void startGetRecipes(Context context) {
        Intent intent = new Intent(context, RecipeIngredientsService.class);
        intent.setAction(ACTION_GET_RECIPES);
        makeRecipeSearchQueryClone(context);
        context.startService(intent);
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_RECIPES.equals(action)) {
                makeRecipeSearchQuery();
            }
        }
    }

    public void doUpdate(){
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(RecipeIngredientsService.this);
        int[] appWidgetIds = widgetManager.getAppWidgetIds(new ComponentName(RecipeIngredientsService.this, RecipeWidgetProvider.class));
        widgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_gridview_grid);
        RecipeWidgetProvider.updateRecipeWidgets(RecipeIngredientsService.this, widgetManager, recipes, appWidgetIds);

    }

    public void doUpdate(Context c){
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(c);
        int[] appWidgetIds = widgetManager.getAppWidgetIds(new ComponentName(c, RecipeWidgetProvider.class));

        widgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_gridview_grid);
        RecipeWidgetProvider.updateRecipeWidgets(c, widgetManager, recipes, appWidgetIds);
    }

    private static void makeRecipeSearchQueryClone(Context context) {

        if(NetworkUtils.isConnectedToInternet(context)){
            URL SearchUrl = NetworkUtils.buildUrl();
            RecipeIngredientsService ris = new RecipeIngredientsService("Recipe Ing Serv");
            ris.new RecipeQueryTask(context).execute(SearchUrl);
        }
    }


    private void makeRecipeSearchQuery() {

        if(NetworkUtils.isConnectedToInternet(this)){
            URL SearchUrl = NetworkUtils.buildUrl();
            new RecipeQueryTask().execute(SearchUrl);
        }
    }

    public class RecipeQueryTask extends AsyncTask<URL, Void, Recipe[]> {

        Context con;

        RecipeQueryTask(){

        }

        RecipeQueryTask(Context c){
            con = c;
        }

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
            if(recipes != null){
                if(con == null) {
                    doUpdate();
                }
                else{
                    doUpdate(con);
                }
            }
        }
    }
}
