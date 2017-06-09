package com.addictyou.main;
	
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	public static String logFilePath = "";
	public static String ip = "";
	public static String port = "";
	public static String id = "";
	public static String pass = "";
	
	@Override
	public void start(Stage primaryStage) {
		try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/addictyou/main/MyScene.fxml"));
            primaryStage.setTitle("Battle of Immortals - Online Players Tracking");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
	}
	
	public static void main(String[] args) {
		String absolutePath = new File(".").getAbsolutePath();
		
		Properties prop = new Properties();
		try (InputStream input = new FileInputStream(absolutePath + "/config.properties")){
			prop.load(input);
			logFilePath = prop.getProperty("logFilePath");
			ip = prop.getProperty("dbIp");
			port = prop.getProperty("dbPort");
			id = prop.getProperty("dbId");
			pass = prop.getProperty("dbPass");
		} catch (IOException ex) {
			ex.printStackTrace();
		} 
		
		launch(args);
	}
}
