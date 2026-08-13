package com.example.chataiserver.util;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public final class ResultSetReaders {

    private ResultSetReaders() {
    }

    public static String string(ResultSet resultSet, String column) throws SQLException {
        return resultSet.getString(column);
    }

    public static BigDecimal decimal(ResultSet resultSet, String column) throws SQLException {
        return resultSet.getBigDecimal(column);
    }

    public static Integer integer(ResultSet resultSet, String column) throws SQLException {
        int value = resultSet.getInt(column);
        return resultSet.wasNull() ? null : value;
    }

    public static Long longValue(ResultSet resultSet, String column) throws SQLException {
        long value = resultSet.getLong(column);
        return resultSet.wasNull() ? null : value;
    }

    public static LocalDate localDate(ResultSet resultSet, String column) throws SQLException {
        java.sql.Date value = resultSet.getDate(column);
        return value == null ? null : value.toLocalDate();
    }

    public static LocalDateTime localDateTime(ResultSet resultSet, String column) throws SQLException {
        Timestamp value = resultSet.getTimestamp(column);
        return value == null ? null : value.toLocalDateTime();
    }
}
