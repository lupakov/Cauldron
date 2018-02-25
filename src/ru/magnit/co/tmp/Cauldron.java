package ru.magnit.co.tmp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class Cauldron {
	private Path path;
	private ArrayList<String> messageList;
	private Path goodDirectory;
	private Path badDirectory;
	private ArrayList<File> templates;
	private DestConnnection connection;
	public Cauldron(Path path, Path goodDirectory, Path badDirectory) throws FileNotFoundException, IOException {
		this.setPath(path);
		this.setGoodDirectory(goodDirectory);
		this.setBadDirectory(badDirectory);
		this.templates =  this.getListFiles();
		connection = new DestConnnection();
		messageList = new ArrayList<String>();
	}
	public Cauldron(String path) throws FileNotFoundException, IOException {
		this(Paths.get(path),Paths.get(path,"Good"), Paths.get(path, "Bad"));
	}

	public Path getPath() {
		return path;
	}
	public void setPath(Path path) {
		this.path = path;
	}
	public void setPath(String path) {
		this.path = Paths.get(path);
	}
	public void cook() throws SQLException, IOException, InvalidFormatException {
		try(Connection con = connection.getConnection()){
			ArrayList<String> curTemplates = getCurrentTemplate(con);
			for (final File f : this.templates) {
				if(curTemplates.contains(f.getName())) {
					moveToBad(f.getPath());
					messageList.add("Ошибка: Шаблон "+f.getName()+" уже добавлен");
					continue;
				}
				try {
				Template t = new Template(f, connection);
				
				}
				catch (Exception e){
					messageList.add(e.getMessage());
					
				}
			}
		}
	}

	public ArrayList<File> getListFiles() {
		ArrayList<File> fileList = new ArrayList<File>();
		for (final File fileEntry : this.path.toFile().listFiles()) {
			if (!fileEntry.isDirectory() & fileEntry.getName().charAt(0)!= '~') {
				fileList.add(fileEntry);
				System.out.println(fileEntry.getName());
			}
		}
		return fileList;
	}
	
	public ArrayList<String> getCurrentTemplate(Connection con) throws SQLException{
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

	
	public void moveToBad(String fileName) throws IOException {
		if (Files.notExists(FileSystems.getDefault().getPath(this.getPath() + File.separator + "Bad"))){
			Files.createDirectories(Paths.get(this.getPath() + File.separator + "Bad"));
		}
		Files.move(FileSystems.getDefault().getPath(this.getPath() + File.separator + fileName), FileSystems.getDefault().getPath(this.getPath() + File.separator+ "Bad" + File.separator + fileName), StandardCopyOption.REPLACE_EXISTING);
	}
	public Path getGoodDirectory() {
		return goodDirectory;
	}
	public void setGoodDirectory(Path goodDirectory) {
		this.goodDirectory = goodDirectory;
	}
	public void setGoodDirectory(String goodDirectory) {
		this.goodDirectory = Paths.get(goodDirectory);
	}
	public Path getBadDirectory() {
		return badDirectory;
	}
	public void setBadDirectory(Path badDirectory) {
		this.badDirectory = badDirectory;	
	}
	public void setBadDirectory(String badDirectory) {
		this.badDirectory = Paths.get(badDirectory);  
	}
}
