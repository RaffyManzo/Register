package project.database.objects;

import java.sql.Date;

public class Teacher {
    private String matricola;
    private String cognome;
    private String nome;
    private Date dataDiNascita;
    private String residenza;
    private int annoDiIscrizione;
    private String email;
    private String materia;

    public Teacher(String matricola, String cognome, String nome, Date dataDiNascita, String residenza, String email, String materia) {
        this.matricola = matricola;
        this.cognome = cognome;
        this.nome = nome;
        this.dataDiNascita = dataDiNascita;
        this.residenza = residenza;
        this.email = email;
        this.materia = materia;
    }

    public String getMatricola() {
        return matricola;
    }

    public String getCognome() {
        return cognome;
    }

    public String getNome() {
        return nome;
    }

    public Date getDataDiNascita() {
        return dataDiNascita;
    }

    public String getResidenza() {
        return residenza;
    }

    public String getEmail() {
        return email;
    }

    public String getMateria() {
        return materia;
    }
}
