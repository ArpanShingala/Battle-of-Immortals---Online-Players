package com.addictyou.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.addictyou.main.Main;
import com.addictyou.model.AccountDetails;

public class AccountDetailsDao {
	
	private String sql = "SELECT Name FROM basetab_sg WHERE RoleID = ? and AccountID = ?";
	private String sql1 = "IF NOT EXISTS (SELECT * FROM PassportBOI.dbo.onlinePlayers WHERE rid = ? AND aid = ?) INSERT INTO PassportBOI.dbo.onlinePlayers (rid, aid, ind) VALUES (?,?,?)";
	private String sql2 = "UPDATE PassportBOI.dbo.onlinePlayers SET ind = ?";
	
	Connection mysqlCon = null;
	Connection sqlServerCon = null;
	
	PreparedStatement pstmtGetAccountData = null;
	PreparedStatement pstmtUpdatePlayerStatus = null;
	PreparedStatement pstmtCharsOffline = null;
	
	public void establishConnection(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("MySQL/SQL server driver not found.");
			e.printStackTrace();
		}

		try {
			mysqlCon = DriverManager.getConnection("jdbc:mysql://" + Main.mysqlIp + ":" + Main.mysqlPort + "/" + Main.mysqlSchema,Main.mysqlId, Main.mysqlPass);
			sqlServerCon = DriverManager.getConnection("jdbc:sqlserver://" + Main.sqlServerIp + ":" + Main.sqlServerPort + ";databaseName=" + Main.sqlServerSchema + ";user=" + Main.sqlServerId + ";password=" + Main.sqlServerPass);
		} catch (SQLException e) {
			System.out.println("Connection Failed.");
			e.printStackTrace();
		}

		if (mysqlCon == null) {
			System.out.println("Failed to establish MySQL connection.");
		}
		
		if (sqlServerCon == null) {
			System.out.println("Failed to establish SQL Server connection.");
		}
		
		try {
			pstmtGetAccountData = mysqlCon.prepareStatement(sql);
			pstmtUpdatePlayerStatus = sqlServerCon.prepareStatement(sql1);
			pstmtCharsOffline = sqlServerCon.prepareStatement(sql2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public AccountDetails getAccountDetails(int roleId, int accountId){
		AccountDetails a = new AccountDetails();
		try {
			pstmtGetAccountData.setInt(1, roleId);
			pstmtGetAccountData.setInt(2, accountId);
			
			ResultSet rs = pstmtGetAccountData.executeQuery();
			
			if(rs.next()){
				a.setAccountId(Integer.toString(accountId));
				a.setRoleId(Integer.toString(roleId));
				a.setCharacterName(rs.getString("Name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return a;
	}
	
	public void updatePlayerStatus(int roleId, int accountId, int status){
		try {
			pstmtUpdatePlayerStatus.setInt(1, roleId);
			pstmtUpdatePlayerStatus.setInt(2, accountId);
			pstmtUpdatePlayerStatus.setInt(3, roleId);
			pstmtUpdatePlayerStatus.setInt(4, accountId);
			pstmtUpdatePlayerStatus.setInt(5, status);
			
			pstmtUpdatePlayerStatus.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void charOfflineAndCloseConnection(){
		try {
			pstmtCharsOffline.setInt(1, 0);
			pstmtCharsOffline.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			mysqlCon.close();
			sqlServerCon.close();
		} catch (SQLException e) {
			// do nothing
		}
	}
}
