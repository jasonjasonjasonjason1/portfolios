package fi.team11.notebook;

import java.time.LocalDateTime;

public class Note {

    //declarations
    private String title;
    private String text;
    private String created;
    private String updated;
    private String createdV;
    private String updatedV;

    //MAIN
    public Note(String title, String text, String created, String updated, String createdV, String updatedV){
        this.title = title;
        this.text = text;
        this.created = created;
        this.updated = updated;
        this.createdV = createdV;
        this.updatedV = updatedV;
    }

    //Getters
    public String getTitle(){
        return title;
    }
    public String getText(){
        return text;
    }
    public String getCreated() {
        return created;
    }
    public String getUpdated() {
        return updated;
    }
    public String getCreatedV() {
        return createdV;
    }
    public String getUpdatedV() {
        return updatedV;
    }
    //Setters
    public void setTitle(String title) {
        this.title = title;
    }
    public void setText(String text) {
        this.text = text;
    }
    public void setUpdated(String updated) {
        this.updated = updated;
    }
    public void setUpdatedV(String updatedV) {
        this.updatedV = updatedV;
    }
}
