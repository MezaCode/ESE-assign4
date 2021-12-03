package mvc.screens;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import mvc.model.Person;
import myexceptions.UnknownException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private static MainController instance = null;
    private String session;
    private ArrayList<Person> people;

    @FXML
    private BorderPane rootPane;

    private MainController(){ }
    private static final Logger LOGGER = LogManager.getLogger();

    public void switchView(ScreenType screenType, Object... args) {
        switch (screenType) {
            case PERSONLIST:
                try {
                    this.setPeople(gateways.PersonGateway.fetchPeople(session));
                }catch (UnknownException e){
                    LOGGER.info("Unknown exception encountered at fetch people call");
                }
                FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/views/peopleListView.fxml"));
                loader.setController(new PersonListController(MainController.getInstance().getPeople()));
                Parent rootNode = null;
                try {
                    rootNode = loader.load();
                    rootPane.setCenter(rootNode);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case PERSONDETAIL:
                loader = new FXMLLoader(this.getClass().getResource("/views/personDetailView.fxml"));
                if(!(args[0] instanceof Person)) {
                    throw new IllegalArgumentException("Hey that's not a Person! " + args[0].toString());
                }
                loader.setController(new PersonDetailController((Person) args[0]));
                rootNode = null;
                try {
                    rootNode = loader.load();
                    rootPane.setCenter(rootNode);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case LOGIN:
                loader = new FXMLLoader(this.getClass().getResource("/views/logginView.fxml"));
                loader.setController(new loginController());
                rootNode = null;
                try {
                    rootNode = loader.load();
                    rootPane.setCenter(rootNode);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            switchView(ScreenType.PERSONLIST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MainController getInstance(){
        if (instance == null)
            instance = new MainController();
        return instance;
    }
    public ArrayList<Person> getPeople(){
        return people;
    }

    public void setPeople(ArrayList<Person> people){
        this.people = people;
    }

    public void setSession(String session){
        this.session = session;
    }

    public String getSession(){
        return this.session;
    }
}
