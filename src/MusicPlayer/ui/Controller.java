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
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    Stage stage;
    Scene scene;
    Player player;

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
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	player = new Player(this);
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

      
        //TreeItem<String> prueba = new TreeItem<>("Cancion 1 blablalba");
        
        //Song cancion=new Song("titulo","album","artista","duracion");
        
        
        TreeItem<Song> prueba= new TreeItem<Song>(player.song);
        TreeItem<Song> prueba2= new TreeItem<Song>(new Song("titulo1","2album","3artista","4duracion"));
        
        root.getChildren().setAll(prueba,prueba2);
        
        treeView.getColumns().setAll(songTitle, songArtist, songAlbum, songDuration);
        songTitle.setCellValueFactory((TreeTableColumn.CellDataFeatures<Song, String> param) ->param.getValue().getValue().getTitle());
        songArtist.setCellValueFactory((TreeTableColumn.CellDataFeatures<Song, String> param) ->param.getValue().getValue().getArtist());
        songAlbum.setCellValueFactory((TreeTableColumn.CellDataFeatures<Song, String> param) ->param.getValue().getValue().getAlbum());
        songDuration.setCellValueFactory((TreeTableColumn.CellDataFeatures<Song, String> param) ->param.getValue().getValue().getDuration());
      
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
}
