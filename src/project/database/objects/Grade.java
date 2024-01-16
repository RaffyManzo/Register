package project.database.objects;

import java.sql.Date;

public class Grade {
    int id;
    private double voto;
    private String tipo;
    private String nota;
    private java.sql.Date data;
    private String studenteId;
    private String materia;

    public Grade(int id, double voto, String tipo, String nota, java.sql.Date data, String studenteId, String materia) {
        this.id = id;
        this.voto = voto;
        this.tipo = tipo;
        this.nota = nota;
        this.data = data;
        this.studenteId = studenteId;
        this.materia = materia;
    }

    public Grade(double voto, String tipo, String nota, java.sql.Date data, String studenteId, String materia) {
        this.voto = voto;
        this.tipo = tipo;
        this.nota = nota;
        this.data = data;
        this.studenteId = studenteId;
        this.materia = materia;
    }


    public double getVoto() {
        return voto;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNota() {
        return nota;
    }


    public java.sql.Date getData() {
        return data;
    }
    public String getStudenteId() {
        return studenteId;
    }

    public String getMateria() {
        return materia;
    }

    public int getID() {return id;}
}
