package com.addictyou.controller;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.apache.commons.io.input.TailerListenerAdapter;

import com.addictyou.dao.AccountDetailsDao;
import com.addictyou.main.Main;
import com.addictyou.model.AccountDetails;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class OnlineTimeTracker {

    @FXML
    private Label statusId, counter, maxCount;
    
    @FXML
    private ListView<String> listView;
    
    ConcurrentHashMap<String, String> map = null;
	ObservableList<String> observableList = null;

    final Pattern loginPattern = Pattern.compile(".+Account\\[.+\\] Char\\[.+\\] Enter World Line\\[.+\\]");
    final Pattern logoutPattern = Pattern.compile(".+Account\\[.+\\] Char\\[.+\\]");
    TailerListener listener = null;
    Tailer tailer = null;
    
    AccountDetailsDao details = null;
    
    int playerCount = 0;
    int maxPlayerCount = 0;
    boolean ind = false;
    
    static boolean wasStartClicked = false;
    
    @FXML
    void onStartClick(ActionEvent event) {
    	wasStartClicked = true;

    	statusId.setText("Online");
    	statusId.setStyle("-fx-text-fill: green; -fx-font-weight: bold");
    	
    	map = new ConcurrentHashMap<>();
    	observableList = FXCollections.observableArrayList(map.values());
    	listView.setItems(observableList);
    	
    	details = new AccountDetailsDao();
    	details.establishConnection();
    	
    	listener = new MyListener();
    	tailer = new Tailer(new File(Main.logFilePath), listener, 500, false);   
        //tailer.run();
        new Thread(tailer).start();
    }

    @FXML
    void onStopClick(ActionEvent event) {
    	statusId.setText("Offline");
    	statusId.setStyle("-fx-text-fill: red; -fx-font-weight: bold");
    	
    	tailer.stop();
    	
    	if(wasStartClicked == true){
    		details.charOfflineAndCloseConnection();
    	}
    }
    
	public class MyListener extends TailerListenerAdapter {

		@Override
		public void handle(String line) {
			try{
				
				boolean login = loginPattern.matcher(line).find();
				boolean logout = logoutPattern.matcher(line).find();
				
				if (login || logout) {
					String[] temp = line.split("\\[");
					
					int accountId = Integer.parseInt(temp[1].split("\\]")[0]);
					int roleId = Integer.parseInt(temp[2].split("\\]")[0]);
					
					AccountDetails account = details.getAccountDetails(roleId, accountId);
					
					String charDetails = "RID: " + roleId + " AID: " + accountId + " Name: " + account.getCharacterName().trim();
					if(login){
						playerCount++;
						//map.put(Integer.toString(roleId), charDetails);
						details.updatePlayerStatus(roleId, accountId, 1);
						ind = true;
					} else{
						playerCount--;
						//map.remove(Integer.toString(roleId));
						details.updatePlayerStatus(roleId, accountId, 0);
						ind = false;
					}
					
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							counter.textProperty().bind(Bindings.format("%d", playerCount));
							if(ind){
								observableList.add(charDetails);
							} else{
								observableList.remove(charDetails);
							}
							
							//observableList.addAll(map.values());
							if(playerCount > maxPlayerCount){
								maxPlayerCount = playerCount;
							}
							maxCount.textProperty().bind(Bindings.format("%d", maxPlayerCount));
						}
					});
				} 
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
}
