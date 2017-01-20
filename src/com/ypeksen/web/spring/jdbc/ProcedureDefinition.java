package com.ypeksen.web.spring.jdbc;


import java.sql.Types;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

public class ProcedureDefinition {
private String schemaName;
private String name;
private RowMapper rowMapper;
private List<RowMapper<? extends Object>> rowMappers;
private int outputType = 0;
private int[] types;
private String mapperName[];
private String compiledSQL;
private boolean hasoutput = true;

public ProcedureDefinition(String schemaName, String name, RowMapper rowMapper, Object... types) {
this.schemaName = schemaName;
this.name = name;
this.rowMapper = rowMapper;
initMappers(types);
init();
}
public ProcedureDefinition(String schemaName, String name, List<RowMapper<? extends Object>> rowMappers, int... types) {
this.schemaName = schemaName;
this.name = name;
this.rowMappers = rowMappers;
this.types = types;
this.hasoutput = true;
init();
}

public ProcedureDefinition(String schemaName, String name, boolean hasoutput, Object... types) {
this.schemaName = schemaName;
this.name = name;
this.hasoutput = hasoutput;
initMappers(types);
}

public ProcedureDefinition(String schemaName, String name, int outputType, Object... types) {
this.schemaName = schemaName;
this.name = name;
this.outputType = outputType;
initMappers(types);
}

public void initMappers(Object... types) {
this.types = new int[types.length];
this.mapperName = new String[types.length];

for (int i = 0; i < types.length; i++) {
if (types[i] instanceof String) {
mapperName[i] = (String) types[i];
this.types[i] = Types.STRUCT;
} else
this.types[i] = (Integer) types[i];
}

init();
}

public String getMapperName(int i) {
if (mapperName != null && mapperName.length > i)
return mapperName[i];

return null;
}

public void init() {

StringBuilder buf = new StringBuilder();
buf.append("{call ");

if (schemaName != null)
buf.append(schemaName).append(".");

buf.append(name).append("(?,?");
if (rowMappers != null){
int size = rowMappers.size();
for (int i = 0; i < size; i++) {
buf.append(",?");
}
} else if(hasoutput){
buf.append(",?");
}

if (types != null) {
for (int type : types) {
buf.append(",?");
}
}
buf.append(")}");
compiledSQL = buf.toString();
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public RowMapper getRowMapper() {
return rowMapper;
}

public void setRowMapper(RowMapper rowMapper) {
this.rowMapper = rowMapper;
}

public int getOutputType() {
return outputType;
}

public void setOutputType(int outputType) {
this.outputType = outputType;
}

public int[] getTypes() {
return types;
}

public void setTypes(int[] types) {
this.types = types;
}

public String getCompiledSQL() {
return compiledSQL;
}

public void setCompiledSQL(String compiledSql) {
compiledSQL = compiledSql;
}

public boolean isHasoutput() {
return hasoutput;
}

public void setHasoutput(boolean hasoutput) {
this.hasoutput = hasoutput;
}

public String getSchemaName() {
return schemaName;
}

public void setSchemaName(String schemaName) {
this.schemaName = schemaName;
}

public List<RowMapper<? extends Object>> getRowMappers() {
return rowMappers;
}

public void setRowMappers(List<RowMapper<? extends Object>> rowMappers) {
this.rowMappers = rowMappers;
}

}


