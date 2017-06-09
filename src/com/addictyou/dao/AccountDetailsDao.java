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
	Connection connection = null;
	PreparedStatement pstmt = null;
	
	public void establishConnection(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("MySQL driver not found.");
			e.printStackTrace();
		}

		try {
			connection = DriverManager.getConnection("jdbc:mysql://" + Main.ip + ":" + Main.port + "/shengui",Main.id, Main.pass);
		} catch (SQLException e) {
			System.out.println("Connection Failed.");
			e.printStackTrace();
		}

		if (connection == null) {
			System.out.println("Failed to establish connection.");
		}
		
		try {
			pstmt = connection.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public AccountDetails getAccountDetails(int roleId, int accountId){
		AccountDetails a = new AccountDetails();
		try {
			pstmt.setInt(1, roleId);
			pstmt.setInt(2, accountId);
			
			ResultSet rs = pstmt.executeQuery();
			
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
}
