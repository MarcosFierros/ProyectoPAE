package MusicPlayer;


import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Song extends RecursiveTreeObject<Song> {

    StringProperty title;
    StringProperty album;
    StringProperty artist;
    StringProperty duration;

    public Song(String title, String album, String artist, String duration){
        this.title = new SimpleStringProperty(title);
        this.album = new SimpleStringProperty(album);
        this.artist = new SimpleStringProperty(artist);
        this.duration = new SimpleStringProperty(duration);
    }



}
