package project.database.objects;

import java.sql.Date;

public class AttendanceEvent {
    int id;
    private String tipo;
    private java.sql.Date data;
    private String studentId;
    private String teacherId;

    public AttendanceEvent(int id, String type, java.sql.Date data, String studentId, String teacherId) {
        this.id = id;
        this.data = data;
        this.tipo = type;
        this.studentId = studentId;
        this.teacherId = teacherId;
    }

    public AttendanceEvent(String type, java.sql.Date data, String studentId, String teacherId) {
        data = new java.sql.Date(new java.util.Date().getTime());
        this.tipo = type;
        this.data = data;
        this.studentId = studentId;
        this.teacherId = teacherId;
    }

    public String getTipo() {
        return tipo;
    }

    public Date getData() {
        return data;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public int getID() {return id;}
}
