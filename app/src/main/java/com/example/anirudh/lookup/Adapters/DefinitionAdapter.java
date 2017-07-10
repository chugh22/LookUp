package com.example.anirudh.lookup.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anirudh.lookup.R;
import com.example.anirudh.lookup.models.LexAndDef;

import java.util.ArrayList;

/**
 * Created by anirudh on 07/07/17.
 */

public class DefinitionAdapter extends RecyclerView.Adapter<DefinitionAdapter.DefViewHolder> {
    ArrayList<LexAndDef> lexAndDefs ;
    Context context ;

    public DefinitionAdapter(ArrayList<LexAndDef> lexAndDefs, Context context) {
        this.lexAndDefs = lexAndDefs;
        this.context = context;
    }


    @Override
    public DefViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = li.inflate(R.layout.list_defsandlexes ,parent ,false) ;
        return new DefViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DefViewHolder holder, int position) {
        LexAndDef thisLexdef = lexAndDefs.get(position) ;
        holder.tvDefinition.setText(thisLexdef.getDefinition());
        holder.tvLex.setText(thisLexdef.getLexicalCategory());
        holder.tvExamples.setText(thisLexdef.getExamples());
    }

    @Override
    public int getItemCount() {
        return lexAndDefs.size();
    }
    class DefViewHolder extends RecyclerView.ViewHolder{
         TextView tvDefinition , tvLex ,tvExamples;
        public DefViewHolder(View itemView) {
            super(itemView);
            tvDefinition = (TextView)itemView.findViewById(R.id.tvDefinition) ;
            tvLex = (TextView)itemView.findViewById(R.id.tvLex) ;
            tvExamples = (TextView)itemView.findViewById(R.id.tvExamples) ;
        }
    }
}
