package mvc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mvc.screens.MainController;
import mvc.screens.ScreenType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class AppMain extends Application {
    private static final Logger LOGGER = LogManager.getLogger();

   //private static ArrayList<Person> people ;

    public static void main(String[] args) {
        LOGGER.info("before launch");

        launch(args);
        LOGGER.info("after launch");
    }

    @Override
    public void start(Stage stage)  throws Exception{
        LOGGER.info("before start");

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/views/mainView.fxml"));
        loader.setController( MainController.getInstance());
        //MainController.getInstance().setPeople(people);
        Parent rootNode = loader.load();
        stage.setScene(new Scene(rootNode));
        MainController.getInstance().switchView(ScreenType.LOGIN);
        stage.setTitle("Assignment2");
        stage.show();

    }
}
