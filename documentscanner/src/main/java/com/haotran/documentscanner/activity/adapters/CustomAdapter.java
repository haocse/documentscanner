package com.haotran.documentscanner.activity.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haotran.documentscanner.R;

import java.util.LinkedHashMap;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";

    private LinkedHashMap<String, String> mDataSet;

    private Handler handler;
    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                    handler.onItemClicked(getAdapterPosition());

                }
            });
            textView = (TextView) v.findViewById(R.id.textView);
        }

        public TextView getTextView() {
            return textView;
        }
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public CustomAdapter(Handler handler, LinkedHashMap<String, String> dataSet) {
        mDataSet = dataSet;
        this.handler = handler;
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        String data = getElementByIndex(position);
        viewHolder.getTextView().setText(data);
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public String getElementByIndex(int index){
        return (String) mDataSet.get( (mDataSet.keySet().toArray())[ index ] );
    }

    public String getEmailByIndex( int index){
        return (String) (mDataSet.keySet().toArray())[ index ];
    }

    public interface Handler {
        void onItemClicked(int position);
    }
}