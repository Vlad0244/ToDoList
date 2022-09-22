package project;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/*
Author: Volodymyr Suprun
Student ID: 991659490
*/
public class App extends Application{
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ToDoList.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
