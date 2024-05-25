package com.tp.transport;

public class GererSignalement {
    private String userId;
    private String problemType;
    private String contactEmail;
    private String gravity;
    private String description;
    private long timestamp;

    public GererSignalement() {
        // Constructeur vide nécessaire pour Firestore
    }

    public GererSignalement(String userId, String problemType, String contactEmail, String gravity, String description, long timestamp) {
        this.userId = userId;
        this.problemType = problemType;
        this.contactEmail = contactEmail;
        this.gravity = gravity;
        this.description = description;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getGravity() {
        return gravity;
    }

    public void setGravity(String gravity) {
        this.gravity = gravity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
