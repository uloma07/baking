package com.example.android.bakingapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.Models.Ingredient;
import com.example.android.bakingapp.Models.Recipe;
import com.example.android.bakingapp.R;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {

    private Recipe[] therecipes;
    private Context mContext;
    private static int RecipeHolderCount;
    private int mNumberItems;
    final private ListItemClickListener mOnClickListener;



    public RecipeAdapter(Context context, Recipe[] recipes, ListItemClickListener listener) {
        RecipeHolderCount = 0;
        mContext = context;
        mOnClickListener = listener;
        therecipes = recipes;
    }

    public void updatetherecipes(Recipe[] items) {
        therecipes = items;
        notifyDataSetChanged();
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    @Override
    public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_listitem, parent, false);
        RecipeHolder viewHolder = new RecipeHolder(view);
        RecipeHolderCount++;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeHolder holder, int position) {
        holder.bind(therecipes[position]);
    }

    @Override
    public int getItemCount() {
        return therecipes!=null? therecipes.length : 0;
    }

    class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        TextView tvRecipeName;
        TextView tvServings;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews and set an onClickListener to listen for clicks. Those will be handled in the
         * onClick method below.
         */
        public RecipeHolder(View itemView) {
            super(itemView);
            tvRecipeName = (TextView) itemView.findViewById(R.id.tv_recipename);
            tvServings = (TextView) itemView.findViewById(R.id.tv_servings);
            itemView.setOnClickListener(this);

        }

        /**
         * A method we wrote for convenience. This method will take an integer as input and
         * use that integer to display the appropriate text within a list item.
         */
        void bind(Recipe listitem) {

            tvRecipeName.setText(listitem.getname());
            tvServings.setText("Serves "+listitem.getservings());

        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }

    }

}