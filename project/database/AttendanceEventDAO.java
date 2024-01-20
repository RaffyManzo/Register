package project.database;

import project.database.objects.AttendanceEvent;
import project.database.objects.Grade;
import project.util.Type;

import java.sql.*;
import java.util.*;

public class AttendanceEventDAO extends AbstractDataAccessObject<AttendanceEvent>{
    @Override
    protected AttendanceEvent extractFromResultSet(ResultSet rs) throws SQLException {
        return new AttendanceEvent(rs.getInt("ID"),
                rs.getString("Tipo"),
                rs.getDate("Data"),
                rs.getString("Studente"),
                rs.getString("Docente"));
    }

    @Override
    public void insertInto(AttendanceEvent attendanceEvent) throws SQLException {
        try(Connection conn = getConnection(); CallableStatement cl = conn.prepareCall(getQuery("insert_att"));) {
            deleteIfThereAreARecordInTheSameDate(attendanceEvent.getStudentId());

            cl.setString(1, attendanceEvent.getTipo());
            cl.setString(2, attendanceEvent.getStudentId());
            cl.setString(3, attendanceEvent.getTeacherId());
            cl.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteIfThereAreARecordInTheSameDate(String studentId) {
        try(Connection conn = getConnection(); CallableStatement cl = conn.prepareCall(getQuery("delete_att"));) {
            cl.setString(1, studentId);
            cl.execute();
            if(cl.getUpdateCount() > 0)
                System.out.println("A record has been eliminated");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dictionary<String, String> getStudentListOfAttendanceEventByDate(String classID) {
        Dictionary<String, String> result = new Hashtable<>();
        try(Connection conn = getConnection(); PreparedStatement st = conn.prepareStatement(getQuery("get_events_by_date"));) {
            st.setString(1, classID);

            ResultSet rs = st.executeQuery();

            while(rs.next()) {
                result.put(rs.getString("Studente"), rs.getString("Tipo"));
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<AttendanceEvent> listOfEventsOrderByDate(String teacherId, String classID) {
        try(Connection conn = getConnection(); PreparedStatement cl = conn.prepareStatement(getQuery("get_all_event_by_date"));) {
            cl.setString(1, teacherId);
            cl.setString(2, classID);
            return rsReader(cl.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeByID(int eventID) {
        try(Connection conn = getConnection(); CallableStatement cl = conn.prepareCall(getQuery("delete_event_by_id"));) {
            cl.setInt(1, eventID);
            cl.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<AttendanceEvent> getTodayEvents(String studentID) {
        try(Connection conn = getConnection(); PreparedStatement pst = conn.prepareStatement(getQuery("get_today_events"));) {
            pst.setString(1, studentID);
            return rsReader(pst.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<AttendanceEvent> getAllStudentEvents(String studentID) {
        try(Connection conn = getConnection(); PreparedStatement cl = conn.prepareStatement(getQuery("get_all_student_event_by_id"));) {
            cl.setString(1, studentID);
            return rsReader(cl.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int countTypeById(String studentID, project.util.Type type) {
        try(Connection conn = getConnection();
            PreparedStatement cl = conn.prepareStatement(getQuery("count_event_type_by_id"), ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            cl.setString(1, studentID);
            cl.setString(2, type.toString());

            ResultSet rs = cl.executeQuery();

            if(rs.absolute(1))
                return rs.getInt("Count");
            else return 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
