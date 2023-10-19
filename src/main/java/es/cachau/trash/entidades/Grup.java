/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.cachau.trash.entidades;

/**
 *
 * @author hugo2
 */
public class Grup {
    int id_grup;
    String nom_grup;

    public Grup(int id_grup, String nom_grup) {
        this.id_grup = id_grup;
        this.nom_grup = nom_grup;
    }

    public int getIdGrup() {
        return id_grup;
    }

    public String getNomGrup() {
        return nom_grup;
    }
}
