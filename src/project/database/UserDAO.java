package project.database;

import project.database.objects.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDAO extends AbstractDataAccessObject {


    @Override
    protected Object extractFromResultSet(ResultSet rs) throws SQLException {
        return new User(rs.getString(1), rs.getString(2));
    }

    @Override
    protected void insertInto(Object o) throws SQLException {

    }

    public boolean findUser(String email, String password) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt =
                     conn.prepareStatement(getQuery("login"))) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            List<User> result = rsReader(pstmt.executeQuery());
            return !result.isEmpty();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
