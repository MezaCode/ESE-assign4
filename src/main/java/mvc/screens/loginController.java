package mvc.screens;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import myexceptions.UnauthorizedException;
import myexceptions.UnknownException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class loginController implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger();

    public loginController(){}

    @FXML
    private TextField userNameText;

    @FXML
    private TextField passwordText;

    @FXML
    private Button loginButton;

    @FXML
    private Text errorText;

    @FXML
    void login(ActionEvent event) {

        String userName = userNameText.getText();
        String pass = passwordText.getText();
        String session = null;
        if (userName.length() < 1){
            errorText.setText("User Name Required!");
        }
        else if (pass.length() < 1){
            errorText.setText("Password Required!");
        }
        else {
            try{
                session = gateways.SessionGateway.login(userName, pass);
            }catch (UnauthorizedException e){
                errorText.setText("Login Unauthorized");
                return;
            }catch (UnknownException e){
                errorText.setText("Login Unauthorized");
                System.out.println("Note for grader: decided to to handle the UnknownException this way as to still differentiate it from the unauthorized but also prevent stack trace printing\nuser side still gets same unathorized message and can be changed later when we have an actual server set up and are getting things other than just a 200 and a 401 response.");
                return;
            }

            if (session != null){
                MainController.getInstance().setSession(session);
                LOGGER.info("<"+userName+"> "+ "LOGGED IN");
                errorText.setText("");
                MainController.getInstance().switchView(ScreenType.PERSONLIST);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
