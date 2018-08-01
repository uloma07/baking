package com.example.android.bakingapp;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import com.example.android.bakingapp.Adapters.GridWidgetService;
import com.example.android.bakingapp.Models.Recipe;
import com.example.android.bakingapp.Services.RecipeIngredientsService;

import java.util.UUID;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    static Recipe[] recipes;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        int width = -1;
        RemoteViews views;

        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);

        views = getLayout(width, context,appWidgetId);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static RemoteViews getLayout(float width,final Context context, final int appWidgetId){
        RemoteViews views;
        if(width < 300){
            // Construct the RemoteViews object
            views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            views.setOnClickPendingIntent(R.id.appwidget_text,pendingIntent);
            if(recipes != null) {
                views.setTextViewText(R.id.appwidget_text, String.valueOf(recipes.length)+ " Recipes Available");
            }
            else{
                views.setTextViewText(R.id.appwidget_text, "Baking App");
            }
            return views;
        }
        else{
            // Change these
            views = new RemoteViews(context.getPackageName(), R.layout.widget_gridview);

            Intent intent = new Intent(context, GridWidgetService.class);
            views.setRemoteAdapter(R.id.widget_gridview_grid,intent);

            if(recipes != null && recipes.length>0) {

                Recipe recipe = recipes[0];

                views.setTextViewText(R.id.wid_tv_recipename, recipe.getname());
                views.setTextViewText(R.id.wid_tv_servings, String.valueOf(recipe.getservings()));
                views.setTextViewText(R.id.wid_tv_ingredients, recipe.getIngredientslist());
                views.setTextViewText(R.id.items_remaining, recipes.length-1 + " recipes remaining");

                Intent otherRecipesintent = new Intent(context, MainActivity.class);
                PendingIntent otherRecipespendingIntent = PendingIntent.getActivity(context, UUID.randomUUID().hashCode(), otherRecipesintent, PendingIntent.FLAG_UPDATE_CURRENT);

                views.setOnClickPendingIntent(R.id.items_remaining,otherRecipespendingIntent);


                Intent recipeDetailsintent = new Intent(context, RecipeDetailsActivity.class);
                recipeDetailsintent.putExtra("image", recipe.getimage());
                recipeDetailsintent.putExtra("servings", recipe.getservings());
                recipeDetailsintent.putExtra("steps", recipe.getsteps());
                recipeDetailsintent.putExtra("ingredients", recipe.getingredients());
                recipeDetailsintent.putExtra("name", recipe.getname());
                recipeDetailsintent.putExtra("id", recipe.getId());
                PendingIntent recipeDetailspendingIntent = PendingIntent.getActivity(context, UUID.randomUUID().hashCode(), recipeDetailsintent, PendingIntent.FLAG_UPDATE_CURRENT);

                views.setOnClickPendingIntent(R.id.wid_tv_recipename,recipeDetailspendingIntent);
                views.setOnClickPendingIntent(R.id.wid_tv_servings,recipeDetailspendingIntent);
                views.setOnClickPendingIntent(R.id.wid_tv_ingredients,recipeDetailspendingIntent);


                GridWidgetService.setRecipes(recipes);

                Intent recipeitemintent = new Intent(context, RecipeDetailsActivity.class);
                PendingIntent recpendingIntent = PendingIntent.getActivity(context, UUID.randomUUID().hashCode(), recipeitemintent, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setPendingIntentTemplate(R.id.widget_gridview_grid, recpendingIntent);
            }
            else{
                //views.setViewVisibility(R.id.empty_info, View.VISIBLE );
                //views.setViewVisibility(R.id.widget_gridview_grid, View.GONE);
            }
            return views;
        }

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        /*for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }*/
        RecipeIngredientsService.startGetRecipes(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        RecipeIngredientsService.startGetRecipes(context);

        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager widgetManager, Recipe[] newrecipes, int[] appWidgetIds) {
        if(newrecipes != null){
            recipes = newrecipes;
        }

        for( int appWidgetId : appWidgetIds ){
            updateAppWidget(context, widgetManager, appWidgetId);
        }
    }

}

