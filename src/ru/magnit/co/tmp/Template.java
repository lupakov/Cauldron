package ru.magnit.co.tmp;

import java.io.File;
import java.util.ArrayList;

public class Template {
	private ArrayList<Action> list;
	private int id;
	private String name;
	private File file;
	public Template(File file) {
		this.file = file;
		this.name = file.getName();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void fillData() {
		
	}
	
}
