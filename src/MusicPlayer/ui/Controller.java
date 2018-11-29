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

import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.ResourceBundle;
import java.util.stream.Collectors;



public class Controller implements Initializable {
	private ListIterator<File> songIterator;
	boolean randomMode=false;
	boolean repeatMode=false;
	boolean draggin=false;
	boolean prevClicked=false;
	boolean activated=false;
	int a=50;
    Stage stage;
    Scene scene;
    Player player;
    
    
    ArrayList<File> musicFiles;
    ArrayList<File> RandomPlaylist;

	
    @FXML private JFXTreeTableView<Song> treeView;
    @FXML private MaterialDesignIconView playPauseIcon;
    @FXML private MaterialDesignIconView shuffleIcon;
    @FXML private MaterialDesignIconView repeatIcon;
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
    	}
		
		else {
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
		if(!repeatMode) {
			repeatMode=true;
			repeatIcon.setFill(Color.rgb(88,250, 172));
				
		}
		else {
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
        
        RandomPlaylist= new ArrayList<>();
        
        
        RandomPlaylist.addAll(musicFiles);
        //Collections.shuffle(RandomPlaylist);
        
        songIterator = musicFiles.listIterator();
        
        
        player = new Player(this, songIterator.next().getPath());
       
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
	    	
    	}
    	else {
    		if(player.isPlaying()) {
        		player.pause();
        		playPauseIcon.setGlyphName("PLAY");
    		}
    		if(repeatMode) {
        		if(player.getSongTime().toString().equals(player.getSongDuration().toString())) {
        			songIterator.previous();
        			player= new Player(this, songIterator.next().getPath());
        			return;
        		}
        		}
    				if(!randomMode)
    			songIterator=musicFiles.listIterator();
    				
    				else {
    					Collections.shuffle(RandomPlaylist);
    				songIterator=RandomPlaylist.listIterator();
    				}
    		player= new Player(this, songIterator.next().getPath());
    		player.setAutoPlay(false);
    		
    	}
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
    	}
    	else {
    		if(player.isPlaying()) {
        		player.pause();
        		playPauseIcon.setGlyphName("PLAY");
    		}
    		
    		if(!randomMode)
    			songIterator=musicFiles.listIterator();
    				
    				else {
    					Collections.shuffle(RandomPlaylist);
    				songIterator=RandomPlaylist.listIterator();
    				}
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
    	if(!draggin) {
    	timeLabel.setText(player.getSongTime().get());
    	progressBar.setProgress(time);
    	}
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
		}
	   
   }
}


