package ru.magnit.co.tmp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DestConnnection {
	private String url;
	private String login;
	private String password;
	
	public DestConnnection() throws FileNotFoundException, IOException {
		readProperties();
	}
	
	public Connection getConnection() {
		Connection con = null;
		try {
			 con = DriverManager.getConnection(url,login, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
		
	}
	public Connection getFastConnection() {
		Connection con = null;
		try {
			 con = DriverManager.getConnection(url+",TYPE=FASTLOAD",login, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
		
	}
	public static void writeProperties() throws FileNotFoundException, IOException {
		Properties prop = new Properties();

		try (FileOutputStream output = new FileOutputStream("config.properties")){
			prop.setProperty("url", "jdbc:teradata://192.168.0.105/CHARSET=UTF16,DATABASE=PRICING_SALE,TMODE=ANSI");
			prop.setProperty("login","dbc");
			prop.setProperty("password", "dbc");
			prop.store(output, null);
		}
		
	}
	public void readProperties() throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		try(FileInputStream input = new FileInputStream("config.properties"))
		{
			prop.load(input);
			url= prop.getProperty("url");
			login = prop.getProperty("login");
			password = prop.getProperty("password");
		}

	}

}
