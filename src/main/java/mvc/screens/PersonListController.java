package mvc.screens;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;


import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import mvc.model.Person;
import myexceptions.UnknownException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PersonListController implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger();
    private static ArrayList<Person> people ;
    private Person temp = null;
    public PersonListController(ArrayList<Person> people ){ this.people = people;}

    @FXML
    private Button addPersonButton;

    @FXML
    private Button deleteSelectedButton;

    @FXML
    private ListView<Person> personList;

    @FXML
    void clickPerson(MouseEvent event) {
        // on double click onlyissuenrfngh
        // 1. get the model that is double clicked (if none, then bail)
        // 2. switch to the cat editing screen with the model that is selected

        if (event.getClickCount() == 2){
            temp = personList.getSelectionModel().getSelectedItem();
            LOGGER.info("SELECTED <"+temp.toString()+">");
        }
    }

    @FXML
    void addPerson(ActionEvent event) {
        LOGGER.info("add person clicked-----------------------------------------------------------------------------------");
        if (temp == null){
            temp = new Person(0, "", "", null);
        }
        else{
            LOGGER.info("READING <"+temp.getFirstName()+" "+temp.getLastName()+">");
        }

        try {
            MainController.getInstance().switchView(ScreenType.PERSONDETAIL, temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void deletePerson(ActionEvent event) {
        if (temp != null){
            LOGGER.info("DELETING <"+temp.getFirstName()+" "+temp.getLastName()+">");
            for (int i = 0; i < people.size(); i++){
                if ( people.get(i).getId() == temp.getId()){
                    people.remove(i);
                    try {
                        gateways.PersonGateway.deletePerson(MainController.getInstance().getSession(), temp.getId());
                    }catch(UnknownException e){
                        LOGGER.info("unknown exception encountered while deleting person");
                    }
                }
            }
        }
        try {
            MainController.getInstance().switchView(ScreenType.PERSONLIST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        personList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        for (int i = 0; i < people.size(); i++){
            personList.getItems().add(people.get(i));
        }
    }
}
