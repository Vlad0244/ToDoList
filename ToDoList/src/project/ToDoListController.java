package project;

import java.io.File;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
/*
Author: Volodymyr Suprun
Student ID: 991659490
*/
public class ToDoListController {

    public void initialize() {
        //Allows for selecting multiple entries
        listViewGroup1.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listViewGroup2.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
    
    //Used when sorting file and organizing it into the Groups. These values can be changed and set to something else
    private String group1 = "Group1";
    private String group2 = "Group2";
    private String completed = "###Completed";

    @FXML
    private Button buttonDeleteItem;

    @FXML
    private Button buttonCompleted;

    @FXML
    private Button buttonExport;

    @FXML
    private Button buttonImport;
    
    @FXML
    private Button buttonNameChange;

    @FXML
    private ListView<String> listViewGroup1;

    @FXML
    private ListView<String> listViewGroup2;

    @FXML
    private ListView<String> listViewCompleted;

    @FXML
    private MenuItem menuGroup1;

    @FXML
    private MenuItem menuGroup2;

    @FXML
    private Tab tabGroup1;

    @FXML
    private Tab tabGroup2;

    @FXML
    private Tab tabCompleted;

    @FXML
    private TabPane tabPane;

    @FXML
    private TextField textFieldNewItem;
    
    @FXML
    private TextField textFieldNameChange;

    @FXML
    private Label labelIfEmpty;

    @FXML
    private Label labelIfFileEmpty;
    
    @FXML
    private Label labelIfNameEmpty;

    //Adds to Group1 upon menubutton press given something is written in textbox
    @FXML
    void addToGroup1(ActionEvent event) {

        if (textFieldNewItem.getText().equals("")) {
            labelIfEmpty.setText("You have not written anything!");
            return;
        }

        else {
            tabPane.getSelectionModel().select(tabGroup1);
            labelIfEmpty.setText("");
            listViewGroup1.getItems().add("- " + textFieldNewItem.getText());
            textFieldNewItem.setText("");
        }
    }

    //Adds to Group2 upon menubutton press given something is written in textbox
    @FXML
    void addToGroup2(ActionEvent event) {

        if (textFieldNewItem.getText().isBlank()) {
            labelIfEmpty.setText("You have not written anything!");
            return;
        }

        else {
            tabPane.getSelectionModel().select(tabGroup2);
            labelIfEmpty.setText("");
            listViewGroup2.getItems().add("- " + textFieldNewItem.getText());
            textFieldNewItem.setText("");
        }
    }

    //Deletes item(s) that are selected. If the item selected has duplicates they will be deleted as well; 
    //unsure how to change this while keeping multiple item select.
    @FXML
    void handleItemDelete(ActionEvent event) {

        if (tabPane.getSelectionModel().getSelectedItem().equals(tabGroup1)) {
            ObservableList<String> list1 = listViewGroup1.getSelectionModel().getSelectedItems();
            listViewGroup1.getItems().removeAll(list1);
        }

        else if (tabPane.getSelectionModel().getSelectedItem().equals(tabGroup2)) {
            ObservableList<String> list2 = listViewGroup2.getSelectionModel().getSelectedItems();
            listViewGroup2.getItems().removeAll(list2);
        }
    }
    
    //Moves item(s) to Completed Tab. Does not change view to it so you can continue selecting in it.
    //Same issue as handleItemDelete, moves all duplicates.
    @FXML
    void handleItemCompleted(ActionEvent event) {

        if (tabPane.getSelectionModel().getSelectedItem().equals(tabGroup1)) {
            ObservableList<String> list1 = listViewGroup1.getSelectionModel().getSelectedItems();
            for (String item : list1) {
                listViewCompleted.getItems().add(item + " [" + group1 + "]");
            }
            listViewGroup1.getItems().removeAll(list1);
        }

        else if (tabPane.getSelectionModel().getSelectedItem().equals(tabGroup2)) {
            ObservableList<String> list2 = listViewGroup2.getSelectionModel().getSelectedItems();
            for (String item : list2) {
                listViewCompleted.getItems().add(item + " [" + group2 + "]");
            }
            listViewGroup2.getItems().removeAll(list2);
        }
        
    }
    
    //Saves all todo items to your disk. The file is organized into which tabs the entries were in.
    @FXML
    void handleToDoExport(ActionEvent event) {
        String textToSave = "";
        FileChooser chooser = new FileChooser();
        File file = chooser.showSaveDialog(buttonExport.getScene().getWindow());
        FileManager fm = new FileManager();
        textToSave += "###Group 1: " + group1 + "\n";
        if(listViewGroup1.getItems() != null) {
            for (int i = 0; i < listViewGroup1.getItems().size() ; i++) {
                textToSave += listViewGroup1.getItems().get(i) + "\n";
            }
        }
        textToSave += "###Group 2: " + group2 + "\n";
        if(listViewGroup2.getItems() != null) {
            for (int i = 0; i < listViewGroup2.getItems().size() ; i++) {
                textToSave += listViewGroup2.getItems().get(i) + "\n";
            }
        }
        textToSave += completed;
        if(listViewCompleted.getItems() != null) {
            textToSave += "\n";
            for (int i = 0; i < listViewCompleted.getItems().size() ; i++) {
                textToSave += listViewCompleted.getItems().get(i) + "\n";
            }
        }
        fm.saveListsToFile(file, textToSave);
    }

    //Reads a file that was previously exported and adds entries to each respective tab.
    @FXML
    void handleToDoImport(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(buttonImport.getScene().getWindow());
        FileManager fm = new FileManager();
        ArrayList<String> textToLoad = fm.loadListsFromDisk(file);
        //checks if ArrayList from loadListsFromDisk is empty
        if (textToLoad.size() <= 4) {
            labelIfFileEmpty.setText("File is empty!");
            return;
        }
        labelIfFileEmpty.setText("");
        //removes newline added by println in saveListsToFile at the very end of the list
        textToLoad.remove(textToLoad.size() - 1);
        //calls function that changes names of every Tab and names of tabs to those in the file
        changeImportedNames(textToLoad);
        int indexOfItem = 1;
        while (!(textToLoad.get(indexOfItem).equals("###Group 2: " + group2))) {
            listViewGroup1.getItems().add(textToLoad.get(indexOfItem));
            indexOfItem++;
        }
        
        indexOfItem++;
        while (!(textToLoad.get(indexOfItem).equals(completed))) {
            listViewGroup2.getItems().add(textToLoad.get(indexOfItem));
            indexOfItem++;
        }
        indexOfItem++;
        while (indexOfItem < textToLoad.size()) {
            listViewCompleted.getItems().add(textToLoad.get(indexOfItem));
            indexOfItem++;
        }
    }
    
    //Changes names of tabs and menus to what is written in the field
    @FXML
    void handleNameChange(ActionEvent event) {
        if (textFieldNameChange.getText().isBlank()) {
            labelIfNameEmpty.setText("Field is empty!");
            return;
        }
        if (tabPane.getSelectionModel().getSelectedItem().equals(tabGroup1)) {
            labelIfNameEmpty.setText("");
            tabGroup1.setText(textFieldNameChange.getText());
            menuGroup1.setText(textFieldNameChange.getText());
            this.group1 = textFieldNameChange.getText();
            textFieldNameChange.setText("");
        }

        else if (tabPane.getSelectionModel().getSelectedItem().equals(tabGroup2)) {
            labelIfNameEmpty.setText("");
            tabGroup2.setText(textFieldNameChange.getText());
            menuGroup2.setText(textFieldNameChange.getText());
            this.group2 = textFieldNameChange.getText();
            textFieldNameChange.setText("");
        }
    }

    //changes names of every Tab and menu to those in the file
    @FXML
    void changeImportedNames(ArrayList<String> textToLoad) {

        for (String item : textToLoad) {
            if (item.contains("###Group 1: ")) {
                String newNameGroup1 = item.substring(12);
                tabGroup1.setText(newNameGroup1);
                menuGroup1.setText(newNameGroup1);
                this.group1 = newNameGroup1;
            }
            if (item.contains("###Group 2: ")) {
                String newNameGroup2 = item.substring(12);
                tabGroup2.setText(newNameGroup2);
                menuGroup2.setText(newNameGroup2);
                this.group2 = newNameGroup2;
            }
        }
    }
}