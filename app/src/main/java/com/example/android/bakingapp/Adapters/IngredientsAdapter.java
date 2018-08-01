package com.example.android.bakingapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.Models.Ingredient;
import com.example.android.bakingapp.R;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsHolder>{

    private Ingredient[] ingredients;
    private Context mContext;
    private static int reviewHolderCount;
    private int mNumberItems;
    private static int viewHolderCount;


    public IngredientsAdapter(Context context, Ingredient[] theingredients) {
        viewHolderCount = 0;
        mContext = context;
        ingredients = theingredients;
    }

    public void updateReviews(Ingredient[] items) {
        ingredients = items;
        notifyDataSetChanged();
    }

    @Override
    public IngredientsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.ingredient_listitem, parent, false);
        IngredientsHolder viewHolder = new IngredientsHolder(view);
        viewHolderCount++;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(IngredientsHolder holder, int position) {
        holder.bind(ingredients[position]);
    }

    @Override
    public int getItemCount() {
        return ingredients.length;
    }

    class IngredientsHolder extends RecyclerView.ViewHolder
    {
        TextView tvmeasure;
        TextView tvquantity;
        TextView tvingredient;

        public IngredientsHolder(View itemView) {
            super(itemView);

            tvmeasure = (TextView) itemView.findViewById(R.id.tv_measure);
            tvingredient = (TextView) itemView.findViewById(R.id.tv_ingredient);
            tvquantity = (TextView) itemView.findViewById(R.id.tv_quantity);
        }

        void bind(Ingredient listitem) {
            tvquantity.setText(String.valueOf(listitem.getquantity()));
            tvingredient.setText(listitem.getingredient());
            tvmeasure.setText(listitem.getmeasure());
        }
    }
}
