package project.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClassDAO extends AbstractDataAccessObject<project.database.objects.Class>{
    @Override
    protected project.database.objects.Class extractFromResultSet(ResultSet rs) throws SQLException {
        return new project.database.objects.Class(rs.getString("Codice"),
                rs.getInt("numero"),
                rs.getString("sezione"),
                rs.getString("indirizzo"));
    }

    @Override
    protected void insertInto(project.database.objects.Class o) throws SQLException {

    }

    public List<project.database.objects.Class> classList() {
        try(Connection conn = getConnection(); PreparedStatement prsmt = conn.prepareStatement(getQuery("class_list"));) {
            return rsReader(prsmt.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public project.database.objects.Class getClassByStudentID(String studentID) {
        try(Connection conn = getConnection(); PreparedStatement prsmt = conn.prepareStatement(getQuery("get_class_info_by_studentid"));) {
            prsmt.setString(1, studentID);
            return rsReader(prsmt.executeQuery()).get(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public project.database.objects.Class getClassID(String classID) {
        try(Connection conn = getConnection(); PreparedStatement prsmt = conn.prepareStatement(getQuery("get_class_by_id"));) {
            prsmt.setString(1, classID);
            return rsReader(prsmt.executeQuery()).get(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
