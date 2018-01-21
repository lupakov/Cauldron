package ru.magnit.co.tmp;

import java.io.File;
import java.sql.SQLException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class Program {

	public static void main(String[] args) throws InvalidFormatException, SQLException {
		// TODO Auto-generated method stub
		String folderPath = "F:\\couldron";
		Cauldron cauldron = new Cauldron(folderPath);
		//cauldron.prepare();
		File l = new File("F:\\couldron\\test.xlsx");
		XlsxReader r = new XlsxReader(l);
		
	}

}
