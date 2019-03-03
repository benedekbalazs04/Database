package sample.database;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Song {
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty track;
    private SimpleStringProperty title;
    private SimpleIntegerProperty albumId;
    private SimpleStringProperty albumName;

    public Song() {
        this.id = new SimpleIntegerProperty();
        this.track = new SimpleIntegerProperty();
        this.title = new SimpleStringProperty();
        this.albumId = new SimpleIntegerProperty();
        this.albumName = new SimpleStringProperty();
    }

    public void  setAlbumName(String name){
        this.albumName.set(name);
    }

    public String getAlbumName(){
        return this.albumName.get();
    }
    public void setId(int id) {
        this.id.set(id);
    }

    public void setTrack(int track) {
        this.track.set(track);
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public void setAlbumId(int albumId) {
        this.albumId.set(albumId);
    }

    public int getId() {
        return id.get();
    }

    public int getTrack() {
        return track.get();
    }

    public String getTitle() {
        return title.get();
    }

    public int getAlbumId() {
        return albumId.get();
    }
}
