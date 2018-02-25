package ru.magnit.co.tmp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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



public class XLSrcData {

	ArrayList<ArrayList<HashMap<String,String>>> sheetRecords;
	ArrayList<String> sheets;
	HashMap<String,String> headers;
	HashMap<String,String> record;
	HashMap<String,String> data;
	ArrayList<HashMap<String,String>> curRecords;
	File file;
	int rowCounter;
	
	XLSrcData(){
		sheetRecords = new ArrayList<ArrayList<HashMap<String,String>>>();
		headers = new HashMap<String, String>();
		data = new HashMap<String, String>();
	}
	XLSrcData(File file) throws IOException, InvalidFormatException{
		this();
		readRecords( file, 0);
		processRecords(0);
	}
	public HashMap<String, String> getHeaders() {
		// TODO Auto-generated method stub
		return headers;
	}

	public HashMap<String,String> getData() {
		// TODO Auto-generated method stub
		return data;
	}

	public void readRecords(File file,  int skipRows) throws IOException, InvalidFormatException {
		readRecords(file.toPath(), skipRows);
	}
	public void readRecords(Path path, int skipRows) throws IOException, InvalidFormatException {
		this.file = path.toFile();
		readRecords( skipRows);
	}
	
	public void readRecords(int skipRows) throws IOException, InvalidFormatException {
		sheets = new ArrayList<String>();
		
		
		ArrayList<String> list = new ArrayList<String>();
		OPCPackage pk = OPCPackage.open(file);
		XSSFReader xssfReader = null;
		try {
			xssfReader = new XSSFReader(pk);
		    StylesTable styles = xssfReader.getStylesTable();
		    System.out.println(styles.getNumberFormats());
		    ReadOnlySharedStringsTable strings = null;
		    try {
		        strings = new ReadOnlySharedStringsTable(pk);
		        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
		        XMLReader parser = XMLReaderFactory.createXMLReader();  
		            while (iter.hasNext()) {
		            
		                    InputStream stream=iter.next();
		                    sheets.add(iter.getSheetName());
		                    System.out.println(iter.getSheetName());
		                    curRecords = new ArrayList<HashMap<String,String>>();
		                    sheetRecords.add(curRecords);
		                    ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, new XSSFSheetXMLHandler.SheetContentsHandler() {
		                        @Override
		                        public void startRow(int i) {
		                        	record = new HashMap<String, String>();
		                        }

		                        @Override
		                        public void endRow(int i) {
		                        	curRecords.add( record);
		                        	rowCounter++;
		                        	if (rowCounter>1) {
		                        		throw new RuntimeException("пора");
		                        	}
		                        	
		                        }


		                        @Override
		                        public void headerFooter(String s, boolean b, String s1) {

		                        }

								@Override
								public void cell(String arg0, String arg1, XSSFComment arg2) {
									record.put(arg0.replaceAll("\\d+", ""), arg1.toUpperCase());
									
								}
				
		                    }, true);


		                    parser.setContentHandler(handler);
		                    System.out.println("Start parse");
		                    rowCounter=0;
		                    parser.parse(new InputSource(stream));
	
		                    System.out.println("Stop parse");


		                   

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

	
	public void processRecords(int sheet) throws IOException {
		headers = new HashMap<String,String>();
		data = new HashMap<String,String>();
		int head = 0;
		int colNum;
		
	

			headers.putAll( sheetRecords.get(sheet).get(0));
			

	
			data.putAll( sheetRecords.get(sheet).get(1));

	}
	
	public String getSheetName() {
		return sheets.get(0);
	}



}
