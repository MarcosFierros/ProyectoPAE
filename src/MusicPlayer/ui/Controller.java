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
    int playListCont;

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
        			playListCont++;
        			int cont=playListCont;
    	    		Label playListLabel = new Label(playListName.toUpperCase());
    	    		playListLabel.addEventHandler(MouseEvent.MOUSE_PRESSED ,new EventHandler<MouseEvent>() {

						@Override
						public void handle(MouseEvent arg0) {
							//pruebas
							System.out.println("funciono");
							System.out.println(cont);
							//fin pruebas
							playList_List.add(new ArrayList<Song>());
							showPlayList(cont);
						}
    	    		});
    	    		playListView.getItems().add(playListLabel);
    	    		newPlayListTF.setText("");
        		}
                dialog.close();
            }
        });
        content.setActions(button);
    	dialog.show();
    }

    @FXML
    private void changeLight(ActionEvent action) {
    	scene.getStylesheets().clear();
    	scene.getStylesheets().add("resource/stylesheet_light.css");
    }

    @FXML
    private void changeDark(ActionEvent action) {
    	scene.getStylesheets().clear();
    	scene.getStylesheets().add("resource/stylesheet_dark.css");
    }

    @FXML
    private void changeEnglish(ActionEvent action) {

    }
    @FXML
    private void changeSpanish(ActionEvent action) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    	player = new Player(this);
    	playListCont=0;
    	intializePlaylist_List();
        showPlayList(0);
        Label defalutMusicLabel = new Label("ARCHIVOS LOCALES");
        defalutMusicLabel.addEventHandler(MouseEvent.MOUSE_PRESSED ,new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				showPlayList(0);
				System.out.println("funciono");
			}
		});
		playListView.getItems().add(defalutMusicLabel);
		playListView.requestFocus();

    }

	private void showPlayList(int indice) {

		JFXTreeTableColumn<Song, String> songTitle = new JFXTreeTableColumn<>("TiTULO");
		songTitle.setPrefWidth(500);

		JFXTreeTableColumn<Song, String> songArtist = new JFXTreeTableColumn<>("ARTISTA");
		songArtist.setPrefWidth(200);

		JFXTreeTableColumn<Song, String> songAlbum = new JFXTreeTableColumn<>("aLBUM");
		songAlbum.setPrefWidth(150);

		JFXTreeTableColumn<Song, String> songDuration = new JFXTreeTableColumn<>("DURACIoN");
		songDuration.setPrefWidth(150);

		ObservableList<Song> songs = FXCollections.observableArrayList();
		final TreeItem<Song> root = new RecursiveTreeItem<>(songs, RecursiveTreeObject::getChildren);
		treeView.setRoot(root);
		treeView.setShowRoot(false);
		treeView.setEditable(true);

		for(Song s : playList_List.get(indice)) {
			TreeItem<Song> song = new TreeItem<Song>(s);
			root.getChildren().add(song);
		}

		treeView.getColumns().setAll(songTitle, songArtist, songAlbum, songDuration);
		songTitle.setCellValueFactory((TreeTableColumn.CellDataFeatures<Song, String> param) ->param.getValue().getValue().getTitle());
		songArtist.setCellValueFactory((TreeTableColumn.CellDataFeatures<Song, String> param) ->param.getValue().getValue().getArtist());
		songAlbum.setCellValueFactory((TreeTableColumn.CellDataFeatures<Song, String> param) ->param.getValue().getValue().getAlbum());
		songDuration.setCellValueFactory((TreeTableColumn.CellDataFeatures<Song, String> param) ->param.getValue().getValue().getDuration());

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

    public void setScene(Scene scene) {
    	this.scene = scene;
    	scene.getStylesheets().add("resource/stylesheet_dark.css");
    }

}
