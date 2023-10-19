package es.cachau.managers.entities;

import es.cachau.Servidor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class User {

  public static List<User> users = new ArrayList<>();

  public String name;
  public String alias;
  public String password;

  public User(String name, String alias, String password) throws SQLException {
    this.name = name;
    this.alias = alias;
    this.password = password;

    this.create(name, alias, this.password);
    users.add(this);
  }

  private void create(String name, String alias, String password)
    throws SQLException {
    String query =
      "INSERT INTO usuaris SET nom_usuari = '" +
      name +
      "', password_usuari = '" +
      password +
      "', alias_usuari = '" +
      alias +
      "';";

    Servidor.database.statement.executeUpdate(query);
  }

  public String getName() {
    return this.name;
  }

  public static User getByName(String name) {
    for (User user : users) {
      if (user.getName().equals(name)) {
        return user;
      }
    }
    return null;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAlias() {
    return this.alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public String getPassword() {
    return this.password.toString();
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean checkPassword(String password) {
    return this.password.equals(password);
  }
}
