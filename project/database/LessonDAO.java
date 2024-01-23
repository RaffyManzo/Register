package project.database;

import project.database.objects.Lesson;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LessonDAO extends AbstractDataAccessObject<Lesson>{
    @Override
    protected Lesson extractFromResultSet(ResultSet rs) throws SQLException {
        return new Lesson(rs.getInt(1),
                rs.getDate(2),
                rs.getInt(3),
                rs.getString(4),
                rs.getString(5));
    }

    @Override
    public void insertInto(Lesson lesson) {
        try(Connection conn = getConnection(); CallableStatement cl = conn.prepareCall(getQuery("insert_lesson"));) {
            cl.setDouble(1, lesson.getOra());
            cl.setString(2, lesson.getClassId());
            cl.setString(3, lesson.getTeahcerId());
            cl.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Lesson> getCurrentLesson(int hour, String classID, String teacherID) {
        try(Connection conn = getConnection(); PreparedStatement cl = conn.prepareStatement(getQuery("get_lesson_info"));) {
            cl.setInt(1, hour);
            cl.setString(2, classID);
            cl.setString(3, teacherID);
            return rsReader(cl.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeByAtt(int hour, String classID, String teacherID ) {
        try(Connection conn = getConnection(); CallableStatement cl = conn.prepareCall(getQuery("delete_lesson"));) {
            cl.setInt(1, hour);
            cl.setString(2, classID);
            cl.setString(3, teacherID);
            cl.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean alreadyExixt(int hour, String teacherID, String classID) {
        try (Connection conn = getConnection(); PreparedStatement cl = conn.prepareStatement(getQuery("find_another_lession"));) {
            cl.setInt(1, hour);
            cl.setString(2, teacherID);
            ArrayList<Lesson> ls = (ArrayList<Lesson>) rsReader(cl.executeQuery());

            if(ls.isEmpty()) {
                return false;
            } else {
                return !ls.get(0).getClassId().contentEquals(classID);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
