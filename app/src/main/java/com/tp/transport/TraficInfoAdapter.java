package com.tp.transport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TraficInfoAdapter extends RecyclerView.Adapter<TraficInfoAdapter.TraficViewHolder> {

    private final List<TraficInfo> signalementList;
    private final OnItemClickListener onItemClickListener;
    private final Context context;

    public TraficInfoAdapter(Context context, List<TraficInfo> signalementList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.signalementList = signalementList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public TraficViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_signalement, parent, false);
        return new TraficViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TraficViewHolder holder, int position) {
        TraficInfo signalement = signalementList.get(position);
        holder.bind(signalement, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return signalementList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(TraficInfo traficInfo);
    }

    public class TraficViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewProblemType;
        private final TextView textViewContactEmail;
        private final TextView textViewGravity;
        private final TextView textViewDescription;

        public TraficViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewProblemType = itemView.findViewById(R.id.problem_type);
            textViewContactEmail = itemView.findViewById(R.id.contact_email);
            textViewGravity = itemView.findViewById(R.id.gravity);
            textViewDescription = itemView.findViewById(R.id.description);
        }

        @SuppressLint("SetTextI18n")
        public void bind(final TraficInfo signalement, final OnItemClickListener listener) {
            textViewProblemType.setText("Type de problème : " + signalement.getProblemType());
            textViewContactEmail.setText("Email de contact : " + signalement.getContactEmail());
            textViewGravity.setText("Gravité : " + signalement.getGravity());
            textViewDescription.setText("Description : " + signalement.getDescription());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(signalement);
                }
            });
        }
    }
}
