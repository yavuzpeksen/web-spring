package com.ypeksen.web.spring.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

import com.ypeksen.web.spring.controllers.UserRegistration;
import com.ypeksen.web.spring.jdbc.mapper.IObjectMapper;



public class TripAdvisorReportObjectMapper implements IObjectMapper<List<UserRegistration>>{
	
	private final static Logger LOG = LoggerFactory.getLogger(TripAdvisorReportObjectMapper.class);
	private String schemaName = "TICKET";
	private String sqlObjectTypeName = schemaName + ".TYPE_TADVISOR_PERF_REPORT";
	private String sqlArrayTypeName = schemaName + ".TABLE_TADVISOR_PERF_REPORT";

	@Override
	public void mapParameter(JdbcTemplate jdbcTemplate, Connection connection, CallableStatement call, int curNo, List<UserRegistration> list) throws SQLException {

		Connection nativeConnection = null;

		NativeJdbcExtractor nativeJdbcExtractor = jdbcTemplate.getNativeJdbcExtractor();
		nativeConnection = nativeJdbcExtractor.getNativeConnection(connection);
	
		StructDescriptor strcutd = new StructDescriptor(sqlObjectTypeName, nativeConnection);
		ArrayDescriptor intDesc = new ArrayDescriptor(sqlArrayTypeName, nativeConnection);
		STRUCT structs[] = null;
	
		int column = strcutd.getMetaData().getColumnCount();

		if (list != null) {
			int size = list.size();
			structs = new STRUCT[size];
				for (int j = 0; j < size; j++) {
					UserRegistration model = list.get(j);
					Object attributes[] = new Object[column];
					structs[j] = new STRUCT(strcutd, nativeConnection, attributes);
				}
			}

		ARRAY list1 = new ARRAY(intDesc, nativeConnection, structs);
		try{
			call.setArray(curNo, list1);
		} catch (SQLException sqle) {
			LOG.error("::mapParameter object:");
			throw sqle;
		}
	}
}

