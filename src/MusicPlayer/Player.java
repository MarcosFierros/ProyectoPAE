package MusicPlayer;

import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import MusicPlayer.ui.Controller;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.*;
import javafx.util.Duration;

public class Player {

	private MediaPlayer mediaPlayer;
	private boolean isPlaying;
	private Media media;
	public Song song;

	private Controller controller;

	public Player(Controller controller) {
		media = new Media(Paths.get("Souk Eye.mp3").toUri().toString());
		media.getMetadata().addListener(new MapChangeListener<String, Object>() {

			@Override
			public void onChanged(Change<? extends String, ? extends Object> change) {
				// TODO Auto-generated method stub
				handleMetadata(change.getKey(), change.getValueAdded());
			}

		});
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setOnPlaying(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

			}
		});
		mediaPlayer.setOnReady(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				DateTimeFormatter format = DateTimeFormatter.ofPattern("mm:ss");
				Duration duration = media.getDuration();
				LocalTime time = LocalTime.of(0, (int) duration.toMinutes(), (int) (duration.toSeconds() - (int) duration.toMinutes()*60));
				song.setDuration(time.format(format));
				controller.intializeDuration(duration.toSeconds());
			}
		});
		isPlaying = false;

		this.controller = controller;
		song = new Song();

		final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(.1), new EventHandler() {

			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				DateTimeFormatter format = DateTimeFormatter.ofPattern("mm:ss");
				Duration duration = media.getDuration();
				Duration time = mediaPlayer.getCurrentTime();
				LocalTime formatedtime = LocalTime.of(0, (int) time.toMinutes(), (int) (time.toSeconds() - (int) time.toMinutes()*60));
				song.setTime(formatedtime.format(format));
				controller.initializeTime(1/duration.toSeconds()*time.toSeconds());
			}

		} ));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();

	}

	public void play() {
		mediaPlayer.play();
		isPlaying = true;
	}

	public void pause() {
		mediaPlayer.pause();
		isPlaying = false;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public StringProperty getSongName() {
		return song.title;
	}

	public StringProperty getSongArtist() {
		return song.artist;
	}

	public StringProperty getSongTime() {
		return song.time;
	}

	public StringProperty getSongDuration() {
		return song.duration;
	}

	public Image getSongImage() {
		return song.image;
	}

	public void changeVolume(double volume) {
		mediaPlayer.setVolume(volume/100);
	}
	private void handleMetadata(String key, Object value) {
		System.out.println(key+ "-" + value.toString());

		if(key.equals("title")) {
			song.setTitle(value.toString());
			controller.initializeSongVar();
		 } else if (key.equals("artist")) {
		    song.setArtist(value.toString());
		 } else if (key.equals("image")) {
		     song.setImage((Image) value);
		 }
	}



}
