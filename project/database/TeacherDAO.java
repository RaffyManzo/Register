package project.database;

import project.database.objects.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherDAO extends AbstractDataAccessObject{
    @Override
    protected Object extractFromResultSet(ResultSet rs) throws SQLException {
        return new Teacher(rs.getString("Matricola"),
                rs.getString("Cognome"),
                rs.getString("Nome"),
                rs.getDate("DataDiNascita"),
                rs.getString("Residenza"),
                rs.getString("DatiAccesso"),
                rs.getString("Materia"));
    }

    @Override
    protected void insertInto(Object o) throws SQLException {

    }

    public Teacher getInfo(String email) {
        try(Connection conn = getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement(getQuery("find_teacher_by_email"));){
            pstmt.setString(1, email);
            List<Teacher> output = rsReader(pstmt.executeQuery());

            if(!output.isEmpty())
                return output.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getSubjectFromId(String teacherID) {
        try(Connection conn = getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement(getQuery("find_teacher_by_id"));){
            pstmt.setString(1, teacherID);
            List<Teacher> output = rsReader(pstmt.executeQuery());

            if(!output.isEmpty())
                return output.get(0).getMateria();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Teacher> findByEmail(String email) {
        try(Connection conn = getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement(getQuery("find_teacher_by_email"));
            ){
            pstmt.setString(1, email);

            return rsReader(pstmt.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Teacher> exceptThisClass(String teacherID, String classID) {
        try(Connection conn = getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement(getQuery("not_same_class_lession"));
        ){
            pstmt.setString(1, teacherID);
            pstmt.setString(2, teacherID);
            pstmt.setString(3, classID);

            return rsReader(pstmt.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Teacher> divisionForClassNumber(String teacherID, int classN) {
        try(Connection conn = getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement(getQuery("all_class_firm_today"));
        ){
            pstmt.setString(1, teacherID);
            pstmt.setInt(2, classN);

            return rsReader(pstmt.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Teacher findById(String id) {
        try(Connection conn = getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement(getQuery("find_teacher_by_id"));
        ){
            pstmt.setString(1, id);

            return (Teacher) rsReader(pstmt.executeQuery()).get(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
