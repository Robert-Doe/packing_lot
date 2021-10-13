package com.robertory.packinglot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static android.R.color.holo_red_dark;

public class SpaceListAdapter  extends RecyclerView.Adapter<SpaceListAdapter.ViewHolder> {

    private Context context;
    private List<Sensor> sensors;
    public SpaceListAdapter(@NonNull Context context, List<Sensor> sensors) {
        this.context=context;
        this.sensors=sensors;
        //this.sharedViewModel=viewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.space,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position)
    {

        if(Integer.parseInt(sensors.get(position).getValue().getValue())<20)
        {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.red));
            holder.cardView.setFocusable(true);
            holder.cardView.setRadius(16);
        }
        holder.tvBlockName.setText("Block : "+sensors.get(position).getName().split("#")[1].split("-")[0]);
        holder.tvPlotNumber.setText(sensors.get(position).getName().split("#")[1].split("-")[1]);

    }

    @Override
    public int getItemCount() {
        return sensors.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView tvPlotNumber;
        TextView tvBlockName;
        CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
              tvPlotNumber =
                    (TextView)itemView.findViewById(R.id.tvPlotNumber);
            tvBlockName=
                    (TextView)itemView.findViewById(R.id.tvBlock);
            cardView = (CardView)itemView.findViewById(R.id.card_view);
        }
    }

}

