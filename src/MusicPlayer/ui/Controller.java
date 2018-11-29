package MusicPlayer.ui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;
import java.util.Locale;
import java.util.ResourceBundle;

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

import MusicPlayer.Main;
import MusicPlayer.Player;
import MusicPlayer.Song;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Controller implements Initializable {

    Stage stage;
    Scene scene;
    Player player;
	FXMLLoader fxmlloader;
	Main main;
	
    int playListCont;
    private ListIterator<File> songIterator;
	boolean randomMode=false;
	boolean repeatMode=false;
	boolean draggin=false;
	boolean prevClicked=false;
	boolean activated=false;
	int a=50;

    ArrayList<ArrayList<Song>> playList_List = new ArrayList<>();
    ArrayList<File> musicFiles;
    ArrayList<File> RandomPlaylist;
    
    @FXML private StackPane stackPane;
    @FXML private JFXTreeTableView<Song> treeView;
    @FXML private MaterialDesignIconView playPauseIcon;
    @FXML private MaterialDesignIconView shuffleIcon;
    @FXML private MaterialDesignIconView repeatIcon;
    @FXML private Label tituloLabel;
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
    private void playRandomList(MouseEvent event) {
		if(!randomMode) {
			Collections.shuffle(RandomPlaylist);
			
			if(repeatMode) {
				songIterator.previous();
				int a=RandomPlaylist.indexOf(songIterator.next());
				songIterator=RandomPlaylist.listIterator(a);
			}
			else
    		songIterator=RandomPlaylist.listIterator();
			
    		
    		
    		randomMode=true;
    		shuffleIcon.setFill(Color.rgb(88,250, 172));
    	} else {
			if(songIterator.hasNext()&&songIterator.nextIndex()>=1) {
			songIterator.previous(); //no puedo sacar el indexOf de songIterator sin previous y next
			a=musicFiles.indexOf(songIterator.next());
			songIterator=musicFiles.listIterator(a+1);
			}
			
			else {
			songIterator=musicFiles.listIterator(0);
			}
			
			randomMode=false;
			shuffleIcon.setFill(Color.BLACK);
		}
		
		
    }

    @FXML
	private void repeatSong(MouseEvent event) {
		if (!repeatMode) {
			repeatMode=true;
			repeatIcon.setFill(Color.rgb(88,250, 172));	
		} else {
			repeatMode=false;
			repeatIcon.setFill(Color.BLACK);
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
    private void FillButton(MouseEvent event) {
    	shuffleIcon.setFill(Color.rgb(88,250, 172)); 
    }
    
    @FXML
    private void FillButtonRepeat(MouseEvent event) {
    	repeatIcon.setFill(Color.rgb(88,250, 172)); 
    }
    
    @FXML
    private void RemoveColor(MouseEvent event) {
    	if(!randomMode)
    	shuffleIcon.setFill(Color.BLACK);
    }
    
    @FXML
    private void RemoveColorRepeat(MouseEvent event) {
    	if(!repeatMode)
    	repeatIcon.setFill(Color.BLACK);
    }
    
    @FXML
    private void volumeSliderAction(MouseEvent event) {
    	player.changeVolume(volumeSlider.getValue());
    }

    @FXML
    private void skipNextButtonAction(MouseEvent event) {
    	prevClicked=false;
    	NextSong();
    }
  
    @FXML
    private void skipPrevButtonAction(MouseEvent event) {
    	prevClicked=true;
    	PrevSong();
    }

    
    @FXML
    private void createNewPlaylist(ActionEvent event) {

    	JFXDialogLayout content= new JFXDialogLayout();
    	Text text = new Text();
    	text.textProperty().bind(I18N.createStringBinding("new_playlist"));
        content.setHeading(text);

        VBox vbox = new VBox();
        JFXTextField newPlayListTF = new JFXTextField();
        newPlayListTF.promptTextProperty().bind(I18N.createStringBinding("name"));
        vbox.getChildren().add(newPlayListTF);
        content.setBody(vbox);;
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton button = new JFXButton();
        button.textProperty().bind(I18N.createStringBinding("accept"));
        button.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
            	String playListName = newPlayListTF.getText();
        		if(playListName.compareTo("") != 0) {
        			playListCont++;
        			int cont=playListCont;
    	    		Label playListLabel = new Label(playListName.toUpperCase());
    	    		playListLabel.getStyleClass().add("listLabel");
    	            playListLabel.setFont(new Font("System Bold", 15));
    	    		playListLabel.addEventHandler(MouseEvent.MOUSE_PRESSED ,new EventHandler<MouseEvent>() {

						@Override
						public void handle(MouseEvent arg0) {
							tituloLabel.setText(playListName.toUpperCase());
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
    private void changeEnglish(ActionEvent action)  {
    	I18N.setResourceBundlePath("resource.i18n.strings_us_en");
    	I18N.setLocale(new Locale("en", "us"));
		try {
			player.pause();
			stage.close();
			main.start(new Stage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    
    @FXML
    private void changeSpanish(ActionEvent action) {
    	I18N.setResourceBundlePath("resource.i18n.strings_mx_es");
    	I18N.setLocale(new Locale("es", "mx"));
    	fxmlloader.setResources(I18N.getResources());
    	try {
    		player.pause();
    		stage.close();
			main.start(new Stage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	
        musicFiles = new ArrayList<>();
        GetMusicFiles();
        RandomPlaylist= new ArrayList<>();
        RandomPlaylist.addAll(musicFiles);
        //Collections.shuffle(RandomPlaylist);
        songIterator = musicFiles.listIterator();
        player = new Player(this, songIterator.next().getPath());

    	playListCont=0;
    	intializePlaylist_List();
        showPlayList(0);
        Label defalutMusicLabel = new Label();   
        defalutMusicLabel.textProperty().bind(I18N.createStringBinding("local_archives"));
        defalutMusicLabel.getStyleClass().add("listLabel");
        defalutMusicLabel.setFont(new Font("System Bold", 15));
        System.out.println(defalutMusicLabel.getFont());
        tituloLabel.setText(defalutMusicLabel.getText());
        defalutMusicLabel.addEventHandler(MouseEvent.MOUSE_PRESSED ,new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				tituloLabel.setText(defalutMusicLabel.getText());
				showPlayList(0);
			}
		});
		playListView.getItems().add(defalutMusicLabel);
		playListView.requestFocus();
		
    }

    public void NextSong() {
    	player.stopTimeline();
    	if(songIterator.hasNext()) {
    		if(player.isPlaying()) {
        		player.pause();
        		playPauseIcon.setGlyphName("PLAY");
    		}	
    		if(repeatMode) {
    			if(prevClicked&&songIterator.hasNext()) {  //Error en RepeatSong al presionar PrevButton 
    				songIterator.next();
    			}
    			if(player.getSongTime().toString().equals(player.getSongDuration().toString())&&
    				songIterator.hasPrevious()) {	
    				songIterator.previous();
    				System.out.println("Repitiendo cancion #"+ songIterator.nextIndex());
    			}
		
    		}
    	
	    	player= new Player(this, songIterator.next().getPath());
	    	player.setAutoPlay(false);
	    	
    	} else {
    		if(player.isPlaying()) {
        		player.pause();
        		playPauseIcon.setGlyphName("PLAY");
    		} if(repeatMode) {
    			if(player.getSongTime().toString().equals(player.getSongDuration().toString())) {
        			songIterator.previous();
        			player= new Player(this, songIterator.next().getPath());
        			return;
        		}
        	} if(!randomMode)
    			songIterator=musicFiles.listIterator();
    		 else {
    			 Collections.shuffle(RandomPlaylist);
    			 songIterator=RandomPlaylist.listIterator();
    		}
    		player= new Player(this, songIterator.next().getPath());
    		player.setAutoPlay(false);	
    	}
    	player.play();
    	playPauseIcon.setGlyphName("PAUSE");
    }

    public void PrevSong() {
    	player.stopTimeline();
    	if(songIterator.hasPrevious()) {
    		if(player.isPlaying()) {
        		player.pause();
        		playPauseIcon.setGlyphName("PLAY");
    		}
	    	player= new Player(this, songIterator.previous().getPath());
	    	player.setAutoPlay(false);
    	} else {
    		if(player.isPlaying()) {
        		player.pause();
        		playPauseIcon.setGlyphName("PLAY");
    		} if(!randomMode)
    			songIterator=musicFiles.listIterator();
    		else {
    			Collections.shuffle(RandomPlaylist);
    			songIterator=RandomPlaylist.listIterator();
    		}
    		player= new Player(this, songIterator.next().getPath());
    		player.setAutoPlay(false);
    	}
    	player.play();
    	playPauseIcon.setGlyphName("PAUSE");
    }

    
	@SuppressWarnings("unchecked")
	private void showPlayList(int indice) {

		JFXTreeTableColumn<Song, String> songTitle = new JFXTreeTableColumn<>();
        
		songTitle.textProperty().bind(I18N.createStringBinding("title"));
		songTitle.setPrefWidth(604);

		JFXTreeTableColumn<Song, String> songArtist = new JFXTreeTableColumn<>();
		songArtist.textProperty().bind(I18N.createStringBinding("artist"));
		songArtist.setPrefWidth(200);

		JFXTreeTableColumn<Song, String> songAlbum = new JFXTreeTableColumn<>();		
		songAlbum.textProperty().bind(I18N.createStringBinding("album"));
		songAlbum.setPrefWidth(150);

		JFXTreeTableColumn<Song, String> songDuration = new JFXTreeTableColumn<>();
		songDuration.textProperty().bind(I18N.createStringBinding("duration"));
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
    	for(@SuppressWarnings("unused") File f: musicFiles) {
    		archivosLocales.add(player.song);
    		NextSong();
    	}
    	player.pause();
    	playPauseIcon.setGlyphName("PLAY");
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
    
    public void setFXMLLoader(FXMLLoader fxmlLoader) {
    	this.fxmlloader = fxmlLoader;
    }
    
    public void GetMusicFiles() {
 	   
 	   File folder = new File("C:\\Music");
 		if (folder.isDirectory())
 		{
 		  File[] files = folder.listFiles();
 		  for(File f: files)
 			  musicFiles.add(f);
 		}
 	   
    }
    
    public void setStage(Stage stage) {
    	this.stage = stage;
    }
    
    public void setMain(Main main) {
    	this.main = main;
    }


}
