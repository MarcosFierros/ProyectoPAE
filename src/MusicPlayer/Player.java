package MusicPlayer;

import java.nio.file.Paths;

import javafx.scene.media.*;

public class Player {
	
	private MediaPlayer mediaPlayer;
	private boolean isPlaying;
	
	public Player() {
		Media media = new Media(Paths.get("Souk Eye.mp3").toUri().toString());
		mediaPlayer = new MediaPlayer(media);
		isPlaying = false;
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
	
}
