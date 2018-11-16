package MusicPlayer;


import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Song extends RecursiveTreeObject<Song> {

    public StringProperty title;
    StringProperty album;
    StringProperty artist;
    StringProperty time;
    StringProperty duration;
    Image image;
    
    public Song() {
    }

    public Song(String title, String album, String artist, String duration){
        this.title = new SimpleStringProperty(title);
        this.album = new SimpleStringProperty(album);
        this.artist = new SimpleStringProperty(artist);
        this.duration = new SimpleStringProperty(duration);
    }
    
    public void setTitle(StringProperty title) {
    	this.title = title;
    }
    
    public void setArtist(StringProperty artist) {
    	this.artist = artist;
    }
    
    public void setTime(StringProperty time) {
		this.time = time;
	}
    
    public void setDuration(StringProperty duration) {
    	this.duration = duration;
    }
    
    public void setImage(Image image) {
    	this.image = image;
    }



}