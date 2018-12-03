package MusicPlayer.ui;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXSlider;

import MusicPlayer.Main;
import MusicPlayer.Player;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import sockets.Client;

public class ControllerClient implements Initializable{

	Stage stage;
    Scene scene;
    Player player;
	FXMLLoader fxmlloader;
	Main main;
	Client client;
	Thread clientThread;
	
    int playListCont;
	boolean draggin=false;
	boolean activated=false;
	int a=50;
    
    @FXML private StackPane stackPane;
    @FXML private MaterialDesignIconView playPauseIcon;
    @FXML private Label tituloLabel;
    @FXML private Label cancionLabel;
    @FXML private Label artistaLabel;
    @FXML private Label timeLabel;
    @FXML private Label durationLabel;
    @FXML private ImageView songImage;
    @FXML private JFXProgressBar progressBar;
    @FXML private JFXSlider volumeSlider;

    @FXML
    private void closeButtonAction(ActionEvent event) {
        stage = (Stage) scene.getWindow();
        stage.close();
    }

    @FXML
    private void minimizeButtonAction(ActionEvent event) {
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void maximizeButtonAction(ActionEvent event) {
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        if(!stage.isMaximized())
            stage.setMaximized(true);
        else
            stage.setMaximized(false);
    }

    @FXML
    private void playPauseButtonAction(MouseEvent event) {
    	if(player.isPlaying()) {
    		player.pause();
    		playPauseIcon.setGlyphName("PLAY");
    	} else {
    		player.play();
    		playPauseIcon.setGlyphName("PAUSE");
    	}

    }

    @FXML
	private void ChangeSongTime(MouseEvent event) {
		draggin=false;
		double mouse = event.getX();
		long progressBarVal = (long) (mouse*100/1237);
		progressBar.setProgress(progressBarVal*.01);
		player.changeTime(progressBarVal);
		
	}

    @FXML
	private void DragBar(MouseEvent event) {
		draggin=true;
		int mouse =(int) event.getX();
		long progressBarVal = mouse*100/1237;
		String a=player.getSongDuration().toString().substring(23, 28); //Tomo el número, innecesario si se quita StringProperty
		int SongSeconds=Integer.parseInt(a.charAt(0)+"")*600 + Integer.parseInt(a.charAt(1)+"")*60+
				Integer.parseInt(a.charAt(3)+"")*10+Integer.parseInt(a.charAt(4)+""); //Calcular segundos
		SongSeconds=(int) (SongSeconds*progressBarVal*.01); // Multiplicar por el porcentaje de la progressBar
		Duration duration=Duration.ZERO; //Inicializa duration
		duration=duration.plusSeconds(SongSeconds); //Le sumo los segundos que saqué de del String
		LocalTime as=LocalTime.of(0,(int)duration.toMinutes(),((int)(SongSeconds-duration.toMinutes()*60))); //Acomodo el tiempo
		DateTimeFormatter format = DateTimeFormatter.ofPattern("mm:ss"); //Formato que usamos
		
		timeLabel.setText(as.format(format)); //Aplico el formato
		progressBar.setProgress((double)progressBarVal*.01); //Actualzar Progreso
	}

    
    @FXML
    private void volumeSliderAction(MouseEvent event) {
    	player.changeVolume(volumeSlider.getValue());
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	try {
			client = new Client(this);
			clientThread = new Thread(client);
			clientThread.start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }


	public void initializeSongVar() {
        cancionLabel.setText(player.getSongName().get());
        artistaLabel.setText(player.getSongArtist().get());
        songImage.setImage(player.getSongImage());
    }

    public void intializeDuration(double duration) {
    	durationLabel.setText(player.getSongDuration().get());
        progressBar.setSecondaryProgress(duration);
    }

    public void initializeTime(double time) {
    	timeLabel.setText(player.getSongTime().get());
    	progressBar.setProgress(time);
    }

    public void setScene(Scene scene) {
    	this.scene = scene;
    	scene.getStylesheets().add("resource/stylesheet_dark.css");
    }
    
    public void getFile(String song) {
    	player = new Player(this, song);
;    }
    
    
}
