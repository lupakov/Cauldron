package ru.magnit.co.tmp;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class Program {

	public static void main(String[] args) throws InvalidFormatException, SQLException, IOException {
		// TODO Auto-generated method stub
		DestConnnection.writeProperties();
		String folderPath = "F:\\couldron";
		Cauldron cauldron = new Cauldron(folderPath);
		cauldron.cook();
		//cauldron.prepare();
		//File l = new File("F:\\couldron\\test.xlsx");
		//XlsxReader r = new XlsxReader(l);
		//Template tmpl = new Template(l);
		
		//System.out.println(tmpl.getTypeTemplate());
		//for (String s : tmpl.getTransformFilter(tmpl.getTypeTemplate())) {
		//	System.out.print(s);
		//}
		
		
		
	}

}
