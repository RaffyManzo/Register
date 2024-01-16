package project.database;

import project.database.objects.Student;
import project.database.objects.Teacher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO extends AbstractDataAccessObject{
    @Override
    protected Object extractFromResultSet(ResultSet rs) throws SQLException {
        return new Student(rs.getString("Matricola"),
                rs.getString("Cognome"),
                rs.getString("Nome"),
                rs.getDate("DataDiNascita"),
                rs.getString("Residenza"),
                rs.getInt("AnnoDiIscrizione"),
                rs.getString("DatiAccesso"),
                rs.getString("Classe"));
    }

    @Override
    protected void insertInto(Object o) throws SQLException {

    }

    public List<Student> getAllStudentByClass(String classID) {
        try(Connection conn = getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement(getQuery("student_list"));){
            pstmt.setString(1, classID);
            return rsReader(pstmt.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<Student>();
    }

    public Student getByID(String id) {
        try(Connection conn = getConnection(); PreparedStatement cl = conn.prepareStatement(getQuery("get_student_by_id"));) {
            cl.setString(1, id);
            return (Student) rsReader(cl.executeQuery()).get(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Student> findByEmail(String email) {
        try(Connection conn = getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement(getQuery("find_student_by_email"));
        ){
            pstmt.setString(1, email);
            return rsReader(pstmt.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
