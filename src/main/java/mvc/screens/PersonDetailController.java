package mvc.screens;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import mvc.model.Person;
import myexceptions.UnknownException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PersonDetailController implements Initializable {
    Person person;
    private static final Logger LOGGER = LogManager.getLogger();

    @FXML
    private TextField firstNameText;

    @FXML
    private TextField lastNameText;

    @FXML
    private TextField dobText;

    @FXML
    private Text ageTitleText;

    @FXML
    private Text ageDisplayText;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    public PersonDetailController(Person p){
        this.person = p;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        firstNameText.setText(person.getFirstName());
        lastNameText.setText(person.getLastName());
        if (person.getDateOfBirth() == null){
            dobText.setText("");
        }
        else{
            dobText.setText(person.getDateOfBirth().toString());
        }

        if (person.getDateOfBirth() != null){
            ageTitleText.setText("Age :");
            ageDisplayText.setText(person.getAge() + "");
        }
    }

    @FXML
    void savePerson(ActionEvent event) {
        ArrayList<Person> people = MainController.getInstance().getPeople();
        String firstName =firstNameText.getText();
        String lastName =lastNameText.getText();
        LocalDate dob = LocalDate.parse(dobText.getText());
        if (person.getId() == 0){
            LOGGER.info("CREATING <"+person.getFirstName()+" "+person.getLastName()+">");
            int newID=0;
            try {
                newID = gateways.PersonGateway.insertPerson(MainController.getInstance().getSession(), firstName, lastName, dob.toString());
            }catch (UnknownException e){
                LOGGER.info("unknown exception encounter while creating new person");
            }
            try{
                gateways.PersonGateway.addAuditTrail(MainController.getInstance().getSession(), "", "added", "", newID);
            }catch(Exception e){
                System.out.println("");
            }
            person.setFirstName(firstName);
            person.setLastName(lastName);
            person.setDateOfBirth(dob);
            person.setId(newID);
            people.add(person);
        }
        else{
            LOGGER.info("UPDATING <"+person.getFirstName()+" "+person.getLastName()+">");
            int id;
            System.out.println("------------------------------------------------------------------------------------------------------------------");
            System.out.println("" + this.person.getId());
            System.out.println("" + this.person.getFirstName().length() + "   "+ firstName.length());
            System.out.println("" + this.person.getLastName().length() + "   "+ lastName.length());
            System.out.println("" + this.person.getDateOfBirth() + "   "+ dob);
            System.out.println("------------------------------------------------------------------------------------------------------------------");

            try {

                if (!firstName.equals(person.getFirstName())) {
                    gateways.PersonGateway.updatePerson(MainController.getInstance().getSession(), firstName, "first_name", person.getFirstName(),person.getId());
                }
                if (!lastName.equals(person.getLastName())) {
                    gateways.PersonGateway.updatePerson(MainController.getInstance().getSession(), lastName, "last_name", person.getLastName(), person.getId());
                }
                if (dob.compareTo(person.getDateOfBirth()) !=0) {
                    gateways.PersonGateway.updatePerson(MainController.getInstance().getSession(), dob.toString(), "dob", person.getDateOfBirth().toString(), person.getId());
                }
            }catch (UnknownException e){
                LOGGER.info("unknown exception encounter while creating new person");
            }
            for (int i = 0; i < people.size(); i++){
                if ( people.get(i).getId() == person.getId()){
                    people.set(i, person);
                }
            }
        }
        MainController.getInstance().setPeople(people);
        MainController.getInstance().switchView(ScreenType.PERSONLIST);

    }

    @FXML
    void cancelPerson(ActionEvent event) {
        MainController.getInstance().switchView(ScreenType.PERSONLIST);
    }
}
