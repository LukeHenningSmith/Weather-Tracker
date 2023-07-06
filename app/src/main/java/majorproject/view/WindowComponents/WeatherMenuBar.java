package majorproject.view.component;

import majorproject.presenter.WeatherPresenter;

import javafx.event.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;

/**
* A class containing JavaFX Menubar to encapsulate all its associated methods and construction.
*/
public class WeatherMenuBar {

  private WeatherPresenter presenter;
  private MenuBar menuBar = new MenuBar();

  private MenuItem about;
  private MenuItem clearCache;

  /**
  * Constructs the MenuBar and actions of included buttons.
  * @param presenter The WeatherPresenter this class will use to notify of user input.
  */
  public WeatherMenuBar(WeatherPresenter presenter){
    this.presenter=presenter;
    setupMenu();
    setupMenuButtons();
  }

  /**
  * Getter method to return the contained Menubar
  * @return Returns the contained MenuBar.
  */
  public MenuBar getMenuBar(){
    return this.menuBar;
  }

  /**
  * Method to setup the menu bar.
  */
  private void setupMenu(){
    this.about = new MenuItem("About");
    this.about.setId("about");
    this.clearCache = new MenuItem("Clear Cache");
    this.clearCache.setId("clearCache");
    Menu menu = new Menu("Menu");
    menuBar.getMenus().add(menu);
    menu.getItems().add(about);
    menu.getItems().add(clearCache);

    this.menuBar.setOnMouseEntered(new EventHandler<MouseEvent>() {
      public void handle(MouseEvent e) {
        menuBar.setCursor(Cursor.HAND);
      }
    });
  }

  /**
  * Method to setup the buttons (and their action events) on the menu bar.
  */
  private void setupMenuButtons(){
    clearCache.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        presenter.clearCache();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cache Alert");
        alert.setHeaderText("Cache Cleared");
        alert.setContentText("The cache has been cleared.");
        alert.showAndWait();
      }
    });
    about.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        presenter.displayAboutPageWindow();
      }
    });
  }

}
