import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class GraphicsScanner extends Stage {
    private int width = 480;
    private int height = 440;

    public GraphicsScanner() {
        setTitle("Graphics Scanner");
        Panel panel = new Panel();
        setScene(new Scene(panel,width,height));
        setResizable(false);
        show();
    }

    private class Panel extends Pane {
        private ScannerJava scannerJava = new ScannerJava();
        private final String inDirectory = "Please, enter the copying directory";
        private final String outDirectory = "Please, enter the output directory";
        private final String No = "No";
        private final String Yes = "Yes";
        private DirectoryChooser chooser;
        private Button buttonIn, buttonOut, scanner;
        private Label labelIn, labelOut, autoDelete, includeSubDir, maskInf;
        private TextField fieldIn, fieldOut, mask;
        private RadioButton aFalse, aTrue, sFalse, sTrue;
        private ToggleGroup groupA, groupS;
        private Stage chooserStage;
        private Alert alert;

        public Panel() {
            setPrefSize(width, height);
            buttonIn = new Button("Copying directory");
            buttonIn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    chooser(inDirectory,fieldIn);
                }
            });
            labelIn = new Label(inDirectory);
            fieldIn = new TextField();
            labelOut = new Label(outDirectory);
            buttonOut = new Button("Output Directory");
            buttonOut.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    chooser(outDirectory,fieldOut);
                }
            });
            groupA = new ToggleGroup();
            groupS = new ToggleGroup();
            fieldOut = new TextField();
            autoDelete = new Label("Delete the data from the copied directory ?");
            aFalse = new RadioButton(No);
            aFalse.setSelected(true);
            aTrue = new RadioButton(Yes);
            aFalse.setToggleGroup(groupA);
            aTrue.setToggleGroup(groupA);
            aFalse.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    scannerJava.autoDelete = false;
                }
            });
            aTrue.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    scannerJava.autoDelete = true;
                }
            });
            includeSubDir = new Label("Include subdirectories or not ?");
            sFalse = new RadioButton(No);
            sFalse.setSelected(true);
            sTrue = new RadioButton(Yes);
            sFalse.setToggleGroup(groupS);
            sTrue.setToggleGroup(groupS);
            sFalse.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    scannerJava.includeSubdirectories = false;
                }
            });
            sTrue.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    scannerJava.includeSubdirectories = true;
                }
            });
            maskInf = new Label("Please, enter a mask \"jpg,png,txt etc\"(without dot)");
            mask = new TextField();
            scanner = new Button("Scanner");

            scanner.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (fieldIn.getText().isEmpty()) {
                        alertError(inDirectory);
                        return;
                    }
                    if (fieldOut.getText().isEmpty()) {
                        alertError(outDirectory);
                        return;
                    }
                    if (mask.getText().isEmpty()) {
                        alertError("Please, enter a mask");
                        return;
                    }
                    scannerJava.inDirectory = fieldIn.getText();
                    scannerJava.outDirectory = fieldOut.getText();
                    scannerJava.mask = mask.getText();
                    if (scannerJava.includeSubdirectories) {
                        scannerJava.copy();
                    } else {
                        scannerJava.copyWithOutSubdirectories();
                    }
                    scannerJava.autoDelete(new File(scannerJava.inDirectory));
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Done");
                    alert.setHeaderText("Files Copied!");
                    alert.showAndWait();
                }
            });
            scannerJava.area.setWrapText(true);
            scannerJava.area.setEditable(false);
            ScrollPane panelArea = new ScrollPane(scannerJava.area);
            panelArea.setPrefSize(450, 100);
            labelIn.setLayoutX(20);
            labelIn.setLayoutY(0);
            labelIn.setPrefSize(260, 20);
            buttonIn.setLayoutX(280);
            buttonIn.setLayoutY(5);
            buttonIn.setPrefSize(160, 20);
            fieldIn.setLayoutX(20);
            fieldIn.setLayoutY(30);
            fieldIn.setPrefSize(400, 20);
            labelOut.setLayoutX(20);
            labelOut.setLayoutY(50);
            labelOut.setPrefSize(260, 20);
            buttonOut.setLayoutX(280);
            buttonOut.setLayoutY(55);
            buttonOut.setPrefSize(160, 20);
            fieldOut.setLayoutX(20);
            fieldOut.setLayoutY(80);
            fieldOut.setPrefSize(400, 20);
            autoDelete.setLayoutX(20);
            autoDelete.setLayoutY(110);
            autoDelete.setPrefSize(480, 20);
            aFalse.setLayoutX(20);
            aFalse.setLayoutY(130);
            aFalse.setPrefSize(100, 20);
            aTrue.setLayoutX(20);
            aTrue.setLayoutY(150);
            aTrue.setPrefSize(100, 20);
            includeSubDir.setLayoutX(20);
            includeSubDir.setLayoutY(170);
            includeSubDir.setPrefSize(480, 20);
            sFalse.setLayoutX(20);
            sFalse.setLayoutY(190);
            sFalse.setPrefSize(100, 20);
            sTrue.setLayoutX(20);
            sTrue.setLayoutY(210);
            sTrue.setPrefSize(100, 20);
            maskInf.setLayoutX(20);
            maskInf.setLayoutY(230);
            maskInf.setPrefSize(480, 20);
            mask.setLayoutX(20);
            mask.setLayoutY(260);
            mask.setPrefSize(400, 20);
            panelArea.setLayoutX(20);
            panelArea.setLayoutY(290);
            panelArea.setPrefSize(440, 100);
            scanner.setLayoutX(190);
            scanner.setLayoutY(390);
            scanner.setPrefSize(100, 20);
            getChildren().addAll(labelIn,buttonIn,fieldIn,labelOut,buttonOut,fieldOut,autoDelete,aFalse,
                    aTrue,includeSubDir,sFalse,sTrue,maskInf,mask,panelArea,scanner);
        }

        private void chooser(String title,TextField field) {
            if (chooser == null) {
                chooser = new DirectoryChooser();
            }
            if (chooserStage == null) {
                chooserStage = new Stage();
            }
            chooser.setTitle(title);
            File selectedDirectory = chooser.showDialog(chooserStage);
            if (selectedDirectory != null)
                field.setText(selectedDirectory.getAbsolutePath());
        }

        private void alertError(String head){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(head);
            alert.showAndWait();
        }
    }
}
