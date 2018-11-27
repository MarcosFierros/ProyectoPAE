package MusicPlayer.ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import MusicPlayer.Player;
import MusicPlayer.Song;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Controller implements Initializable {

    Stage stage;
    Scene scene;
    Player player;
    
    ArrayList<ArrayList<Song>> playList_List = new ArrayList<>();

    @FXML private StackPane stackPane;
    @FXML private JFXTreeTableView<Song> treeView;
    @FXML private MaterialDesignIconView playPauseIcon;
    @FXML private Label cancionLabel;
    @FXML private Label artistaLabel;
    @FXML private Label timeLabel;
    @FXML private Label durationLabel;
    @FXML private ImageView songImage;
    @FXML private JFXProgressBar progressBar;
    @FXML private JFXSlider volumeSlider;
    @FXML private JFXButton playlistmenu;
    @FXML private JFXListView<Label> playListView;
    
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
    private void createNewPlaylist(ActionEvent event) {
    	
    	JFXDialogLayout content= new JFXDialogLayout();
        content.setHeading(new Text("Nueva Playlist"));
        
        VBox vbox = new VBox();
        JFXTextField newPlayListTF = new JFXTextField();
        newPlayListTF.setPromptText("NOMBRE");
        vbox.getChildren().add(newPlayListTF);
        content.setBody(vbox);;
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton button = new JFXButton("ACEPTAR");
        button.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
            	String playListName = newPlayListTF.getText();
        		if(playListName.compareTo("") != 0) {
    	    		Label playListLabel = new Label(playListName.toUpperCase());
    	    		playListView.getItems().add(playListLabel);
    	    		newPlayListTF.setText("");
    	    		
    	    		
        		}
            	
                dialog.close();
            }
        });
        content.setActions(button);
    	dialog.show();
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

        intializePlaylist_List();
        
        for(Song s : playList_List.get(0)) {
        	TreeItem<Song> song = new TreeItem<Song>(s);
        	root.getChildren().add(song);
        }
        
        treeView.getColumns().setAll(songTitle, songArtist, songAlbum, songDuration);
        songTitle.setCellValueFactory((TreeTableColumn.CellDataFeatures<Song, String> param) ->param.getValue().getValue().getTitle());
        songArtist.setCellValueFactory((TreeTableColumn.CellDataFeatures<Song, String> param) ->param.getValue().getValue().getArtist());
        songAlbum.setCellValueFactory((TreeTableColumn.CellDataFeatures<Song, String> param) ->param.getValue().getValue().getAlbum());
        songDuration.setCellValueFactory((TreeTableColumn.CellDataFeatures<Song, String> param) ->param.getValue().getValue().getDuration());
        
        Label defalutMusicLabel = new Label("ARCHIVOS LOCALES");
        playListView.getItems().add(defalutMusicLabel);
    	playListView.requestFocus();
      
    }
    
    private void intializePlaylist_List() {
    	ArrayList<Song> archivosLocales = new ArrayList<>();
    	archivosLocales.add(player.song);
    	playList_List.add(archivosLocales);
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
