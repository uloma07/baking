package com.example.android.bakingapp;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.android.bakingapp.Fragments.StepFragment;
import com.example.android.bakingapp.Models.Ingredient;
import com.example.android.bakingapp.Models.Recipe;
import com.example.android.bakingapp.Models.Step;

import java.util.Arrays;

public class StepDetailsActivity extends AppCompatActivity {

    public static final String STEP = "the_step_obj";
    Step s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details_activity);
        //DONE!!! SO excited!!
        if(savedInstanceState == null) {
            StepFragment stepfragment = new StepFragment();

            s = getIntentData();

            stepfragment.setStep(s);

            FragmentManager fragmentmanager = getSupportFragmentManager();

            fragmentmanager.beginTransaction()
                    .add(R.id.step_container, stepfragment)
                    .commit();
        }
        else{
            s = savedInstanceState.getParcelable(STEP);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(STEP, s);
        super.onSaveInstanceState(outState);
    }

    Step getIntentData(){
        Step newStep = new Step();
        try {
            String image = getIntent().getStringExtra("image");
            newStep.setthumbnailURL(image);

            String description = getIntent().getStringExtra("description");
            newStep.setDescription(description);

            String shortdescription = getIntent().getStringExtra("shortdescription");
            newStep.setshortDescription(shortdescription);

            String videoUrl = getIntent().getStringExtra("videoUrl");
            newStep.setvideoURL(videoUrl);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            return newStep;
        }

    }
}
