package sample;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class Main extends Application
{


    public static Stage parentWindow;
    public static  Scene s;

    public static void main(String[] args)  throws TesseractException {


        launch(args);

    }


    @Override
    public void start(Stage primaryStage) throws Exception {


        parentWindow = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

       // Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
     //   double width = resolution.getWidth();
      //  double height = resolution.getHeight();
      //  double w = width/1200;  //your window width
       // double h = height/1000;  //your window hight
      //  Scale scale = new Scale(800, 800, 0, 0);
       // root.getTransforms().add(scale);


        s = new Scene(root, 800,800);


        parentWindow.setScene(s);
        parentWindow.setResizable(false);
        parentWindow.show();

        primaryStage.setOnCloseRequest(e -> handleExit());

    }


    private void handleExit(){

        Platform.exit();
        System.exit(0);

    }
}
