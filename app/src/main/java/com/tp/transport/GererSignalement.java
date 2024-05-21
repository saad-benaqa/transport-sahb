package com.tp.transport;


public class GererSignalement {
    private String ville;
    private String date;
    private String description;
    private String descriptionDetaillee;
    private boolean expanded; // Pour suivre l'état de l'expansion

    public GererSignalement(String ville, String date, String description, String descriptionDetaillee) {
        this.ville = ville;
        this.date = date;
        this.description = description;
        this.descriptionDetaillee = descriptionDetaillee;
        this.expanded = false; // Initialisé à false (non étendu)
    }

    public String getVille() {
        return ville;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getDescriptionDetaillee() {
        return descriptionDetaillee;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
