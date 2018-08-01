package com.example.android.bakingapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.Adapters.IngredientsAdapter;
import com.example.android.bakingapp.Adapters.StepsAdapter;
import com.example.android.bakingapp.Models.Recipe;
import com.example.android.bakingapp.R;
import com.squareup.picasso.Picasso;


public class RecipeFragment extends Fragment
        //implements StepsAdapter.ListItemClickListener
{


    Recipe r;

    ImageView imageView;
    TextView name;
    TextView serving;
    TextView ingTitle;
    RecyclerView steps;
    RecyclerView ingredients;
    IngredientsAdapter ingredientsAdapter;
    StepsAdapter stepsAdapter;
    TextView stepsTitle;
    private StepsAdapter.ListItemClickListener mOnClickListener;


    Context context;

    public static final String RECIPE = "the_recipe_obj";

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public RecipeFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(savedInstanceState != null){
            r = savedInstanceState.getParcelable(RECIPE);
        }

        View rootView = inflater.inflate(R.layout.fragment_recipe,container,false);

        if(r != null){

            context = rootView.getContext();
            imageView = (ImageView) rootView.findViewById(R.id.fimage_full);
            name = (TextView) rootView.findViewById(R.id.ftv_name);
            serving = (TextView) rootView.findViewById(R.id.ftv_servings);
            steps = (RecyclerView) rootView.findViewById(R.id.frv_steps);
            ingredients = (RecyclerView) rootView.findViewById(R.id.frv_ingredients);
            ingTitle = (TextView) rootView.findViewById(R.id.fing_header);
            stepsTitle = (TextView) rootView.findViewById(R.id.fsteps_header);

            LinearLayoutManager stepslayoutManager = new LinearLayoutManager(rootView.getContext());
            LinearLayoutManager ingredientslayoutManager = new LinearLayoutManager(rootView.getContext());
            steps.setLayoutManager(stepslayoutManager);
            ingredients.setLayoutManager(ingredientslayoutManager);

            steps.setHasFixedSize(false);
            ingredients.setHasFixedSize(false);

            populateView(r,rootView.getContext());

        }else {
            //TODO: replace this with a log
        }

        return rootView;
    }

    void populateView(Recipe r, Context c){
        if (r.getimage() != null && !r.getimage().trim().isEmpty()){
            Picasso.with(c)
                    .load(r.getimage())
                    .into(imageView);
        }

        if (r.getname() != null && !r.getname().trim().isEmpty()){
            name.setText(r.getname());
        }

        if ( r.getservings() > -1){
            serving.setText(String.valueOf(r.getservings()));
        }

        if (r.getingredients() != null && r.getingredients().length > 0){
            ingredientsAdapter = new IngredientsAdapter(c, r.getingredients());
            // Attach the adapter to a RecyclerView
            ingredients.setAdapter(ingredientsAdapter);

            ingTitle.setText("Ingredients (" + r.getingredients().length+")");
            ingTitle.setTextSize(16);
        }

        if (r.getsteps() != null && r.getsteps().length>0){
            stepsAdapter = new StepsAdapter(c , r.getsteps(), mOnClickListener);
            // Attach the adapter to a RecyclerView
            steps.setAdapter(stepsAdapter);

            stepsTitle.setText("Steps (" + r.getsteps().length+")" );
            stepsTitle.setTextSize(16);
        }
    }

    /*@Override
    public void onListItemClick(int clickedItemIndex) {

        Step vid = r.getsteps()[clickedItemIndex];
        //openWebPage(vid.getvideoURL().toString());
        Intent intent = new Intent(context, StepDetailsActivity.class);
        intent.putExtra("image", vid.getthumbnailURL());
        intent.putExtra("description", vid.getDescription());
        intent.putExtra("shortdescription", vid.getshortDescription());
        startActivity(intent);
    }*/

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(RECIPE, r);
        super.onSaveInstanceState(outState);
    }

    public void setRecipe(Recipe therecipe){
        r=therecipe;
    }

    public void setListenter(StepsAdapter.ListItemClickListener thelistener){
        mOnClickListener=thelistener;
    }
}
