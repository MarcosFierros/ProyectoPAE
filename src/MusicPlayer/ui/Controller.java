package MusicPlayer.ui;

import MusicPlayer.Player;
import MusicPlayer.Song;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;

import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ResourceBundle;



public class Controller implements Initializable {
	private Iterator<File> songIterator;
    Stage stage;
    Scene scene;
    Player player;
    Path mypath= Paths.get("ListaMusica.txt");
    
    ArrayList<File> musicFiles;

	
    @FXML private JFXTreeTableView<Song> treeView;
    @FXML private MaterialDesignIconView playPauseIcon;
    @FXML private Label cancionLabel;
    @FXML private Label artistaLabel;
    @FXML private Label timeLabel;
    @FXML private Label durationLabel;
    @FXML private ImageView songImage;
    @FXML private JFXProgressBar progressBar;
    @FXML private JFXSlider volumeSlider;
    
    @FXML
    private void closeButtonAction(ActionEvent event) {
        scene = ((Button)event.getSource()).getScene();
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
    private void volumeSliderAction(MouseEvent event) {
    	player.changeVolume(volumeSlider.getValue());
    }

    @FXML
    private void skipNextButtonAction(MouseEvent event) {
    	NextSong();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        JFXTreeTableColumn<Song, String> songTitle = new JFXTreeTableColumn<>("TÍTULO");
        songTitle.setPrefWidth(500);

        JFXTreeTableColumn<Song, String> songArtist = new JFXTreeTableColumn<>("ARTISTA");
        songArtist.setPrefWidth(200);

        JFXTreeTableColumn<Song, String> songAlbum = new JFXTreeTableColumn<>("ÁLBUM");
        songAlbum.setPrefWidth(150);

        JFXTreeTableColumn<Song, String> songDuration = new JFXTreeTableColumn<>("DURACIÓN");
        songDuration.setPrefWidth(150);

        ObservableList<Song> songs = FXCollections.observableArrayList();

        final TreeItem<Song> root = new RecursiveTreeItem<>(songs, RecursiveTreeObject::getChildren);
        treeView.setRoot(root);
        treeView.setShowRoot(false);
        treeView.setEditable(true);

        treeView.getColumns().setAll(songTitle, songArtist, songAlbum, songDuration);
        
        musicFiles = new ArrayList<>();
        GetMusicFiles();
        System.out.println(musicFiles.toString());
        songIterator = musicFiles.iterator();
        player = new Player(this, songIterator.next().getPath());
       
    }
    
    public void NextSong() {
    	System.gc();
    	player.stopTimeline();
    	if(songIterator.hasNext()) {
	    	player= new Player(this, songIterator.next().getPath());
	    	player.setAutoPlay(true);
    	}
    	else {
    		songIterator = musicFiles.iterator();
    		player= new Player(this, songIterator.next().getPath());
    		player.setAutoPlay(false);
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
    
   public void GetMusicFiles() {
	   
	   File folder = new File("C:\\Music");
		if (folder.isDirectory())
		{
		  // you can get all the names
		  //String[] fileNames = folder.list();

		  // you can get directly files objects
		  File[] files = folder.listFiles();
		  for(File f: files)
			  musicFiles.add(f);
			  System.out.println();
		}
	   
   }
}
