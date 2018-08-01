package com.example.android.bakingapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingapp.Models.Step;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utils.NetworkUtils;

import java.net.URL;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsHolder> {

    private Step[] steps;
    private Context mContext;
    private int mNumberItems;
    private static int viewHolderCount;
    final private ListItemClickListener mOnClickListener;


    public StepsAdapter(Context context, Step[] thesteps, ListItemClickListener listener) {
        viewHolderCount = 0;
        mContext = context;
        mOnClickListener = listener;
        steps = thesteps;
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public void updateReviews(Step[] items) {
        steps = items;
        notifyDataSetChanged();
    }

    @Override
    public StepsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.step_listitem, parent, false);
        StepsHolder viewHolder = new StepsHolder(view);
        viewHolderCount++;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StepsHolder holder, int position) {
        holder.bind(steps[position]);
    }

    @Override
    public int getItemCount() {
        return steps.length;
    }

    class StepsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvShortdescription;
        Button vidButton;
        String vidURl;

        public StepsHolder(final View itemView) {
            super(itemView);

            tvShortdescription = (TextView) itemView.findViewById(R.id.tv_shortdescription);
            vidButton = (Button) itemView.findViewById(R.id.btn_viewStep);
            vidButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = getAdapterPosition();
                    String url = steps[clickedPosition].getvideoURL();
                    Context thecontext = itemView.getContext();
                    //itemView.getContext().getPackageManager()
                    if(!NetworkUtils.isConnectedToInternet(thecontext)){
                        Toast toast = Toast.makeText(thecontext, "Please check internet connection", Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }
                    Uri webpage = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

                    if (intent.resolveActivity(itemView.getContext().getPackageManager()) != null) {
                        itemView.getContext().startActivity(intent);
                    }
                }
            });
            itemView.setOnClickListener(this);
        }

        void bind(Step listitem) {
            tvShortdescription.setText(String.valueOf(listitem.getshortDescription()));
            //vidButton.setOnClickListener(this.myClickHandler(););
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }



    }
}


