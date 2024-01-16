package project.database;

import project.database.objects.Grade;

import java.sql.*;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class GradeDAO extends AbstractDataAccessObject<Grade>{
    @Override
    protected Grade extractFromResultSet(ResultSet rs) throws SQLException {
        return new Grade(rs.getInt("ID"),
                rs.getDouble("Voto"),
                rs.getString("Tipo"),
                rs.getString("Nota"),
                rs.getDate("Data"),
                rs.getString("Studente"),
                rs.getString("Materia"));
    }

    public double getAvgByStudentIdAndSubject(String studentID, String subject) {
        try(Connection conn = getConnection();
            CallableStatement clst = conn.prepareCall(getQuery("AVG_each"))) {

            clst.setString(1, studentID);
            clst.setString(2, subject);

            ResultSet rs = clst.executeQuery();
            if(rs.next()){
                return rs.getDouble("avgStudent");
            } else
                return 0.0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insertInto(Grade g) throws SQLException {
        try(Connection conn = getConnection(); CallableStatement cl = conn.prepareCall(getQuery("insert_val"));) {
            cl.setDouble(1, g.getVoto());
            cl.setString(2, g.getTipo());
            cl.setString(3, g.getNota());
            cl.setString(4, g.getStudenteId());
            cl.setString(5, g.getMateria());
            cl.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Grade> listOfGradeOrderByDate(String subject, String selClass) {
        try(Connection conn = getConnection(); PreparedStatement cl = conn.prepareStatement(getQuery("get_all_grades_by_date"));) {
            cl.setString(1, subject);
            cl.setString(2, selClass);
            return rsReader(cl.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeByID(int eventID) {
        try(Connection conn = getConnection(); CallableStatement cl = conn.prepareCall(getQuery("delete_grade_by_id"));) {
            cl.setInt(1, eventID);
            cl.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Grade> getAllStudentGrades(String studentID) {
        try(Connection conn = getConnection(); PreparedStatement cl = conn.prepareStatement(getQuery("get_all_student_grade_by_id"));) {
            cl.setString(1, studentID);
            return rsReader(cl.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Grade> getWeeklyStudentGrades(String studentID) {
        try(Connection conn = getConnection(); PreparedStatement cl = conn.prepareStatement(getQuery("get_weekly_student_grade_by_id"));) {
            cl.setString(1, studentID);
            return rsReader(cl.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public double getAVGInDate(String studentID, Date date) {
        try(Connection conn = getConnection(); PreparedStatement cl = conn.prepareStatement(getQuery("get_avg_all_grade_in_date"));) {
            cl.setString(1, studentID);
            cl.setDate(2, date);

            ResultSet rs = cl.executeQuery();
            if(rs.next())
                return rs.getDouble(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return 0.0;

    }

    public double getAvgForType1(String studentID) {
        try(Connection conn = getConnection(); PreparedStatement cl = conn.prepareStatement(getQuery("get_avg_by_type_1"));) {
            cl.setString(1, studentID);
            ResultSet rs = cl.executeQuery();
            if(rs.next())
                return rs.getDouble("Avg");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public double getAvgForType2(String studentID) {
        try(Connection conn = getConnection(); PreparedStatement cl = conn.prepareStatement(getQuery("get_avg_by_type_2"));) {
            cl.setString(1, studentID);
            ResultSet rs = cl.executeQuery();
            if(rs.next())
                return rs.getDouble("Avg");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public double getAvg(String studentID) {
        try(Connection conn = getConnection(); PreparedStatement cl = conn.prepareStatement(getQuery("get_avg"));) {
            cl.setString(1, studentID);
            ResultSet rs = cl.executeQuery();
            if(rs.next())
                return rs.getDouble("Avg");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public double getClassAvg(String subject, String classID) {
        try(Connection conn = getConnection(); PreparedStatement cl = conn.prepareStatement(getQuery("get_class_avg"));) {
            cl.setString(1, subject);
            cl.setString(2, classID);
            ResultSet rs = cl.executeQuery();
            if(rs.next())
                return rs.getDouble(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
