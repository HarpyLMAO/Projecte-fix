package es.cachau.managers.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {

  public Connection connection;
  public Statement statement;

  public Database() throws ClassNotFoundException {
    this.connect();
  }

  public void connect() throws ClassNotFoundException {
    Class.forName("com.mysql.cj.jdbc.Driver");

    try {
      connection =
        DriverManager.getConnection(
          "jdbc:mysql://localhost:3307/bbdd_projectexat",
          "root",
          "1234"
        );
      statement = connection.createStatement();
    } catch (java.sql.SQLException e) {
      e.printStackTrace();
    }
  }
}
