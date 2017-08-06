package com.model2.mvc.common;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class ListTypeHandler extends BaseTypeHandler<List<String>> {

	public ListTypeHandler() {
		// TODO Auto-generated constructor stub
	}

	/*@Override
	public Object getResult(ResultSet rs, String columnIndex) throws SQLException {
		return rs.getString(columnIndex);
	}

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		return rs.getString(columnIndex);
	}

	@Override
	public Object getResult(CallableStatement cStmt, int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return cStmt.getObject(parameterIndex);
	}

	@Override
	public void setParameter(PreparedStatement pStmt, int i, Object parameter, JdbcType jdbcType) throws SQLException {
		
		//fileName의 파라미터를 List로 casting
		List<String> objects = (List<String>)parameter;
		String str = "";
		for(String obj : objects) {
			str += obj;
		}
		pStmt.setObject(i, str, jdbcType.TYPE_CODE);
		
	}*/

	@Override
	public List<String> getNullableResult(ResultSet rs, String columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		String str = rs.getString(columnIndex);
		System.out.println(str);
		
		if(str != null) {
		List<String> files = new ArrayList<String>();
		String[] strArr = str.split(",");
		for(String file : strArr) {
			files.add(file);
		}
		System.out.println(files);
		
		return files;
		} else {
			List<String> files = new ArrayList<String>();
			files.add("");
			return files;
		}
	}

	@Override
	public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		String str = rs.getString(columnIndex);
		
		if(str != null) {
			List<String> files = new ArrayList<String>();
			String[] strArr = str.split(",");
			for(String file : strArr) {
				files.add(file);
			}
			System.out.println(files);
			
			return files;
			} else {
				List<String> files = new ArrayList<String>();
				files.add("");
				return files;
			}
	}

	@Override
	public List<String> getNullableResult(CallableStatement cStmt, int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		String str = cStmt.getString(parameterIndex);
		
		if(str != null) {
			List<String> files = new ArrayList<String>();
			String[] strArr = str.split(",");
			for(String file : strArr) {
				files.add(file);
			}
			System.out.println(files);
			
			return files;
			} else {
				List<String> files = new ArrayList<String>();
				files.add("");
				return files;
			}
	}


	@Override
	public void setNonNullParameter(PreparedStatement pStmt, int i, List<String> parameter, JdbcType jdbcType)
			throws SQLException {
		// TODO Auto-generated method stub
		//fileName의 파라미터를 List로 casting
		StringBuffer buffer = new StringBuffer(parameter.toString());
		
				pStmt.setString(i, buffer.substring(1, buffer.length()-1));
	}


}
