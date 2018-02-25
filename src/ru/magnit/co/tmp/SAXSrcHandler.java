package ru.magnit.co.tmp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class SAXSrcHandler {
	private String sheetName;
	private File file;
	private int skipRows;
	private SAXUploadEngine eng;
	private HashMap<String,String> record;
	private String[] filter;
	SAXSrcHandler(){
		
	}
	SAXSrcHandler(File file, String sheetName, int skipRows, String[] filter){
		this.file = file;
		this.setSheetName(sheetName);
		this.skipRows = skipRows;
		this.filter = filter;
	}


	public void processRecords(File file, String sheetName,  int skipRows) throws IOException, InvalidFormatException, SQLException {
		this.setSheetName(sheetName);
		this.setSkipRows(skipRows);
		this.file = file;
		processRecords();
	}

	
	public void processRecords() throws IOException, InvalidFormatException, SQLException {

		
		
		ArrayList<String> list = new ArrayList<String>();
		OPCPackage pk = OPCPackage.open(file);
		XSSFReader xssfReader = null;
		try {
			xssfReader = new XSSFReader(pk);
		    StylesTable styles = xssfReader.getStylesTable();
		    ReadOnlySharedStringsTable strings = null;
		    try {
		        strings = new ReadOnlySharedStringsTable(pk);
		        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
		        XMLReader parser = XMLReaderFactory.createXMLReader();  
		            while (iter.hasNext()) {
		            
		                    InputStream stream=iter.next();
		                    if (sheetName.toUpperCase().equals(iter.getSheetName().toUpperCase())){
		                    System.out.println(iter.getSheetName());


		                    ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, new XSSFSheetXMLHandler.SheetContentsHandler() {
		                        @Override
		                        public void startRow(int i) {
		                        	record = new HashMap<String,String>();
		                        }

		                        @Override
		                        public void endRow(int i) {
		                        	if (i >= skipRows) {
		                        		System.out.println("try "+i);
			                        	try {
			                        		for (String s : filter) {
			                        			System.out.println(s);
			                        		}
			                        		System.out.println(filter);
			                        	
			                        		System.out.println(record);
			                        		
			                        		for (String s:getRecordByFilter(record,filter)) {
			                        			System.out.println(s);
			                        		}
			                        		System.out.println( getRecordByFilter(record,filter));
											eng.addRecord( getRecordByFilter(record,filter));
										} catch (SQLException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
		                        	}
		                        }


		                        @Override
		                        public void headerFooter(String s, boolean b, String s1) {

		                        }

								@Override
								public void cell(String arg0, String arg1, XSSFComment arg2) {
									record.put(arg0.replaceAll("\\d+",""), arg1);
									
								}
				
		                    }, true);


		                    parser.setContentHandler(handler);
		                    System.out.println("we begin parseeeeee");
		                    parser.parse(new InputSource(stream));
		                    System.out.println("we begin try finish");
		                    eng.finishLoad();
		                    System.out.println("we end try finish");

		                    }

		                   

		        }
		            } catch (SAXException e) {
		                e.printStackTrace();
		            }

		        } catch (IOException e) {
		            e.printStackTrace();
		        } catch (OpenXML4JException e) {
		            e.printStackTrace();
		        } catch (RuntimeException e) {
		        	
		        }


		    }
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public int getSkipRows() {
		return skipRows;
	}
	public void setSkipRows(int skipRows) {
		this.skipRows = skipRows;
	}
	public SAXUploadEngine getEng() {
		return eng;
	}
	public void setEng(SAXUploadEngine eng) {
		this.eng = eng;
	}
	public String[] getRecordByFilter(HashMap<String, String> map, String[] filter) {
		ArrayList<String> temp = new ArrayList<String>();
		for(String s : filter) {
			try {
			temp.add(map.get(s));
			}
			catch (Exception e){
				temp.add(null);
			}
		}
		return temp.toArray(new String[0]);
	}

}
