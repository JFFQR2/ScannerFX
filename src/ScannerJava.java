import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by quite on 08.04.17.
 */
public class ScannerJava  extends Thread{
        String mask;
        boolean includeSubdirectories;
        boolean autoDelete;
        private long interval;
        String inDirectory;
        String outDirectory;
        private File[] files;
        private boolean interapted;
        TextArea area = new TextArea();
        Alert alert;


        MyFilter filter = null;
        void copyWithOutSubdirectories(){
            filter = new MyFilter(mask);
            File outputDirectory = new File(outDirectory);
            File currentDirectory = new File(inDirectory);
            if (!currentDirectory.isDirectory()){
                if (Main.isNull){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            alertError();
                        }
                    });
                }
                System.out.println("This is not a directory or directory exists");
                System.exit(0);
            } else if (!outputDirectory.exists()){
                Path path = Paths.get(outDirectory);
                try {
                    Files.createDirectories(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Scanning folder -" + currentDirectory);
            files = currentDirectory.listFiles(filter);
            for (File file : files) {
                if (file.isDirectory()){
                    area.appendText("Exclude "+ file.getName()+"\n\r");
                    System.out.println("Exclude "+ file.getName());
                }
                copy(file, outputDirectory);
            }
        }
        void copy() {
            filter = new MyFilter(mask);
            File currentFolder = new File(inDirectory);
            if (!currentFolder.isDirectory()){
                if (Main.isNull){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            alertError();
                        }
                    });
                }
                System.out.println("This not a directory or directory exists");
                System.exit(0);
            }
            File outputDirectory = new File(outDirectory);
            if (!outputDirectory.exists()){
                Path path = Paths.get(outDirectory);
                try {
                    Files.createDirectories(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            scanDirectory(currentFolder, outputDirectory);
        }

        void scanDirectory(File inDirectory, File outDirectory) {
            area.appendText("Scanning directory -" + inDirectory+"\n\r");
            System.out.println("Scanning directory -" + inDirectory);
            files = inDirectory.listFiles(filter);
            for (File file : files) {
                if (file.isDirectory()) {
                    scanDirectory(file, outDirectory);
                } else {
                    copy(file, outDirectory);
                }
            }
        }

        void copy(File file, File outputDirectory) {
            try (InputStream input = new FileInputStream(file);
                 OutputStream out = new FileOutputStream(new File(outputDirectory + File.separator + file.getName()))) {
                System.out.println("Copying " + file + " to directory " + outputDirectory);
                area.appendText("Copying " + file + " to directory " + outputDirectory+"\n\r");
                byte data[] = new byte[input.available()];
                input.read(data);
                out.write(data);
            } catch (Exception e) {
                System.out.println();
            }
        }

        private void waitInterval(long interval){
            try {
                sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        void autoDelete(File file)
        {
            if(!file.exists()||!autoDelete) {
                System.out.println("auto delete false");
                return;
            }
            if(file.isDirectory())
            {
                for(File f : file.listFiles()) {
                    autoDelete(f);
                }
            }
            else
            {
                try {
                    Files.delete(Paths.get(file.getAbsolutePath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("deleted " + file.getName());
                area.appendText("deleted " + file.getName()+"\n\r");
            }
        }
        @Override
        public void run() {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
                System.out.println("Please, enter the copying directory");
                inDirectory = br.readLine();
                System.out.println("Please, enter the output directory");
                outDirectory = br.readLine();
                System.out.println("Delete the data from the copied directory ? ");
                System.out.println("false/true");
                autoDelete = Boolean.parseBoolean(br.readLine());
                System.out.println("Include subdirectories or not ?");
                System.out.println("false/true");
                includeSubdirectories = Boolean.parseBoolean(br.readLine());
//            System.out.println("Please enter a wait interval in milliseconds");
//            interval=Long.parseLong(br.readLine());
                System.out.println("Please, enter the mask");
                mask=br.readLine();
                System.out.println(Thread.currentThread().getName()+" start.");
                // while (!interrupted()){
                //waitInterval(interval);
                if (includeSubdirectories){
                    copy();
                } else {
                    copyWithOutSubdirectories();
                }
                autoDelete(new File(inDirectory));
                // }

            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Done!");
        }
        private final class MyFilter implements FileFilter {
            private final String fileType;

            private MyFilter(String fileType) {
                this.fileType = fileType;
            }

            public boolean accept(File pathname) {
                return pathname.getName().endsWith("." + fileType) || pathname.isDirectory();
            }
        }

        private void alertError(){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("This not a directory or directory exists");
            alert.showAndWait();
        }
    }
