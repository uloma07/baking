package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.android.bakingapp.Adapters.StepsAdapter;
import com.example.android.bakingapp.Fragments.RecipeFragment;
import com.example.android.bakingapp.Fragments.StepFragment;
import com.example.android.bakingapp.Models.Ingredient;
import com.example.android.bakingapp.Models.Recipe;
import com.example.android.bakingapp.Models.Step;

import java.util.Arrays;

public class RecipeDetailsActivity extends AppCompatActivity implements StepsAdapter.ListItemClickListener {

    public static final String RECIPE = "the_recipe_obj";

    Recipe r;
    boolean twoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        r = getIntentData();

        if(findViewById(R.id.divider) != null){

            twoPane = true;

            if(savedInstanceState == null){

                RecipeFragment recipefragment = new RecipeFragment();

                recipefragment.setRecipe(r);
                recipefragment.setListenter(this);

                FragmentManager recfragmentmanager = getSupportFragmentManager();

                recfragmentmanager.beginTransaction()
                        .replace(R.id.master_details_fragment, recipefragment)
                        .commit();


                StepFragment stepfragment = new StepFragment();

                Step s = r.getsteps()[0];

                stepfragment.setStep(s);

                FragmentManager fragmentmanager = getSupportFragmentManager();

                fragmentmanager.beginTransaction()
                        .replace(R.id.child_details_fragment, stepfragment)
                        .commit();
            }
            else{
                r = savedInstanceState.getParcelable(RECIPE);
            }

        }
        else{
            twoPane = false;
            RecipeFragment recipefragment2 = new RecipeFragment();

            recipefragment2.setRecipe(r);
            recipefragment2.setListenter(this);

            FragmentManager fragmentmanager = getSupportFragmentManager();

            fragmentmanager.beginTransaction()
                    .add(R.id.recipe_container, recipefragment2)
                    .commit();
        }


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(RECIPE, r);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        Step vid = r.getsteps()[clickedItemIndex];

        if(twoPane){

            StepFragment stepfragment = new StepFragment();

            stepfragment.setStep(vid);

            FragmentManager fragmentmanager = getSupportFragmentManager();

            fragmentmanager.beginTransaction()
                    .replace(R.id.child_details_fragment, stepfragment)
                    .commit();

        }
        else {
            Intent intent = new Intent(this, StepDetailsActivity.class);
            intent.putExtra("image", vid.getthumbnailURL());
            intent.putExtra("description", vid.getDescription());
            intent.putExtra("shortdescription", vid.getshortDescription());
            intent.putExtra("videoUrl",vid.getvideoURL());
            startActivity(intent);
        }
    }

    Recipe getIntentData(){
        Recipe newRecipe = new Recipe();

        String image = getIntent().getStringExtra("image");
        newRecipe.setimage(image);

        String name = getIntent().getStringExtra("name");
        newRecipe.setname(name);

        int servings = getIntent().getIntExtra("servings",0);
        newRecipe.setservings(servings);

        Long recipeid = getIntent().getLongExtra("id",-1);
        newRecipe.setID(recipeid);

        if(getIntent().getExtras() != null) {


            Parcelable[] parcelablesteps = getIntent().getExtras().getParcelableArray("steps");
            Step[] steps = Arrays.copyOf(parcelablesteps, parcelablesteps.length, Step[].class);

            newRecipe.setsteps(steps);

            Parcelable[] parcelableingredients = getIntent().getExtras().getParcelableArray("ingredients");
            Ingredient[] ingredients = Arrays.copyOf(parcelableingredients, parcelableingredients.length, Ingredient[].class);

            newRecipe.setingredients(ingredients);
        }
        else{
            Step[] steps = {};
            newRecipe.setsteps(steps);

            Ingredient[] ingredients = {};
            newRecipe.setingredients(ingredients);
        }

        return newRecipe;
    }
}
