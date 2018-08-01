package com.example.android.bakingapp.utils;

import android.util.Log;

import com.example.android.bakingapp.Models.Ingredient;
import com.example.android.bakingapp.Models.Recipe;
import com.example.android.bakingapp.Models.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {

    public static Recipe[] parseRecipeJson(String json) {

        if(json != null && !json.isEmpty()){
            try {

                JSONArray json_obj = new JSONArray(json);

                Recipe[] recipes = new Recipe[json_obj.length()];

                for (int i=0; i<json_obj.length(); i++){
                    JSONObject recipe_data = new JSONObject(json_obj.getString(i));

                    Recipe m = new Recipe();
                    m.setID(Long.getLong(recipe_data.getString("id")));
                    m.setname(recipe_data.getString("name"));
                    m.setservings(Integer.parseInt(recipe_data.getString("servings")));
                    m.setimage(recipe_data.getString("image"));

                    JSONArray ingredients_obj = new JSONArray(recipe_data.getString("ingredients"));

                    Ingredient[] ingredients = new Ingredient[ingredients_obj.length()];
                    for (int j=0; j<ingredients_obj.length(); j++) {
                        JSONObject ingredients_data = new JSONObject(ingredients_obj.getString(j));
                        Ingredient ing = new Ingredient();

                        ing.setingredient(ingredients_data.getString("ingredient"));
                        ing.setmeasure(ingredients_data.getString("measure"));
                        ing.setquantity(Float.parseFloat(ingredients_data.getString("quantity")));

                        ingredients[j] = ing;
                    }

                    m.setingredients(ingredients);


                    JSONArray Steps_obj = new JSONArray(recipe_data.getString("steps"));

                    Step[] steps = new Step[Steps_obj.length()];
                    for (int k=0; k<Steps_obj.length(); k++) {
                        JSONObject steps_data = new JSONObject(Steps_obj.getString(k));
                        Step step = new Step();

                        step.setthumbnailURL(steps_data.getString("thumbnailURL"));
                        step.setvideoURL(steps_data.getString("videoURL"));
                        step.setDescription(steps_data.getString("description"));
                        step.setshortDescription(steps_data.getString("shortDescription"));
                        step.setID(Long.parseLong(steps_data.getString("id")));
                        steps[k] = step;
                    }

                    m.setsteps(steps);

                    recipes[i] = m;
                }

                return recipes;
            }
            catch (JSONException j){
                j.printStackTrace();
            }
        }
        return null;
    }

}
