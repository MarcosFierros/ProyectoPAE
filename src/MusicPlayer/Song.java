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
    
    public StringProperty getTitle() {
		return title;
	}

	public StringProperty getAlbum() {
		return album;
	}

	public void setAlbum(StringProperty album) {
		this.album = album;
	}

	public StringProperty getArtist() {
		return artist;
	}

	public StringProperty getTime() {
		return time;
	}

	public StringProperty getDuration() {
		return duration;
	}

	public Image getImage() {
		return image;
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
