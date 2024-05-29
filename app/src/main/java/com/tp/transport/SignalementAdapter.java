package com.tp.transport;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SignalementAdapter extends RecyclerView.Adapter<SignalementAdapter.SignalementViewHolder> {

    private final List<GererSignalement> signalementList;
    private final OnItemClickListener listener;

    public SignalementAdapter(List<GererSignalement> signalementList, OnItemClickListener listener) {
        this.signalementList = signalementList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SignalementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_signalement, parent, false);
        return new SignalementViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SignalementViewHolder holder, int position) {
        holder.bind(signalementList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return signalementList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(GererSignalement signalement);
    }

    public static class SignalementViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewProblemType;
        private final TextView textViewContactEmail;
        private final TextView textViewGravity;
        private final TextView textViewDescription;
        private final TextView textViewLocation;
        private final TextView textViewDate;

        public SignalementViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewProblemType = itemView.findViewById(R.id.problem_type);
            textViewContactEmail = itemView.findViewById(R.id.contact_email);
            textViewGravity = itemView.findViewById(R.id.gravity);
            textViewDescription = itemView.findViewById(R.id.description);
            textViewLocation = itemView.findViewById(R.id.location);
            textViewDate = itemView.findViewById(R.id.date);
        }

        @SuppressLint("SetTextI18n")
        public void bind(GererSignalement signalement, OnItemClickListener listener) {
            textViewProblemType.setText("Type de problème : " + signalement.getProblemType());
            textViewContactEmail.setText("Email de contact : " + signalement.getContactEmail()); // Bind email data
            textViewGravity.setText("Gravité : " + signalement.getGravity());
            textViewDescription.setText("Description : " + signalement.getDescription());
            textViewLocation.setText("Location : " + signalement.getLocation());
            textViewDate.setText("Date : " + signalement.getDate());

            itemView.setOnClickListener(v -> listener.onItemClick(signalement));
        }
    }
}
