package project.database;

import project.database.objects.Lession;
import project.database.objects.Student;

import java.sql.*;
import java.util.List;

public class LessionDAO extends AbstractDataAccessObject<Lession>{
    @Override
    protected Lession extractFromResultSet(ResultSet rs) throws SQLException {
        return new Lession(rs.getInt(1),
                rs.getDate(2),
                rs.getInt(3),
                rs.getString(4),
                rs.getString(5));
    }

    @Override
    public void insertInto(Lession lession) {
        try(Connection conn = getConnection(); CallableStatement cl = conn.prepareCall(getQuery("insert_lession"));) {
            cl.setDouble(1, lession.getOra());
            cl.setString(2, lession.getClassId());
            cl.setString(3, lession.getTeahcerId());
            cl.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Lession> getCurrentLession(int hour, String classID, String teacherID) {
        try(Connection conn = getConnection(); PreparedStatement cl = conn.prepareStatement(getQuery("get_lession_info"));) {
            cl.setInt(1, hour);
            cl.setString(2, classID);
            cl.setString(3, teacherID);
            return rsReader(cl.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeByAtt(int hour, String classID, String teacherID ) {
        try(Connection conn = getConnection(); CallableStatement cl = conn.prepareCall(getQuery("delete_lession"));) {
            cl.setInt(1, hour);
            cl.setString(2, classID);
            cl.setString(3, teacherID);
            cl.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
