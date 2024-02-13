package com.example.proiectdatabase;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.File;
import java.sql.*;
import java.util.ResourceBundle;
import java.net.URL;

public class LoginController implements Initializable {
   @FXML
   private Button cancelButton;
    @FXML
    private Button loginButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private ImageView brandingImageView;
    @FXML
    private ImageView logosImageView;
    @FXML
    private TextField usernametextField;
    @FXML
    private PasswordField enterPasswordField;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        //function to set images because idk why it doesn t work directily from fxml
        File brandingFile=new File("C:\\Users\\orian\\JAVAAAAA\\proiectdatabase\\src\\main\\java\\com\\images\\Starwarslogo.jpg");
        Image brandingImage =new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);
        File logosFile=new File("C:\\Users\\orian\\JAVAAAAA\\proiectdatabase\\src\\main\\java\\com\\images\\HD-wallpaper-star-wars-logo-for-your-mobile-tablet-explore-star-wars-logo-star-wars-jedi-logo-star-wars-empire-logo.jpg");
        Image logosImage =new Image(logosFile.toURI().toString());
        logosImageView.setImage(logosImage);
    }
    public void loginButtonOnAction(ActionEvent event){//login button is pressed

        if(usernametextField.getText().isBlank()==false&&enterPasswordField.getText().isBlank()==false){
            validateLogin();//if the textfields are not empty check to validate

        }else{
            loginMessageLabel.setText("Please enter username and password");//else please fill the fields
        }

    }
    public void cancelButtonOnAction(ActionEvent event){
        Stage stage= (Stage)  cancelButton.getScene().getWindow();
        stage.close();//cancel button is pressed
    }
    private static User loggedInUser;
    public void validateLogin() {//create the database connection
        DbFunctions connectNow = new DbFunctions();
        Connection connectDB = connectNow.getConnection();
        //SQL queries to check the username existance and validate login
        String checkUsernameExists = "SELECT count(1) FROM user_account WHERE username = ?";
        String verifyLogin = "SELECT count(1) FROM user_account WHERE username = ? AND password = ?";

        try {
            //check if the username exists in the database
            PreparedStatement checkUsernameStatement = connectDB.prepareStatement(checkUsernameExists);
            checkUsernameStatement.setString(1, usernametextField.getText());
            ResultSet usernameResult = checkUsernameStatement.executeQuery();
            usernameResult.next();
            //get the count of usernames found
            int usernameCount = usernameResult.getInt(1);

            if (usernameCount == 1) {
                //username exists, now check password
                PreparedStatement verifyStatement = connectDB.prepareStatement(verifyLogin);
                verifyStatement.setString(1, usernametextField.getText());
                verifyStatement.setString(2, enterPasswordField.getText());
                ResultSet passwordResult = verifyStatement.executeQuery();
                passwordResult.next();

                if (passwordResult.getInt(1) == 1) {
                    //password is correct, proceed with login
                    loggedInUser = fetchLoggedInUser(usernametextField.getText());
                    movietableForm();
                } else {
                    //incorrect password
                    loginMessageLabel.setText("Incorrect password. Please try again.");
                }
            } else {
                //username does not exist
                loginMessageLabel.setText("Username does not exist. Please press login again to register.");
                createAccountForm();
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }
    }

    private User fetchLoggedInUser(String username) {//method to fetch the logged in user from database
        User user = null;
        String query = "SELECT account_id, username FROM user_account WHERE username = ?";
        Connection connection = null;
        try {
            DbFunctions dbFunctions = new DbFunctions();
            connection = dbFunctions.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("account_id");
                String fetchedUsername = resultSet.getString("username");
                user = new User(userId, fetchedUsername);
            }

            preparedStatement.close();
        } catch (SQLException e) {

        }

        return user;
    }
    public static User getLoggedInUser() {
        return loggedInUser;
    }
    public void createAccountForm(){
        try{


            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("register.fxml"));
            Stage registerStage = new Stage();
            Scene scene = new Scene(fxmlLoader.load(), 520, 567);
            registerStage.setTitle("Oops looks like you don't have an account, please register");
            registerStage.setScene(scene);
            registerStage.show();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();

        }
    }
    public void movietableForm(){
        try{


            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("movies.fxml"));
            Stage registerStage = new Stage();
            Scene scene = new Scene(fxmlLoader.load(),  691,522);
            MovieTable movieTableController = fxmlLoader.getController();
            movieTableController.setLoggedInUser(loggedInUser);

            registerStage.setTitle("Star Wars");
            registerStage.setScene(scene);
            registerStage.show();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();

        }

    }

}