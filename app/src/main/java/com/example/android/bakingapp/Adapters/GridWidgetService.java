package com.example.android.bakingapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.example.android.bakingapp.Models.Recipe;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RecipeDetailsActivity;
import com.example.android.bakingapp.Services.RecipeIngredientsService;
import com.example.android.bakingapp.utils.JsonUtils;
import com.example.android.bakingapp.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class GridWidgetService extends RemoteViewsService{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }

    public static void setRecipes(Recipe[] r){
        GridRemoteViewsFactory.recipes = r;
    }
}


class GridRemoteViewsFactory implements RemoteViewsFactory {

    Context mcontext;
    public static Recipe[] recipes;

    public GridRemoteViewsFactory(Context appContext){
        mcontext = appContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

        if(NetworkUtils.isConnectedToInternet(mcontext)){
            URL SearchUrl = NetworkUtils.buildUrl();
            String SearchResults = null;
            try {
                SearchResults = NetworkUtils.getResponseFromHttpUrl(SearchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //put items into a Recipe[]
            Recipe[] results = JsonUtils.parseRecipeJson(SearchResults);
            if(results != null){
                recipes = results;
            }
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if(recipes == null) return  0;
        return recipes.length;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if(recipes == null || recipes.length == 0){
            return null;
        }

        Recipe r = recipes[position];

        RemoteViews views = new RemoteViews(mcontext.getPackageName(), R.layout.testrecipeitem);
        views.setTextViewText(R.id.tv_recipenames, r.getname());
        views.setTextViewText(R.id.tv_servingss, String.valueOf(r.getservings()));
        views.setViewVisibility(R.id.tv_widget_ingredientss, View.VISIBLE);
        views.setTextViewText(R.id.tv_widget_ingredientss, r.getIngredientslist());

        Bundle extras = new Bundle();
        extras.putParcelable(RecipeDetailsActivity.RECIPE, r);
        Intent fillIntent = new Intent();
        fillIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.thes_recipe_item,fillIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
