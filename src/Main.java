import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Main extends Application implements EventHandler{
private GraphicsScanner graphics = null;
private ScannerJava console = null;
private Button graphicsButton;
private Button consoleButton;
static boolean isNull = false;
    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Scanner");
        FlowPane pane = new FlowPane();
        graphicsButton = new Button("Graphics");
        consoleButton = new Button("Console");
        graphicsButton.setPrefSize(160,120);
        consoleButton.setPrefSize(160,120);
        graphicsButton.setOnAction(this);
        consoleButton.setOnAction(this);
        pane.getChildren().addAll(graphicsButton,consoleButton);
        primaryStage.setScene(new Scene(pane,320,120));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void handle(Event event) {
        if (event.getSource()==graphicsButton){
            if (graphics==null){
                graphics = new GraphicsScanner();
                isNull=true;
            }
        } if (event.getSource()==consoleButton){
            if (console==null){
                console=new ScannerJava();
                console.start();
            }
        }
    }
}
