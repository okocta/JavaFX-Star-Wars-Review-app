package com.example.proiectdatabase;


import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MovieTable implements Initializable {
    @FXML
    private ImageView logomImageView;
    @FXML
    private TableView<Movie> tableView;
    @FXML
    private TableColumn<Movie,Integer>idcol;
    @FXML
    private TableColumn<Movie,String>namecol;
    @FXML
    private TextField select_id;
    @FXML
    private TextField select_rating;
    @FXML
    private TextArea select_comment;

    private final DbFunctions dbFunctions= new DbFunctions();
    private User loggedInUser;


    public void setLoggedInUser(User user) {
        loggedInUser = user;
    }


    public void initialize(URL url, ResourceBundle resourceBundle) {
        //same as the other ones

        File logoFile = new File("C:\\Users\\orian\\JAVAAAAA\\proiectdatabase\\src\\main\\java\\com\\images\\HD-wallpaper-star-wars-logo-for-your-mobile-tablet-explore-star-wars-logo-star-wars-jedi-logo-star-wars-empire-logo.jpg");
        Image logomImage = new Image(logoFile.toURI().toString());
        logomImageView.setImage(logomImage);
        //set cell value factories for each column
        idcol.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        namecol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        //fetch data from the database and populate the TableView
        populateTableView();

    }
    private void populateTableView() {
        Connection connection = dbFunctions.getConnection();

        if (connection != null) {
            try {
                //SQL query to retrieve data
                String sqlQuery = "SELECT movie_id, movie_title FROM starwarsmovies";

                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                ResultSet resultSet = preparedStatement.executeQuery();


                while (resultSet.next()) {
                    //create Movie objects and add them to the TableView
                    int id = resultSet.getInt("movie_id");
                    String name = resultSet.getString("movie_title");
                    tableView.getItems().add(new Movie(id, name));
                }

                resultSet.close();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void submitReview(ActionEvent event) {
        int movieId = Integer.parseInt(select_id.getText());//get the id and rating and comment from the text fields and area
        int rating = Integer.parseInt(select_rating.getText());
        String comment = select_comment.getText();

        if (validateInputs(movieId, rating)) {
            insertReviewIntoDatabase(movieId, rating, comment);
            clearFields();//clear input fields
        } else {

        }
    }
    private boolean validateInputs(int movieId, int rating) {

        return (movieId >= 1 && movieId <= 9) && (rating >= 1 && rating <= 10);
    }
    @FXML
    private void eraseFields(ActionEvent event) {
        clearFields();
    }
    private void clearFields() {
        select_id.clear();
        select_rating.clear();
        select_comment.clear();
    }
    private void insertReviewIntoDatabase(int movieId, int rating, String comment) {
        DbFunctions dbFunctions = new DbFunctions();//get a database connection
        Connection connection = dbFunctions.getConnection();

        if (connection != null) {
            try {
                String sqlQuery = "INSERT INTO moviereviews (movie_id, user_id, rating, comment) VALUES (?, ?, ?, ?)";

                int userId = loggedInUser.getUserId();

                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                preparedStatement.setInt(1, movieId);
                preparedStatement.setInt(2, userId);
                preparedStatement.setInt(3, rating);
                preparedStatement.setString(4, comment);

                preparedStatement.executeUpdate();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void showMovieReviews() {
        Movie selectedMovie = tableView.getSelectionModel().getSelectedItem();

        if (selectedMovie != null) {
            int selectedMovieId = selectedMovie.getId();
            // Fetch and display reviews for the selected movie
            displayMovieReviews(selectedMovieId);
        }
    }
    private void displayMovieReviews(int movieId) {
        //fetch movie reviews from the database for the selected movieId
        ObservableList<MovieReview> reviews = fetchMovieReviews(movieId);

        //create a new stage to display the reviews
        Stage reviewsStage = new Stage();
        reviewsStage.setTitle("Reviews for Selected Movie");

        //create a TableView to display reviews
        TableView<MovieReview> reviewsTable = new TableView<>(); //create a TableView for reviews
        //create a TableColumn named "User" to display usernames
        TableColumn<MovieReview, String> userColumn = new TableColumn<>("User");
        //set the cell value factory for the "User" column to get usernames from MovieReview objects
        userColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<MovieReview, Integer> ratingColumn = new TableColumn<>("Rating");
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        TableColumn<MovieReview, String> commentColumn = new TableColumn<>("Comment");
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        reviewsTable.getColumns().addAll(userColumn, ratingColumn, commentColumn);//add columns to tableview
        reviewsTable.setItems(reviews);
        //create a VBox layout to hold the TableView
        VBox vbox = new VBox(new Label("Reviews for the selected movie:"), reviewsTable);
        //create a Scene and set it in the Stage
        Scene scene = new Scene(vbox, 600, 400);
        reviewsStage.setScene(scene);
        //show the stage
        reviewsStage.show();
    }

    //method to fetch reviews from the database based on movieId
    private ObservableList<MovieReview> fetchMovieReviews(int movieId) {
        ObservableList<MovieReview> reviews = FXCollections.observableArrayList();
        //fetch reviews for the selected movieId from the database
        //assume MovieReview is a class representing movie reviews with username, rating, and comment fields

        DbFunctions dbFunctions = new DbFunctions();
        Connection connection = dbFunctions.getConnection();

        if (connection != null) {
            try {
                String sqlQuery = "SELECT u.username, mr.rating, mr.comment " +
                        "FROM moviereviews mr " +
                        "JOIN user_account u ON mr.user_id = u.account_id " +
                        "WHERE mr.movie_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                preparedStatement.setInt(1, movieId);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String username = resultSet.getString("username");
                    int rating = resultSet.getInt("rating");
                    String comment = resultSet.getString("comment");

                    reviews.add(new MovieReview(username, rating, comment));
                }

                resultSet.close();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return reviews;
    }

}
