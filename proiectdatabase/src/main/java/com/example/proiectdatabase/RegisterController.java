package com.example.proiectdatabase;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.sql.ResultSet;


import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    @FXML
    private ImageView logoImageView;
    @FXML
    private Button closeButton;
    @FXML
    private Label registrationMessageLabel;
    @FXML
    private Label confirmPasswordLabel;
    @FXML
    private TextField setPasswordField;
    @FXML
    private TextField confirmPasswordField;
    @FXML
    private TextField usernameTextField;



    public void initialize(URL url, ResourceBundle resourceBundle) {
        //same as in login controller

        File logoFile = new File("C:\\Users\\orian\\JAVAAAAA\\proiectdatabase\\src\\main\\java\\com\\images\\logostar.png");
        Image logoImage = new Image(logoFile.toURI().toString());
        logoImageView.setImage(logoImage);
    }
    public void registerButtonOnAction(ActionEvent event)
    {
        //check if passwords match
        if(setPasswordField.getText().equals(confirmPasswordField.getText())){
            registerUser();

            confirmPasswordLabel.setText("Passwords match");
        }else{
            confirmPasswordLabel.setText("Passwords do not match!");
        }


    }
    public void closeButtonOnAction(ActionEvent event)
    {//close the window
        Stage stage=(Stage) closeButton.getScene().getWindow();
        stage.close();

    }
    public void registerUser() {
        //create a database connection
        DbFunctions connectNow = new DbFunctions();
        Connection connectdb = connectNow.getConnection();
        //get username and password form the textfields
        String username = usernameTextField.getText();
        String password = setPasswordField.getText();
        try {
            //check if the username already exists in the database
            Statement checkStatement = connectdb.createStatement();
            String checkQuery = "SELECT COUNT(*) AS count FROM user_account WHERE username = '" + username + "'";
            ResultSet checkResult = checkStatement.executeQuery(checkQuery);
            checkResult.next();
            int usernameCount = checkResult.getInt("count");

            if (usernameCount > 0) {
                //ssername already exists
                registrationMessageLabel.setText("This username already exists");
            } else {
                //username is unique, proceed with registration
                String insertFields = "INSERT INTO user_account(username, password) VALUES ('";
                String insertValues = username + "','" + password + "')";
                String insertToRegister = insertFields + insertValues;

                Statement statement = connectdb.createStatement();
                statement.executeUpdate(insertToRegister);

                registrationMessageLabel.setText("User has been registered successfully!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

}
