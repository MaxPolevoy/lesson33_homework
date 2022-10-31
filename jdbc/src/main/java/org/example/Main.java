package org.example;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class Main {
    private static final String INSERT_CITY = "INSERT INTO city_student (city) VALUES (?);"; // акер добавить свои данные не сможет

    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbcDriver");
        String url = "jdbc:mysql://localhost:3306/student";
        String user = "root";
        String password = "root";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            log.debug("Auto commit enable: {}", connection.getAutoCommit());// получаем ответ есть ли автокомит подключенные на уровне базы данных

            //создание таблици в джава
            Statement statement = connection.createStatement(); // выполняем запрос к базе данных
            statement.execute("CREATE TABLE IF NOT EXISTS city_student (" +
                    "id bigint primary key auto_increment, " +
                    "city varchar(50) not null ," +
                    ")");

            PreparedStatement insertCity_student = connection.prepareStatement(INSERT_CITY, Statement.RETURN_GENERATED_KEYS);
            insertCity_student.setString(1, "Minsk");
            insertCity_student.setString(1, "New York");
            insertCity_student.setString(1, "Berlin");
            insertCity_student.setString(1, "Kyiv");
            insertCity_student.setString(1, "New York");
            int insertedRows = insertCity_student.executeUpdate();
            log.debug("Inserted {} rows", insertedRows);

            ResultSet generatedKeys = insertCity_student.getGeneratedKeys();
            Long City_studentId = null;
            while (generatedKeys.next()) {
                City_studentId = generatedKeys.getLong(1);
            }
            log.info("Insert city_studentId with id {}", City_studentId);

            PreparedStatement student = connection.prepareStatement("SELECT * FROM student;");
            ResultSet resultSet = student.executeQuery();
            while (resultSet.next()) {
                log.info("Student found: id={}, name={}, last={}, gender={}, elective={}",
                        resultSet.getLong("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("gender"),
                        resultSet.getString("elective"));
            }

        } catch (SQLException e) {
            log.error("Exception during DB connection: {}", e.getMessage(), e);
        }
    }
}