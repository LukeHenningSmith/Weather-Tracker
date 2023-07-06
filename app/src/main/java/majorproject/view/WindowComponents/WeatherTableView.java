package majorproject.view.component;

import majorproject.presenter.*;

import majorproject.view.alert.WeatherAlertWindow;

import majorproject.model.pojowrapper.LocationWeather;

import java.util.List;
import java.io.File;

import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Label;
import javafx.scene.Cursor;
import javafx.event.*;
import javafx.collections.*;
import javafx.scene.input.MouseEvent;

/**
* A class containing TableView to encapsulate all its associated methods and construction.
*/
public class WeatherTableView {

  private WeatherPresenter presenter;
  private TableView tableView;

  /**
  * Constructs TableView object and methods.
  * @param presenter The WeatherPresenter this class will use to notify of user input.
  */
  public WeatherTableView(WeatherPresenter presenter){
    this.presenter=presenter;
    this.tableView = new TableView();
    this.tableView.setId("weatherTable");
    setupTable();
    setupEvents();
  }

  /**
  * Method to setup the events of the table view.
  */
  private void setupEvents(){
    this.tableView.setPlaceholder(new Label("No city weather data to display"));

    this.tableView.setOnMouseClicked(event -> {
      if(event.getClickCount() == 2) {
        LocationWeather selectedLocation = (LocationWeather) tableView.getSelectionModel().getSelectedItem();
        if(selectedLocation!=null){
          new WeatherAlertWindow(selectedLocation);
        }
      }
    });

    this.tableView.setOnMouseEntered(new EventHandler<MouseEvent>() {
      public void handle(MouseEvent e) {
        tableView.setCursor(Cursor.HAND);
      }
    });
  }

  /**
  * Getter method that returns the contained TableView.
  * @return Returns the TableView contained inside.
  */
  public TableView getTableView(){
    return this.tableView;
  }

  /**
  * Updates TableView to display the current weather data.
  * @param weatherData The current weather data.
  */
  public void updateTableViewData(List<LocationWeather> weatherData){
    this.tableView.getItems().clear();
    int maxViewableTableSize = 3;
    double tableRowSize = 25.13;
    // add the new data in
    if(weatherData!=null){
      //then add things to table
      for(LocationWeather l : weatherData){
        this.tableView.getItems().add(l);
      }
      if(weatherData.size() < maxViewableTableSize){
        this.tableView.setPrefHeight(tableRowSize*(weatherData.size()+1)); //+1 for the top row of table
      } else {
        this.tableView.setPrefHeight(tableRowSize*(maxViewableTableSize+1)); //+1 for the top row of table
      }
    }
  }

  /**
  * Gets an ObservableList of the selected rows of the TableView.
  * @return Returns the current selected rows in the TableView.
  */
  public ObservableList getSelectedIndices(){
    return this.tableView.getSelectionModel().getSelectedIndices();
  }

  /**
  * Method to setup the columns of the TableView.
  */
  private void setupTable(){
    //set the columns up
    TableColumn<LocationWeather, Double> column1 =
    new TableColumn<>("City");

    column1.setCellValueFactory(
    new PropertyValueFactory<>("cityName"));

    TableColumn<LocationWeather, Double> column2 =
    new TableColumn<>("State");

    column2.setCellValueFactory(
    new PropertyValueFactory<>("stateName"));

    TableColumn<LocationWeather, Double> column3 =
    new TableColumn<>("Country");

    column3.setCellValueFactory(
    new PropertyValueFactory<>("countryName"));

    TableColumn<LocationWeather, Double> column31 =
    new TableColumn<>("Latitude");

    column31.setCellValueFactory(
    new PropertyValueFactory<>("latitude"));

    TableColumn<LocationWeather, Double> column32 =
    new TableColumn<>("Longitude");

    column32.setCellValueFactory(
    new PropertyValueFactory<>("longitude"));

    TableColumn<LocationWeather, Double> column4 =
    new TableColumn<>("Temperature");

    column4.setCellValueFactory(
    new PropertyValueFactory<>("temperature"));

    TableColumn<LocationWeather, String> column5 =
    new TableColumn<>("Cloud Coverage");

    column5.setCellValueFactory(
    new PropertyValueFactory<>("clouds"));

    TableColumn<LocationWeather, Double> column6 =
    new TableColumn<>("Wind Speed");

    column6.setCellValueFactory(
    new PropertyValueFactory<>("windSpeed"));

    TableColumn<LocationWeather, Double> column7 =
    new TableColumn<>("Wind Direction");

    column7.setCellValueFactory(
    new PropertyValueFactory<>("windDirection"));

    TableColumn<LocationWeather, Integer> column8 =
    new TableColumn<>("Precipitation");

    column8.setCellValueFactory(
    new PropertyValueFactory<>("precipitation"));

    TableColumn<LocationWeather, Integer> column9 =
    new TableColumn<>("Air Quality Index");

    column9.setCellValueFactory(
    new PropertyValueFactory<>("aqi"));

    this.tableView.getColumns().addAll(column1,column2,column3, column31, column32, column4, column5, column6,
    column7, column8, column9);

    column1.setSortable(false);
    column2.setSortable(false);
    column3.setSortable(false);
    column31.setSortable(false);
    column32.setSortable(false);
    column4.setSortable(false);
    column5.setSortable(false);
    column6.setSortable(false);
    column7.setSortable(false);
    column8.setSortable(false);
    column9.setSortable(false);

    this.tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    this.tableView.setPrefHeight(25.5);
  }
}
