package ru.magnit.co.tmp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class DestTable {
	private String tableName;


	private char rewriteType;
	private DestConnnection connection;
	private ArrayList<String> fieldNames;
	private ArrayList<String> fieldTypes;
	private ArrayList<Boolean> indexes;

	
	public DestTable() {
		
	}
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public char getRewriteType() {
		return rewriteType;
	}

	public void setRewriteType(char rewriteType) {
		this.rewriteType = rewriteType;
	}

	public DestConnnection getConnection() {
		return connection;
	}

	public void setConnection(DestConnnection connection) {
		this.connection = connection;
	}

	public ArrayList<String> getFieldNames() {
		return fieldNames;
	}

	public void setFieldNames(ArrayList<String> fieldNames) {
		this.fieldNames = fieldNames;
	}

	public ArrayList<String> getFieldTypes() {
		return fieldTypes;
	}

	public void setFieldTypes(ArrayList<String> fieldTypes) {
		this.fieldTypes = fieldTypes;
	}

	public ArrayList<Boolean> getIndexes() {
		return indexes;
	}

	public void setIndexes(ArrayList<Boolean> indexes) {
		this.indexes = indexes;
	}

	public void createTable() throws SQLException {
		String sDropTbl = "DROP TABLE " + tableName;
		String sCreateTbl = getCreateQuery();
		if (rewriteType == 'w') {
			try(Connection con = connection.getConnection()){
			//DatabaseMetaData meta = con.getMetaData();
			//ResultSet rs = meta.getTables(null, null, tableName, null);
			//while(rs.next()) {
			//	Statement st = con.createStatement();
			//	st.execute("drop table "+ tableName);
			//}
				try (Statement stmt = con.createStatement()){
					try {
						stmt.executeUpdate(sDropTbl);
					}
					catch(SQLException ex) {
						System.out.println("Drop table exception ignored: " + ex);
					}
					System.out.println(sCreateTbl);
					stmt.executeUpdate(sCreateTbl);
				} 
			}
			
		}
		
	}

	
	public void loadSAXData(SAXSrcHandler handler,char digSep, String dFrmt, String tFrmt ) throws SQLException, InvalidFormatException, IOException {
		createTable();
		Connection con = connection.getConnection();
		SAXUploadEngine eng = new SAXUploadEngine(tableName, fieldTypesToChar(), con);
		eng.setDateFormat(dFrmt);
		eng.setTimestampFormatter(tFrmt);
		eng.setDigitSeparator(digSep);
		handler.setEng(eng);
		handler.processRecords();
	}
	

	public String getCreateQuery() {
		StringBuilder sb = new StringBuilder();
		StringBuilder ib = new StringBuilder();
		int fieldCount = fieldNames.size();
		int indexCount = 0;
		sb.append("create multiset table ");
		sb.append(tableName);
		sb.append(" (");
		for (int i = 0 ; i<fieldCount; i++) {
			sb.append(fieldNames.get(i));
			sb.append(" ");
			sb.append(fieldTypes.get(i));
			if (i!=fieldCount-1) {
				sb.append(", ");
				
			}
			if(indexes.get(i)) {
				if(indexCount != 0) {
					ib.append(", ");
				
				}
				ib.append(fieldNames.get(i));
				indexCount++;
			}
		}
		sb.append(") ");
		if (ib.toString().length()>0) {
			sb.append(" primary index ( ");
			sb.append(ib.toString());
			sb.append(" )");
		}
		return sb.toString();
		
		
	}
	
	public char[] fieldTypesToChar() {
		char[] ft = new char[fieldTypes.size()];
		for (int i = 0; i < fieldTypes.size(); i++) {
			if(fieldTypes.get(i).toUpperCase().equals("INTEGER")) {
				ft[i]='i';	
			}else if(fieldTypes.get(i).toUpperCase().equals("FLOAT")) {
				ft[i]='f';
			}else if(fieldTypes.get(i).toUpperCase().equals("DATE")) {
				ft[i]='d';
			}else if(fieldTypes.get(i).toUpperCase().equals("TIMESTAMP")) {
				ft[i]='t';
			}else  {
				ft[i]='s';
			}
			
		}
		System.out.println(ft);
		return ft;
	}
}
