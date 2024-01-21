package project.database.objects;

import java.sql.Date;

public class Lession {
    private int id;
    private java.sql.Date data;
    private int ora;
    private String classId;
    private String teahcerId;

    public Lession(int id, Date data, int ora, String classId, String teahcerId) {
        this.id = id;
        this.data = data;
        this.ora = ora;
        this.classId = classId;
        this.teahcerId = teahcerId;
    }

    public Lession(int ora, String classId, String teahcerId) {
        this.ora = ora;
        this.classId = classId;
        this.teahcerId = teahcerId;
    }

    public int getId() {
        return id;
    }

    public Date getData() {
        return data;
    }

    public int getOra() {
        return ora;
    }

    public String getClassId() {
        return classId;
    }

    public String getTeahcerId() {
        return teahcerId;
    }
}
