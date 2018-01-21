package ru.magnit.co.tmp;
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class XlsxReader {
	private OPCPackage pk;
	private String art_code;
	private String whs_code;
	private String begin_dt;
	private String end_dt;
	private String frmt;
	private String suma_begin_dt;
	private String suma_end_dt;
	private String action_price;
	private String fix_price;
	private String fix_discount;
	private boolean title;
	
	ArrayList<String> list;
	Map<String, String> fields = new HashMap<String, String>();
	public XlsxReader(File file) throws InvalidFormatException {
		pk = OPCPackage.open(file);
		list = new ArrayList<String>();
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

                    ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, new XSSFSheetXMLHandler.SheetContentsHandler() {
                        @Override
                        public void startRow(int i) {
                        	//System.out.println("row number :"+ i);
                        	if(i == 0) {
                        		title = true;
                        	}
                        	clearVar();
                        }

                        private void clearVar() {
                        	art_code = null;
                        	whs_code = null;
                        	begin_dt = null;
                        	end_dt = null;
                        	frmt = null;
                        	suma_begin_dt = null;
                        	suma_end_dt = null;
                        	action_price = null;
                        	fix_price = null;
                        	fix_discount = null;
							
						}

						@Override
                        public void endRow(int i) {
                        	//System.out.println("row number :"+ i);
                        	if(i == 0) {
                        		title = false;
                        		//System.out.println("row number :"+ i);
                        		checkTitle();
                        	}
                        	showVar();
                        	
                        }

                        private void showVar() {
							System.out.println(art_code + " "+ whs_code + " "+begin_dt +" "+end_dt+" "+frmt+" "+ action_price);
							
						}

						@Override
                        public void cell(String s, String s1, XSSFComment xssfComment) {
                        	if(title) {
                        		parseTitle(s,s1);
                        	}

                            list.add(s1);
                            System.out.println(s1);
                            System.out.println(s);
                            parseString(s,s1);
                            
                        }

                        @Override
                        public void headerFooter(String s, boolean b, String s1) {
                            System.out.println("headerFooter "+s1);
                            System.out.println("headerFooter "+ s);
                            
                        }
                    }, true);


                    parser.setContentHandler(handler);
                    parser.parse(new InputSource(stream));


                   

        }
            } catch (SAXException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (OpenXML4JException e) {
            e.printStackTrace();
        }
	}
	
	public void parseString(String adr, String val) {
		String col = adr.replaceAll("[0-9]", "");
		System.out.println("col = "+col);
		if (col.equals(fields.get("artCode"))) {
			art_code = val;
		}
		else if (col.equals(fields.get("whsCode"))) {
			whs_code = val;
		}
		else if (col.equals(fields.get("beginDt"))) {
			begin_dt = val;
		}
		else if (col.equals(fields.get("endDt"))) {
			end_dt = val;
		}
		else if (col.equals(fields.get("frmt"))) {
			frmt = val;
		}
		else if (col.equals(fields.get("sumaBeginDt"))) {
			suma_begin_dt = val;
		}
		else if (col.equals(fields.get("sumaEndDt"))) {
			suma_end_dt = val;
		}
		else if (col.equals(fields.get("actionPrice"))) {
			action_price = val;
		}
		else if (col.equals(fields.get("fixPrice"))) {
			fix_price = val;
		}
		else if (col.equals(fields.get("fixDiscount"))) {
			fix_discount = val;
		}

		
	}
	public void parseTitle(String adr, String val) {
		String col = adr.replaceAll("[0-9]", "");
		System.out.println(val.toUpperCase());
		if (val.toUpperCase() == "КОД ТТ") {
			
			fields.put("whsCode", col);
			
		}
		else if (val.toUpperCase().equals("КОД ТП") ) {

			fields.put("artCode", col);

		}
		else if (val.toUpperCase().equals("ДАТА С")) {
			fields.put("beginDt", col);
		}
		else if (val.toUpperCase().equals("ДАТА ПО")) {
			fields.put("endDt", col);
		}
		else if (val.toUpperCase().equals("СУМА С")) {
			fields.put("sumaBeginDt", col);
		}
		else if (val.toUpperCase().equals("СУМА ПО")) {
			fields.put("sumaEndDt", col);
		}
		else if (val.toUpperCase().equals("ФОРМАТ")) {
			fields.put("frmt", col);
		}
		else if (val.toUpperCase().equals("АКЦИОННАЯ ЦЕНА")) {
			fields.put("actionPrice", col);
		}
		else if (val.toUpperCase().equals("ФИКСИРОВАННАЯ ЦЕНА")) {
			fields.put("fixPrice", col);
		}
		else if (val.toUpperCase().equals("ФИКСИРОВАННАЯ СКИДКА")) {
			fields.put("fixDiscount", col);
		}
		System.out.println(fields);
	}
	
	public void checkTitle() {

		if(!fields.containsKey("whsCode") & !fields.containsKey("frmt")) {
			throw new NoSuchElementException("Не найдено поле Код ТП или Формат");
		}
		if(!fields.containsKey("artCode")) {
			throw new NoSuchElementException("Не найдено поле Код ТП");
		}	
		if(!fields.containsKey("beginDt")) {
			throw new NoSuchElementException("Не найдено поле Дата С");
		}
		
		if(!fields.containsKey("endDt")) {
			throw new NoSuchElementException("Не найдено поле Дата ПО");
		}
		
		if(!fields.containsKey("sumaBeginDt")) {
			throw new NoSuchElementException("Не найдено поле СУМА С");
		}
		if(!fields.containsKey("sumaEndDt")) {
			throw new NoSuchElementException("Не найдено поле СУМА ПО");
		}
		
		if(!fields.containsKey("actionPrice") & !fields.containsKey("fixPrice") & !fields.containsKey("fixDiscount") ) {
			throw new NoSuchElementException("Не найдено поле Акционная цена / Фиксированная цена / Фиксированная скидка");
		}	
		
	}
}
