package com.ypeksen.web.spring.jdbc;

import java.sql.SQLException;

public interface IProcedureExecutor {

	Object  execProc(ProcedureDefinition definition, Object[] args) throws SQLException;

}
