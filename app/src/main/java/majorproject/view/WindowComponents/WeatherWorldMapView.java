package majorproject.view.component;

import majorproject.presenter.*;
import majorproject.model.pojowrapper.LocationWeather;

import java.util.List;
import java.util.ArrayList;
import org.controlsfx.control.WorldMapView;
import org.controlsfx.control.WorldMapView.CountryView;
import java.io.File;
import java.io.FileNotFoundException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
* A class containing JavaFX WorldMapView to encapsulate all its associated methods and construction.
*/
public class WeatherWorldMapView {

  private WeatherPresenter presenter;
  private WorldMapView worldMapView;

  /**
  * Constructs TableView object and methods.
  * @param presenter The WeatherPresenter this class will use to notify of user input.
  */
  public WeatherWorldMapView(WeatherPresenter presenter){
    this.presenter=presenter;
    this.worldMapView = new WorldMapView();
    this.worldMapView.setId("weatherMap");
    setup();
  }

  /**
  * Simple getter method for the WorldMapView contained in the class.
  * @return The WorldMapView contained in the class.
  */
  public WorldMapView getWorldMapView(){
    return this.worldMapView;
  }

  /**
  * Sets up the WorldMapView object and its associated location image adding event.
  */
  private void setup(){
    worldMapView.setLocationViewFactory(location -> {
      File imageFile = new File("src/main/resources/icons/"+location.getName()+".png");
      // File imageFile = presenter.getImageFile(location.getName());
      double imageSize = 40.0;
      Image image = new Image(imageFile.toURI().toString());
      ImageView imageView = new ImageView();
      imageView.setImage(image);
      imageView.setFitWidth(imageSize);
      imageView.setPreserveRatio(true);
      imageView.setTranslateX(-1*(imageSize/2));
      imageView.setTranslateY(-1*(imageSize/2));
      return imageView;
    });
    worldMapView.setMaxSize(1000,450);
  }

  /**
  * Updates WorldMapView to display the current weather data.
  * @param weatherData The current weather data.
  */
  public void updateWorldMapViewData(List<LocationWeather> weatherData){
    //delete and replace all the icons.
    worldMapView.locationsProperty().clear();
    List<WorldMapView.Location> newLocations = new ArrayList<WorldMapView.Location>();
    for(int i=0; i<weatherData.size();i++){
      newLocations.add(new WorldMapView.Location(weatherData.get(i).getCurrentWeatherData().getWeatherIconData().getIcon(),
      weatherData.get(i).getLatitude(), weatherData.get(i).getLongitude()));
    }
    worldMapView.locationsProperty().addAll(newLocations);
  }

}
