import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.scene.image.ImageView;
//import javafx.scene.image.Image;
import javafx.application.Platform;
 
public class JIMachine extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    private static int imgSize = 0; // to controle current scale factor of the image

    // Create Node ImageView object
    // (Image object is not a Node,
    // and cannot be used with BorderPain directly)
    private ImageView img;
    //private Image img;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JIMachine by Oleksii Kolesnyk");

        final FileChooser fileChooser = new FileChooser(); // Create file chooser

        // Buttons
        final Button openButton = new Button("Open");
        final Button zoomInButton = new Button("Zoom +");
        final Button zoomOutButton = new Button("Zoom -");
        final Button restoreSizeButton = new Button("100%");
        final Button quitButton = new Button("Quit");

        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(800,600); // Set initial size
        HBox toolbar = new HBox(10); // toolbar with buttons, spacing 10px
        borderPane.setTop(toolbar); // Set toolbar to the top
        toolbar.setPadding(new Insets(5, 5, 5, 5)); // Set padding for top toolbar
        toolbar.getChildren().addAll(openButton, zoomInButton, zoomOutButton, restoreSizeButton, quitButton);

        primaryStage.setScene(new Scene(borderPane)); // Create a scene
        primaryStage.show(); // Display scene

        // Make sure that app will really exit on window close
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

        openButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        configureFileChooser(fileChooser);
                        File file = fileChooser.showOpenDialog(primaryStage);
                        if (file != null) {
                            openFile(file); // open file
                            imgSize = 600; // sset initial size to 600px
                            setImageSize(imgSize); // Normal image size, 100%
                            borderPane.setCenter(img); // Set image
                        }
                    }
                });

        zoomInButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        zoomIn();
                    }
                });

        restoreSizeButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        setImageSize(600); // Normal image size, 100%
                    }
                });

        zoomOutButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        zoomOut();
                    }
                });

        quitButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        primaryStage.close();
                    }
                });
    } // End start()

    // Change image size
    private void setImageSize(int size){
        // Check if picture was loaded
        if(imgSize != 0){
            img.setFitWidth(size);
            img.setPreserveRatio(true);
            img.setSmooth(true);
            img.setCache(true);
            imgSize = size; // change size variable
        }
    }

    // Zoom in
    // no more then five zooms
    // above normal size
    // changes in range from 100 to 1100 px in width
    private void zoomIn(){
        if(imgSize >= 100 && imgSize <= 1000) {
            setImageSize(imgSize + 100);
        }
    }

    // Zoom out
    // no more then five zooms
    // below normal size
    // changes in range from 100 to 1100 px in width
    private void zoomOut(){
        if(imgSize <= 1100 && imgSize >= 200){
            setImageSize(imgSize - 100);
        }
    }

    // File choser dialog configuration
    private static void configureFileChooser(
            final FileChooser fileChooser) {
        fileChooser.setTitle("Select Image");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All files", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
    }

    // Open file method
    // Will try to open file
    // If faled, will log error
    private void openFile(File file) {
        try {
            img = new ImageView(file.toURI().toURL().toExternalForm());
            //img = new Image(file.toURI().toURL().toExternalForm());
        } catch (IOException ex) {
            Logger.getLogger(JIMachine.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }
    }
}
