package com.tp.transport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TraficInfoAdapter extends RecyclerView.Adapter<TraficInfoAdapter.TraficInfoViewHolder> {

    private Context context;
    private List<TraficInfo> traficInfoList;

    public TraficInfoAdapter(List<TraficInfo> traficInfoList) {
        this.context = context;
        this.traficInfoList = traficInfoList;
    }

    @NonNull
    @Override
    public TraficInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trafic_info, parent, false);
        return new TraficInfoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TraficInfoViewHolder holder, int position) {
        TraficInfo traficInfo = traficInfoList.get(position);
        holder.bind(traficInfo);
    }

    @Override
    public int getItemCount() {
        return traficInfoList.size();
    }

    public class TraficInfoViewHolder extends RecyclerView.ViewHolder {

        private TextView villeTextView;
        private TextView dateTextView;
        private TextView descriptionTextView;
        private LinearLayout expandableLayout;
        private TextView descriptionDetailleeTextView;

        public TraficInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            villeTextView = itemView.findViewById(R.id.textViewVille);
            dateTextView = itemView.findViewById(R.id.textViewDate);
            descriptionTextView = itemView.findViewById(R.id.textViewDescription);
            descriptionDetailleeTextView = itemView.findViewById(R.id.textViewDescriptionDetaillee);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);

            descriptionTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TraficInfo traficInfo = traficInfoList.get(getAdapterPosition());
                    traficInfo.setExpanded(!traficInfo.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }

        public void bind(TraficInfo traficInfo) {
            villeTextView.setText(traficInfo.getVille());
            dateTextView.setText(traficInfo.getDate());
            descriptionTextView.setText(traficInfo.getDescription());
            descriptionDetailleeTextView.setText(traficInfo.getDescriptionDetaillee());

            // Gérer l'état d'expansion/réduction de l'accordion
            if (traficInfo.isExpanded()) {
                expandableLayout.setVisibility(View.VISIBLE);
                descriptionDetailleeTextView.setText(traficInfo.getDescriptionDetaillee());
            } else {
                expandableLayout.setVisibility(View.GONE);
            }
        }
    }
}
