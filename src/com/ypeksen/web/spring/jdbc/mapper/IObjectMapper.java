package com.ypeksen.web.spring.jdbc.mapper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;

public interface IObjectMapper<T> {

void mapParameter(JdbcTemplate jdbcTemplate,Connection connection, CallableStatement call, int curNo, T object) throws SQLException;

}