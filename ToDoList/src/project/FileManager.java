package project;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
/*
Author: Volodymyr Suprun
Student ID: 991659490
*/
public class FileManager {
    public void saveListsToFile(File file, String text) {
        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.println(text);
        }
        catch (Exception err) {
            System.out.println("Error - File not saved");
        }
    }

    public ArrayList<String> loadListsFromDisk(File file) {
        ArrayList<String> importedText = new ArrayList<String>();
        try (Scanner scan = new Scanner(file)) {
           while (scan.hasNextLine()) {
               importedText.add(scan.nextLine());
           }
        }
        catch (Exception err) {
            System.out.println("Error - file not loaded");
        }
        return importedText;
    }
}
