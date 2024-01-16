package project.database.objects;

import java.sql.Date;

public class Student {
    private final String matricola;
    private final String cognome;
    private final String nome;
    private final Date dataDiNascita;
    private final String residenza;
    private final int annoDiIscrizione;
    private final String email;
    private final String classe;

    public Student(String matricola, String cognome, String nome, Date dataDiNascita, String residenza, int annoDiIscrizione, String email, String classe) {
        this.matricola = matricola;
        this.cognome = cognome;
        this.nome = nome;
        this.dataDiNascita = dataDiNascita;
        this.annoDiIscrizione = annoDiIscrizione;
        this.residenza = residenza;
        this.email = email;
        this.classe = classe;
    }

    public int getAnnoDiIscrizione() {
        return annoDiIscrizione;
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

    public String getClasse() {
        return classe;
    }
}


