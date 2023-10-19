package es.cachau.managers.entities;

import es.cachau.Servidor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Group {

  public static List<Group> groups = new ArrayList<>();

  public final String name;
  public final int id;

  public Group(String name, int id) throws SQLException {
    this.name = name;
    this.id = id;

    this.create();
    groups.add(this);
  }

  private void create() throws SQLException {
    String query = "INSERT INTO grups SET nom_grup = '" + this.name + "';";
    Servidor.database.statement.executeUpdate(query);
  }

  public static void deleteGroup(Group group) throws SQLException {
    String query = "DELETE FROM grups WHERE id_grup = " + group.id + ";";
    ResultSet resultSet = Servidor.database.statement.executeQuery(query);

    if (resultSet.next()) {
      System.out.println("Grup eliminat correctament.");
    } else {
      System.out.println("No s'ha pogut eliminar el grup.");
    }
  }

  public static Group getByName(String name) {
    for (Group group : groups) {
      if (group.getName().equals(name)) {
        return group;
      }
    }
    return null;
  }

  public int getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }
}
