package com.tp.transport;

public class GererSignalement {
    private String problemType;
    private String contactEmail;
    private String gravity;
    private String description;
    private String location;
    private String date;

    // Getters
    public String getProblemType() {
        return problemType;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getGravity() {
        return gravity;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    // Setters
    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public void setGravity(String gravity) {
        this.gravity = gravity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
