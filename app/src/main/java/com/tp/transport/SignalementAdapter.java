package com.tp.transport;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SignalementAdapter extends RecyclerView.Adapter<SignalementAdapter.SignalementViewHolder> {

    private List<GererSignalement> signalementList;

    public SignalementAdapter(List<GererSignalement> signalementList) {
        this.signalementList = signalementList;
    }

    @NonNull
    @Override
    public SignalementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_signalement, parent, false);
        return new SignalementViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SignalementViewHolder holder, int position) {
        GererSignalement signalement = signalementList.get(position);
        holder.bind(signalement);
    }

    @Override
    public int getItemCount() {
        return signalementList.size();
    }

    public class SignalementViewHolder extends RecyclerView.ViewHolder {

        private TextView villeTextView;
        private TextView dateTextView;
        private TextView descriptionTextView;
        private LinearLayout expandableLayout;
        private TextView descriptionDetailleeTextView;

        public SignalementViewHolder(@NonNull View itemView) {
            super(itemView);
            villeTextView = itemView.findViewById(R.id.textViewVille);
            dateTextView = itemView.findViewById(R.id.textViewDate);
            descriptionTextView = itemView.findViewById(R.id.textViewDescription);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            descriptionDetailleeTextView = itemView.findViewById(R.id.textViewDescriptionDetaillee);

            // Gérer l'expansion/réduction de l'accordion au clic sur la description
            descriptionTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GererSignalement signalement = signalementList.get(getAdapterPosition());
                    signalement.setExpanded(!signalement.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }

        public void bind(GererSignalement signalement) {
            villeTextView.setText(signalement.getVille());
            dateTextView.setText(signalement.getDate());
            descriptionTextView.setText(signalement.getDescription());

            // Afficher/masquer la description détaillée en fonction de l'état d'expansion
            if (signalement.isExpanded()) {
                expandableLayout.setVisibility(View.VISIBLE);
                descriptionDetailleeTextView.setText(signalement.getDescriptionDetaillee());
            } else {
                expandableLayout.setVisibility(View.GONE);
            }
        }
    }
}
