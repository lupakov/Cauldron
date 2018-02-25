package ru.magnit.co.tmp;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.regex.Pattern;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class Template {

	private int id;
	private String name;
	private File file;
	private String tempTable;
	private XLSrcData src;
	private DestTable dstTable;

	public Template(File file, DestConnnection con) throws InvalidFormatException, IOException, SQLException {
		this.file = file;
		this.name = file.getName();
		src = new XLSrcData(this.file);
		tempTable = "pricing_sale._ovt_cauldron_temporary";
		dstTable = new DestTable();
		dstTable.setConnection(con);
		dstTable.setTableName(tempTable);
		dstTable.setFieldNames(getField((getTypeTemplate())).get(0));
		dstTable.setFieldTypes(getField((getTypeTemplate())).get(1));
		
		dstTable.setIndexes(getIndexes(getTypeTemplate()));
		dstTable.setRewriteType('w');
		SAXSrcHandler handler = new SAXSrcHandler(file, src.getSheetName(), 1, getTransformFilter(getTypeTemplate( )));
		dstTable.loadSAXData(handler, ',', "M/d/yy", "M/d/yy HH:mm");
		
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
	public int getTypeTemplate( ) {
		HashMap<String,String> header = src.getHeaders();
		HashMap<String,String> data = src.getData();
		if(Pattern.compile(Pattern.quote("ëèñòîâêà"),Pattern.CASE_INSENSITIVE).matcher(file.getName()).find()) {
			return 3;
		}
		if(Pattern.compile(Pattern.quote("ıêñïîğò"),Pattern.CASE_INSENSITIVE).matcher(file.getName()).find()) {
			return 4;
		}
		if(header.containsValue("ÀÊÖÈÎÍÍÀß ÖÅÍÀ")) {
			return 0;
		}
		if(header.containsValue("ÑÍÈÆÅÍÈÅ Â %")) {
			return 2;
			
		}
		if(header.containsValue("ÔÈÊÑÈĞÎÂÀÍÍÀß ÖÅÍÀ")) {
			if(header.containsValue("ÔÎĞÌÀÒ")) {
				if(data.get(getKeyByValue(header, "ÔÎĞÌÀÒ")).equals("ÃÌ")) {
					return 11;
				}
				else {
					return 12;
				}
			}
			else {
				return 11;
			}
		}
		throw new RuntimeException("Íå óäàëîñü îïğåäåëèòü òèï øàáëîíà. Ôàéë: " + file.getName() +
				" Çàãîëîâêè: " + header.toString()+ " Äàííûå: " + data.toString());
	}
	public static String getKeyByValue(Map<String, String> map, String value) {
		for(Entry<String,String> entry :map.entrySet()) {
			if(Objects.equals(value, entry.getValue())) {
				return entry.getKey();
			}
			
		}
		return null;
	}
	
	public String[] getTransformFilter(int typeTemplate) {
		ArrayList<String> filtr = new ArrayList<String>();
		String[] fields = getHeadersByType(typeTemplate);
		for (String s : fields) {
			filtr.add(getKeyByValue(src.getHeaders(),s));
		}
		return filtr.toArray(new String[0]);
		
	}
	public String[] getHeadersByType(int typeTemplate) {
		switch(typeTemplate) {
		case 0: return new String[]{"ÊÎÄ ÌÕ","ÊÎÄ ÒÏ","ÄÀÒÀ Ñ","ÄÀÒÀ ÏÎ","ÀÊÖÈÎÍÍÀß ÖÅÍÀ","ÑÓÌÀ Ñ", "ÑÓÌÀ ÏÎ"};
		case 11:return new String[]{"ÊÎÄ ÌÕ","ÊÎÄ ÒÏ","ÄÀÒÀ Ñ","ÄÀÒÀ ÏÎ","ÔÈÊÑÈĞÎÂÀÍÍÀß ÖÅÍÀ","ÑÓÌÀ Ñ", "ÑÓÌÀ ÏÎ"};
		case 12:return new String[]{"ÔÎĞÌÀÒ","ÊÎÄ ÒÏ","ÄÀÒÀ Ñ","ÄÀÒÀ ÏÎ","ÔÈÊÑÈĞÎÂÀÍÍÀß ÖÅÍÀ","ÑÓÌÀ Ñ", "ÑÓÌÀ ÏÎ"};
		case 2:return new String[]{"ÊÎÄ ÌÕ","ÊÎÄ ÒÏ","ÄÀÒÀ Ñ","ÄÀÒÀ ÏÎ","ÑÍÈÆÅÍÈÅ Â %","ÑÓÌÀ Ñ", "ÑÓÌÀ ÏÎ"};
		case 3:return new String[]{"ÊÎÄ ÌÕ","ÊÎÄ ÒÏ","ÄÀÒÀ Ñ","ÄÀÒÀ ÏÎ","ÀÊÖÈÎÍÍÀß ÖÅÍÀ"};
		case 4:return new String[]{"ÊÎÄ ÌÕ","ÊÎÄ ÒÏ","ÄÀÒÀ Ñ","ÄÀÒÀ ÏÎ","ÀÊÖÈÎÍÍÀß ÖÅÍÀ"};
		}
		
		return new String[] {""};
	}
	
	public ArrayList<ArrayList<String>> getField(int typeTemplate) {
		ArrayList<ArrayList<String>> res = new ArrayList<ArrayList<String>>();
		ArrayList<String> tmpNames= null;
		ArrayList<String> tmpTypes = null;
	
		switch(typeTemplate) {
		case 0: 
			tmpNames = new ArrayList<String>(Arrays.asList((new String[] {"whs_code","art_code","begin_dt","end_dt", "price", "suma_begin_dt", "suma_end_dt"})));
			 tmpTypes = new ArrayList<String>(Arrays.asList((new String[] {"varchar(20)","varchar(20)","date","date", "float", "date", "date"})));
			break;
		case 11: 
			tmpNames = new ArrayList<String>(Arrays.asList((new String[] {"whs_code","art_code","begin_dt","end_dt", "price", "suma_begin_dt", "suma_end_dt"})));
			tmpTypes = new ArrayList<String>(Arrays.asList((new String[] {"varchar(20)","varchar(20)","date","date", "float", "date", "date"})));
			break;
			
		case 12:
			tmpNames = new ArrayList<String>(Arrays.asList((new String[] {"frmt","art_code","begin_dt","end_dt", "price", "suma_begin_dt", "suma_end_dt"})));
			tmpTypes = new ArrayList<String>(Arrays.asList((new String[] {"varchar(20)","varchar(20)","date","date", "float", "date", "date"})));
			break;
			
		case 2:
			tmpNames = new ArrayList<String>(Arrays.asList((new String[] {"whs_code","art_code","begin_dt","end_dt", "price", "suma_begin_dt", "suma_end_dt"})));
			tmpTypes = new ArrayList<String>(Arrays.asList((new String[] {"varchar(20)","varchar(20)","date","date", "float", "date", "date"})));
			break;
		case 3:
			tmpNames = new ArrayList<String>(Arrays.asList((new String[] {"whs_code","art_code","begin_dt","end_dt", "price"})));
			tmpTypes = new ArrayList<String>(Arrays.asList((new String[] {"varchar(20)","varchar(20)","date","date", "float"})));
			break;			
		case 4:
			tmpNames = new ArrayList<String>(Arrays.asList((new String[] {"whs_code","art_code","begin_dt","end_dt", "price"})));
			tmpTypes = new ArrayList<String>(Arrays.asList((new String[] {"varchar(20)","varchar(20)","date","date", "float"})));
			break;	
		}
		res.add(tmpNames);
		res.add(tmpTypes);
		return res;
	}
	public ArrayList<Boolean> getIndexes(int typeTemplate){
		ArrayList<Boolean> tmp7 = new ArrayList<Boolean>()  ;
		tmp7.add(true);
		tmp7.add(true);
		tmp7.add(false);
		tmp7.add(false);
		tmp7.add(false);
		tmp7.add(false);
		tmp7.add(false);
		
		ArrayList<Boolean> tmp5 = new ArrayList<Boolean>()  ;
		tmp5.add(true);
		tmp5.add(true);
		tmp5.add(false);
		tmp5.add(false);
		tmp5.add(false);
		tmp5.add(false);
		tmp5.add(false);
		
		switch (typeTemplate) {
		case 0:
		case 11:
		case 12:
		case 2:
			return tmp7;
		case 3:
		case 4:
			return tmp5;
		}
		return tmp5;
		
	}
	public void moveToGood() {
		
	}
	public boolean checkTmpTable(Connection con) {
		
	}
	
	
	
}
