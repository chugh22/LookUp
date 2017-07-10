package com.example.anirudh.lookup.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anirudh.lookup.R;
import com.example.anirudh.lookup.models.HistoryModel;

import java.util.ArrayList;

/**
 * Created by anirudh on 09/07/17.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    ArrayList<HistoryModel> historyModelArrayList ;
    Context mContext ;

    public HistoryAdapter(ArrayList<HistoryModel> historyModelArrayList, Context mContext) {
        this.historyModelArrayList = historyModelArrayList;
        this.mContext = mContext;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = li.inflate(R.layout.list_recentlookups , null) ;
        return new HistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        HistoryModel thishistory = historyModelArrayList.get(position) ;
        holder.tvWord.setText(thishistory.getWord());
        holder.tvExample.setText(thishistory.getExamples());
        holder.tvDef.setText(thishistory.getDefinition());
        holder.tvLex.setText(thishistory.getLexicalCategory());

    }

    @Override
    public int getItemCount() {
        return historyModelArrayList.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder{
        TextView tvWord , tvLex ,tvDef ,tvExample ;
        public HistoryViewHolder(View itemView) {
            super(itemView);
            tvDef = (TextView) itemView.findViewById(R.id.tvDefinitionRecentLookups);
            tvWord = (TextView) itemView.findViewById(R.id.tvWordRecentLookups) ;
            tvExample = (TextView) itemView.findViewById(R.id.tvExamplesRecentLookups) ;
            tvLex = (TextView)itemView.findViewById(R.id.tvLexRecentLookups) ;

        }
    }
}
