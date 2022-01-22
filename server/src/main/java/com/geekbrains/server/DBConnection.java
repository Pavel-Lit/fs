package com.geekbrains.server;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class DBConnection {
    private static Connection connection;
    private static Statement statement;


    public static void start(){
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            statement = connection.createStatement();
            statement.execute("create table if not exists users (id integer primary key autoincrement, login text,  password text);");
            log.debug("database connected");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void stop() {
        try {
            if (statement != null) statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static int getIdByLoginAndPassword(String login, String password) {

        String sqlQuery = String.format("SELECT id FROM users WHERE login = '%s' AND password = '%s';", login, password);
        try (ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            if (resultSet.next()) {
                int id;
                id =  resultSet.getInt(1);
                resultSet.close();
                return id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
//    public static void addNewUser(String login, String password){
//        String sqlQuery = String.format("SELECT id FROM users WHERE login = '%s' AND password = '%s';", login, password);
//        try (ResultSet resultSet = statement.executeQuery(sqlQuery)) {
//
//            if(!resultSet.next()){
//                String sqlQueryAddUser = String.format("INSERT INTO users (login, password) VALUES " +
//                        "('%s', '%s');", login, password);
//                PreparedStatement ps = connection.prepareStatement(sqlQueryAddUser);
//                ps.executeUpdate();
//            }
//        } catch (SQLException e){
//            e.printStackTrace();
//        }
//    }

    public static int addNewUser(String login, String password){
        if (checkUser(login)== -1){
           return -1;
        }
        String sqlQuery = String.format("INSERT INTO users (login, password) VALUES " +
               "('%s', '%s');", login, password);
        try {
            statement.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static int checkUser(String login) {
        String sqlQuery = String.format("SELECT IIF (count(1)>0,  'User already exists', 'User can create') FROM users WHERE login = '%s'", login);
        try(ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            if (resultSet.getString(1).equals("User already exists")){
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }
}


