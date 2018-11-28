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
        this.title = new SimpleStringProperty();
        this.album = new SimpleStringProperty();
        this.artist = new SimpleStringProperty();
        this.duration = new SimpleStringProperty();
        this.time = new SimpleStringProperty();
    }

    public Song(String title, String album, String artist, String duration){
        this.title = new SimpleStringProperty(title);
        this.album = new SimpleStringProperty(album);
        this.artist = new SimpleStringProperty(artist);
        this.duration = new SimpleStringProperty(duration);
        this.time = new SimpleStringProperty("");
    }
    
    public StringProperty getTitle() {
		return title;
	}

	public StringProperty getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album.set(album);
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

	public void setTitle(String title) {
    	this.title.set(title);
    }
    
    public void setArtist(String artist) {
    	this.artist.set(artist);;
    }
    
    public void setTime(String time) {
		this.time.set(time);;
	}
    
    public void setDuration(String duration) {
    	this.duration.set(duration);;
    }
    
    public void setImage(Image image) {
    	this.image = image;
    }



}
