package com.ypeksen.web.spring.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import oracle.jdbc.OracleTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.google.gson.Gson;
import com.ypeksen.web.spring.jdbc.mapper.IObjectMapper;
import com.ypeksen.web.spring.utils.ObjectUtils;

@SuppressWarnings("unchecked")
public class OracleProcedureExecutor implements IProcedureExecutor, InitializingBean {

	private static final int MAX_OUTPUT_SIZE = 5;
	private Logger LOG = LoggerFactory.getLogger(this.getClass());
	private Marker LOG_FATAL = MarkerFactory.getMarker("FATAL");
	private JdbcTemplate jdbcTemplate;
	private String schemaName;
	private Map<String, IObjectMapper> objmapper;
	private Gson gson;
	private int prefetchSize = 0;
	private boolean enableJdbcMetrics = true;

@Override
public void afterPropertiesSet() throws Exception {

}

@Override
public Object execProc(ProcedureDefinition definition, Object[] args) throws SQLException {

	try {
		return execProcInternal(definition, args);
	
	}finally{
	
	}
}

private Object execProcInternal(ProcedureDefinition definition, Object[] args) throws SQLException {

	checkDefinition(definition, args);
	
	Connection connection = null;
	CallableStatement call = null;
	
	int curNo = 0;
	
	long time1 = System.currentTimeMillis();
	
	try {
		connection = jdbcTemplate.getDataSource().getConnection();
		call = connection.prepareCall(definition.getCompiledSQL());
	}catch (SQLException sqle){
		close(call, connection);
		throw sqle;
	}

	long time2 = System.currentTimeMillis();
	
	long time3 = 0L;
	long time4 = 0L;
	long time5 = 0L;
	
	try{
		curNo = prepareCall(definition, connection, call, jdbcTemplate, args);
		time3 = System.currentTimeMillis();
	
		int count = 0;
	
	try{
		count = call.executeUpdate();
	}catch (SQLException sqle){
		int errorCode = sqle.getErrorCode();
	
	if(errorCode == 4068)
		count = call.executeUpdate();
	else{
		throw sqle;
	}
	}
	
		time4 = System.currentTimeMillis();
	
		Object result = processResult(definition, call, curNo, count);
	
		time5 = System.currentTimeMillis();
	
		if(LOG.isDebugEnabled()){
			logCall(definition, args, result, time1, time2, time3, time4, time5);
		}

	return result;
	}catch (SQLException sqle){
		reportSQLException(definition, args, sqle);
	
		throw sqle;
	}catch (Exception e){
		reportSQLException(definition, args, e);
	throw new SQLException(e);
	}finally{
		close(call, connection);
	}

}

private void logCall(ProcedureDefinition definition, Object[] args, Object result, long time1, long time2, long time3, long time4,
long time5) {

StringBuilder buf = new StringBuilder(200);

long diff1 = (time2 - time1);
long diff2 = (time3 - time2);
long diff3 = (time4 - time3);
long diff4 = (time5 - time4);
long diff5 = (time5 - time1);

buf.append("stats[" + diff1 + "/" + diff2 + "/" + diff3 + "/" + diff4 + "/" + diff5 + "] ");

// buf.append("userId :0 ");

buf.append(definition.getCompiledSQL());
if (args != null) {
for (int i = 0; i < args.length; i++) {

buf.append("{" + i + " :");

if (args[i] == null) {
buf.append(" null ");
} else if (args[i] instanceof Integer[] || args[i] instanceof String[] || args[i] instanceof Integer
|| args[i] instanceof String || args[i] instanceof Double || args[i] instanceof Double[]) {
buf.append(gson.toJson(args[i]));
} else if (args[i] instanceof List) {
List list = (List) args[i];

if (list.size() < MAX_OUTPUT_SIZE || list.isEmpty())
buf.append("List :" + gson.toJson(list));
else {
buf.append("List :" + list.size());
if (!list.isEmpty())
buf.append(" 1.json:" + gson.toJson(list.get(0)));
}
} else if (args[i] instanceof Set) {
buf.append(" :" + args[i]);
// buf.append(" json:" + gson.toJson(args[i]));
} else {
buf.append(" json:" + gson.toJson(args[i]));
}

buf.append("} ");
}
}

buf.append(" => ");

if (result == null) {
buf.append(" null ");
} else if (result instanceof List) {
List list = (List) result;

if (list.size() < MAX_OUTPUT_SIZE || list.isEmpty())
buf.append("List :" + gson.toJson(list));
else {
buf.append("List :" + list.size());
if (!list.isEmpty())
buf.append(" 1.json:" + gson.toJson(list.get(0)));
}
} else {
buf.append(gson.toJson(result));
// buf.append(result);
}

LOG.debug("::execProc " + buf.toString());
}

private void close(Statement statement, Connection connection) {

if (statement != null) {
try {
statement.close();
} catch (SQLException e) {

}
}

if (connection != null) {
try {
connection.close();
} catch (SQLException e) {

}
}
}

private Object processResult(ProcedureDefinition definition, CallableStatement call, int curNo, int count) throws SQLException {
Object result = null;

if (count == 1 && definition.isHasoutput()) {

if (definition.getRowMapper() != null) {
ResultSet rset = (ResultSet) call.getObject(curNo);
if (rset == null || rset.isClosed())
return null;

try {
if (prefetchSize > 0)
rset.setFetchSize(prefetchSize);
result = processReturnSet(rset, definition.getRowMapper());
} finally {
rset.close();
}

} else {
if (definition.getOutputType() == Types.NUMERIC) {
int int1 = call.getInt(curNo);
return new Integer(int1);
} else if (definition.getOutputType() == Types.DOUBLE) {
double int1 = call.getDouble(curNo);
return new Double(int1);
} else if (definition.getOutputType() == Types.VARCHAR) {
String value = call.getString(curNo);
return value;
} else if (definition.getOutputType() == Types.DATE) {
java.sql.Date value = call.getDate(curNo);
if (value == null)
return null;

return new Date(value.getTime());

} else {
LOG.error("OracleDebugProcedureExecutor::execProc Unprocessed return :" + call.getObject(curNo));
}
}
}

return result;
}

private int prepareCall(ProcedureDefinition definition, Connection connection, CallableStatement call, JdbcTemplate jdbcTemplate2,
Object[] args) throws SQLException {

int curNo = 1;

if (definition.getTypes() != null) {
int types[] = definition.getTypes();

for (int i = 0; i < types.length; i++) {

if (args[i] == null && types[i] != Types.STRUCT) {
call.setNull(curNo, types[i]);
} else if (types[i] == Types.DATE) {
if (args[i] instanceof Date)
call.setTimestamp(curNo, new java.sql.Timestamp(((Date) args[i]).getTime()));
else if (args[i] instanceof Integer)
call.setTime(curNo, new java.sql.Time(((Integer) args[i]).intValue()));
else if (args[i] instanceof Long)
call.setTimestamp(curNo, new java.sql.Timestamp(((Long) args[i]).longValue()));
} else if (types[i] == Types.NUMERIC) {
call.setObject(curNo, args[i]);
} else if (types[i] == Types.VARCHAR) {
call.setObject(curNo, args[i]);
} else if ((types[i] == Types.ARRAY || types[i] == Types.STRUCT) && objmapper != null) {
String name = null;

if (args[i] == null) {
name = definition.getMapperName(i);
} else
name = args[i].getClass().getName();

if (objmapper.containsKey(name)) {
IObjectMapper mapper = objmapper.get(name);
mapper.mapParameter(jdbcTemplate, connection, call, curNo, args[i]);
} else if (args[i] instanceof List) {

if (objmapper == null || objmapper.isEmpty())
throw new SQLException("no object map", "1000");

List list = (List) args[i];
String keyName;

if (list.size() == 0) {
keyName = definition.getMapperName(i);
IObjectMapper mapper = objmapper.get(keyName);

if (mapper != null) {
setViaMapper(mapper, connection, call, curNo, null);
}

} else {
Object obj = list.get(0);
keyName = "List<" + obj.getClass().getName() + ">";

IObjectMapper mapper = objmapper.get(keyName);
if (mapper != null) {
setViaMapper(mapper, connection, call, curNo, args[i]);
} else {
LOG.error("::execProc no object map for :" + keyName);
throw new SQLException("no object map", "keyName");
}
}
} else if (args[i] instanceof Set) {

if (objmapper == null || objmapper.isEmpty())
throw new SQLException("no object map", "1000");

Set set = (Set) args[i];
String keyName;

if (set.size() == 0) {
keyName = definition.getMapperName(i);
IObjectMapper mapper = objmapper.get(keyName);

if (mapper != null) {
setViaMapper(mapper, connection, call, curNo, null);
}

} else {
Object obj = set.iterator().next();
keyName = "Set<" + obj.getClass().getName() + ">";

IObjectMapper mapper = objmapper.get(keyName);
if (mapper != null) {
setViaMapper(mapper, connection, call, curNo, args[i]);
} else {
LOG.error("::execProc no object map for :" + keyName);
throw new SQLException("no object map", "keyName");
}
}
}

} else
call.setObject(curNo, args[i]);

curNo++;
}
}

if (definition.isHasoutput()) {
if (definition.getRowMapper() != null)
call.registerOutParameter(curNo, OracleTypes.CURSOR);
else
call.registerOutParameter(curNo, definition.getOutputType());

}

return curNo;
}

protected void checkDefinition(ProcedureDefinition definition, Object[] args) throws SQLException {

if (definition.getTypes() == null && args != null || definition.getTypes() != null && args == null) {
LOG.error("::execProc :" + definition.getName() + " parameter mismatch 1");
throw new SQLException("mismatch", "20020");
}

if (definition.getTypes() != null && (definition.getTypes().length != args.length)) {
LOG.error("::execProc " + definition.getName() + " parameter mismatch " + definition.getTypes().length + " !=" + args.length);
throw new SQLException("mismatch", "20020");
}
}

private void setViaMapper(IObjectMapper mapper, Connection connection, CallableStatement call, int curNo, Object object)
throws SQLException {

mapper.mapParameter(jdbcTemplate, connection, call, curNo, object);

}

private void reportSQLException(ProcedureDefinition definition, Object[] args, Exception sqle) {
StringBuilder buf = new StringBuilder();

try {
buf.append("::reportSQLException ");

if (sqle != null && sqle instanceof SQLException) {
buf.append(" error:" + ((SQLException) sqle).getErrorCode());
}

buf.append(" exec:" + definition.getCompiledSQL() + " hasOutput:" + definition.isHasoutput());

int index = 0;
if (definition.getTypes() != null) {
int types[] = definition.getTypes();

for (int i = 0; i < types.length; i++) {
index = i + 1;
buf.append("\n\t[" + index + "]: " + types[i] + " :");

if (args[i] == null && types[i] != Types.STRUCT) {
buf.append("NULL");
} else if (types[i] == Types.DATE) {
buf.append("date:" + args[i]);
} else if (types[i] == Types.NUMERIC) {
buf.append("numeric:" + args[i]);
} else if (types[i] == Types.VARCHAR) {
buf.append("varchar:" + args[i]);
} else if ((types[i] == Types.ARRAY || types[i] == Types.STRUCT) && objmapper != null) {

if (args[i] == null) {
buf.append("dmappername:" + definition.getMapperName(i) + " :");
} else
buf.append("classname:" + args[i].getClass().getName() + " :");

buf.append(ObjectUtils.object2String(args[i]));

} else if (args[i] instanceof List) {
List list = (List) args[i];

if (list.isEmpty())
buf.append("List(empty):");
else {
Object obj = list.get(0);
buf.append("List<" + obj.getClass().getName() + "> :");
buf.append(ObjectUtils.object2String(args[i]));
}

} else
buf.append("object:" + args[i]);
}
}

if (sqle != null) {
buf.append("\n\n sqle:" + sqle);
}

if (sqle instanceof SQLException) {
int errorCode = ((SQLException) sqle).getErrorCode();
LOG.warn(buf.toString());
} else
LOG.error(buf.toString(), sqle);

} catch (Throwable th) {
LOG.error(LOG_FATAL, "::reportSQLException buf:" + buf + " sqle:" + sqle, th);
}

}

private Object processReturnSet(ResultSet rset, RowMapper rowMapper) throws SQLException {
ArrayList list = null;

if (rset.next()) {
list = new ArrayList();
int i = 0;

// int columnCount = rset.getMetaData().getColumnCount();

do {
try {
list.add(rowMapper.mapRow(rset, i));
} catch (SQLException e) {
LOG.error("processReturnSet :", e);

if (LOG.isDebugEnabled()) {
ResultSetMetaData metaData = rset.getMetaData();
for (int ri = 1; ri <= metaData.getColumnCount(); ri++) {
LOG.debug("[" + ri + "] :" + metaData.getColumnTypeName(ri) + " =:" + rset.getObject(ri));
}
}

throw e;
}

i++;
} while (rset.next());
}

return list;
}

protected String getSQLKey(String procedureName, Object[] args) {
return procedureName + ":" + (args != null ? args.length : 0);
}

public JdbcTemplate getJdbcTemplate() {
return jdbcTemplate;
}

public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
this.jdbcTemplate = jdbcTemplate;
}

public String getSchemaName() {
return schemaName;
}

public void setSchemaName(String schemaName) {
this.schemaName = schemaName;
}

public Map<String, IObjectMapper> getObjmapper() {
return objmapper;
}

public void setObjmapper(Map<String, IObjectMapper> objmapper) {
this.objmapper = objmapper;
}

public Gson getGson() {
return gson;
}

public void setGson(Gson gson) {
this.gson = gson;
}

public int getPrefetchSize() {
return prefetchSize;
}

public void setPrefetchSize(int prefetchSize) {
this.prefetchSize = prefetchSize;
}

public boolean isEnableJdbcMetrics() {
return enableJdbcMetrics;
}

public void setEnableJdbcMetrics(boolean enableJdbcMetrics) {
this.enableJdbcMetrics = enableJdbcMetrics;
}

}

