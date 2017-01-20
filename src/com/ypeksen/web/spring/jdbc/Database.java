package com.ypeksen.web.spring.jdbc;

import javax.sql.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.*;
import com.mysql.cj.jdbc.MysqlDataSource;

public class Database {

	private MysqlDataSource datasource;
	public Database(String url, String user, String password){
		
		datasource = new MysqlDataSource();
		datasource.setURL("jdbc:mysql://" + url);
		datasource.setUser(user);
		datasource.setPassword(password);
		//datasource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/TestDB");
		//Connection connection = datasource.getConnection();
	}
	
	
	public void sendQuery() throws SQLException{
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    if(datasource == null){
    	return;
    }

  	try {
			con = datasource.getConnection();
			pst = con.prepareStatement("SELECT * FROM hastane");
			  rs = pst.executeQuery();

			  while (rs.next()) {

			      System.out.println(rs.getString(1));
			  }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(con != null)
				con.close();
		}
  	
	}
}
