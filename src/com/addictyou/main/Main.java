package com.addictyou.main;
	
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.addictyou.controller.OnlineTimeTracker;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Main extends Application {
	public static String logFilePath = "";
	static String path =  "";
	
	public static String sqlServerIp = "";
	public static String sqlServerPort = "";
	public static String sqlServerId = "";
	public static String sqlServerPass = "";
	public static String sqlServerSchema = "";
	
	public static String mysqlIp = "";
	public static String mysqlPort = "";
	public static String mysqlId = "";
	public static String mysqlPass = "";
	public static String mysqlSchema = "";
	
	private static boolean ifError = false;
	private static String error = "";

	final static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HHmmss");
	
	@Override
	public void start(Stage primaryStage) {
		try {
			if(ifError){
            	raiseErrorAlert();
            } else{
            	Parent root = FXMLLoader.load(getClass().getResource("/com/addictyou/main/MyScene.fxml"));
                primaryStage.setTitle("Battle of Immortals - Online Players Tracking");
                primaryStage.onCloseRequestProperty();
                primaryStage.setScene(new Scene(root));
                primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    public void handle(WindowEvent we) {
                    }
                });  
                primaryStage.show();
            }
        } catch(IllegalStateException e) {
           // do nothing
        } catch(Exception e) {
            e.printStackTrace();
        }
	}
	
	public static void main(String[] args) {
		String absolutePath = new File(".").getAbsolutePath().split("\\.")[0];
		path = absolutePath + "config.properties";
		
		Properties prop = new Properties();
		try (InputStream input = new FileInputStream(path)){
			prop.load(input);
			logFilePath = prop.getProperty("logFilePath");
			
			rearrangeManagerlog(logFilePath);
			
			mysqlIp = prop.getProperty("mysqlIp");
			mysqlPort = prop.getProperty("mysqlPort");
			mysqlId = prop.getProperty("mysqlId");
			mysqlPass = prop.getProperty("mysqlPass");
			mysqlSchema = prop.getProperty("mysqlSchema");
			
			sqlServerIp = prop.getProperty("sqlServerIp");
			sqlServerPort = prop.getProperty("sqlServerPort");
			sqlServerId = prop.getProperty("sqlServerId");
			sqlServerPass = prop.getProperty("sqlServerPass");
		} catch (IOException ex) {
			ex.printStackTrace();
		} 
		
		launch(args);
	}
	
	public static void rearrangeManagerlog(String path){
		File logFile = new File(path);
		if(logFile.exists()){
			File backupDir = new File(logFile.getParent() + "/ManagerlogBackup");
			try {
				if(!backupDir.exists()){
					backupDir.mkdirs();
				}
				FileUtils.moveFile(logFile, new File(backupDir.getPath() + "/managerlog " + dateFormat.format(new Date()) + ".log"));
				logFile.createNewFile();
			} catch (IOException e) {
				ifError = true;
				error = "Error moving existing log file";
				e.printStackTrace();
			}
		} else{
			ifError = true;
			error = "Cannot locate the log file at path " + path;
		}
	}
	
	public static void raiseErrorAlert(){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Error");
		alert.setHeaderText("Failure");
		alert.setContentText(error);
		alert.setResizable(false);
		alert.show();
		
		Optional<ButtonType> result = alert.showAndWait();
		ButtonType button = result.orElse(ButtonType.CANCEL);
		
		if (button == ButtonType.OK) {
		    System.exit(0);
		} 
		
	}
}
