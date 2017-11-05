package ru.magnit.co.tmp;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;

public class Cauldron {
	private String path;
	private ArrayList<Template> list;
	private ArrayList<String> curretnTemplate;
	private ArrayList<String> fileNames;
	public Cauldron(String path) {
		this.setPath(path);
		fileNames = new ArrayList<String>();
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public void readTemplates() {
		
	}
	public void cook() {
		
	}
	public void prepare () {
		final File folder = new File(path);
		listFilesForFolder(folder);
	}
	public void listFilesForFolder(final File folder) {
		for (final File fileEntry : folder.listFiles()) {
			if (!fileEntry.isDirectory()) {
				fileNames.add(fileEntry.getName());
				System.out.println(fileEntry.getName());
			}
		}
	}
	public ArrayList<String> getCurrentTemplat(Connection con) throws SQLException{
		ArrayList<String> list = new ArrayList<String>();
		Statement stmt = null;
		stmt = con.createStatement();
		String sql;
		sql = "select template_name from pricing_sale.moinitoring_io_template";
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()) {
			list.add(rs.getString(0));
		}
		return list;
	}
	public boolean checkCurrent(String templateName) {
		return curretnTemplate.contains(templateName);
	}
}
