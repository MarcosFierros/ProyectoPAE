package MusicPlayer;

import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import MusicPlayer.ui.Controller;
import MusicPlayer.ui.ControllerClient;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.StringProperty;
import javafx.collections.MapChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Player {

	private MediaPlayer mediaPlayer;
	private boolean isPlaying;
	private Media media;
	public Song song;
	Player play;
	private Timeline timeline;
	private Controller controller;
	private ControllerClient controllerClient;

	public Player(Controller controller, String cancion) {
		media = new Media(Paths.get(cancion).toUri().toString());
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
		 
		
		timeline = new Timeline(new KeyFrame(Duration.seconds(.1), new EventHandler() {

			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				DateTimeFormatter format = DateTimeFormatter.ofPattern("mm:ss");
				Duration duration = media.getDuration();
				Duration time = mediaPlayer.getCurrentTime();
				LocalTime formatedtime = LocalTime.of(0, (int) time.toMinutes(), (int) (time.toSeconds() - (int) time.toMinutes()*60));
				song.setTime(formatedtime.format(format));
				controller.initializeTime(1/duration.toSeconds()*time.toSeconds());
				if(duration.toSeconds()==time.toSeconds()&&duration.toSeconds()>1){
					controller.NextSong();
				}
				
			}
			
		} ));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}
	
	public Player(ControllerClient controller, String cancion) {
		media = new Media(Paths.get(cancion).toUri().toString());
		media.getMetadata().addListener(new MapChangeListener<String, Object>() {

			@Override
			public void onChanged(Change<? extends String, ? extends Object> change) {
				// TODO Auto-generated method stub
				handleMetadata2(change.getKey(), change.getValueAdded());
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
				controllerClient.intializeDuration(duration.toSeconds());
			}
		});
		isPlaying = false;
		
		this.controllerClient = controller;
		song = new Song();
		 
		
		timeline = new Timeline(new KeyFrame(Duration.seconds(.1), new EventHandler() {

			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				DateTimeFormatter format = DateTimeFormatter.ofPattern("mm:ss");
				Duration duration = media.getDuration();
				Duration time = mediaPlayer.getCurrentTime();
				LocalTime formatedtime = LocalTime.of(0, (int) time.toMinutes(), (int) (time.toSeconds() - (int) time.toMinutes()*60));
				song.setTime(formatedtime.format(format));
				controllerClient.initializeTime(1/duration.toSeconds()*time.toSeconds());
				if(duration.toSeconds()==time.toSeconds()&&duration.toSeconds()>1){
					
				}
				
			}
			
		} ));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}
	
	


	public void changeTime(long progress) {
		progress =(long) ((long)media.getDuration().toMillis()*progress*.01);
		mediaPlayer.seek(Duration.millis(progress));
	}

	public void stopTimeline() {
		timeline.stop();
	}

	public void setAutoPlay(boolean b) {
		mediaPlayer.setAutoPlay(b);
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

		if(key.equals("title")) {
			song.setTitle(value.toString());
			controller.initializeSongVar();
		 } else if (key.equals("artist")) {
		    song.setArtist(value.toString());
		 } else if (key.equals("image")) {
		     song.setImage((Image) value);
		 } else if(key.equals("album"))
			 song.setAlbum(value.toString());
	}
	
	private void handleMetadata2(String key, Object value) {

		if(key.equals("title")) {
			song.setTitle(value.toString());
			controllerClient.initializeSongVar();
		 } else if (key.equals("artist")) {
		    song.setArtist(value.toString());
		 } else if (key.equals("image")) {
		     song.setImage((Image) value);
		 } else if(key.equals("album"))
			 song.setAlbum(value.toString());
	}




}
