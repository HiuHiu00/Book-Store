package dao;

import dal.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminDAO {

    private static final Logger logger = Logger.getLogger("Log");
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    /**
     * Closes the database resources connection.
     *
     * @param connection Connection the database connection to be closed.
     * @param ps The PreparedStatement to be closed.
     * @param rs The ResultSet to be closed
     */
    private void closeConnection(Connection connection, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (connection != null) {
                DBContext.releaseConnection(connection);
                logger.log(Level.INFO, "Connection released back to the pool.");
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error closing resources", ex);
        }
    }

    /**
     * Retrieves the password for an account by email.
     *
     * @param email The email of the account.
     * @return {@code password} of the account, or {@code null} if no account is
     * found.
     */
    public String getPasswordByEmail(String email) {
        try {
            connection = DBContext.getConnection();
            String query = "SELECT password FROM Account WHERE Email=?";
            ps = connection.prepareStatement(query);
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("password");
            }
            return null;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error occurred while retrieving password by email", e);
            return null;
        } finally {
            closeConnection(connection, ps, rs);
        }
    }

    /**
     * Retrieves the password for an account by email.
     *
     * @param email The email of the account.
     * @return {@code password} of the account, or {@code null} if no account is
     * found.
     */
    public String getRoleKeyByEmail(String email) {
        try {
            connection = DBContext.getConnection();
            String query = """
                           SELECT rk.RoleKey FROM RoleKey rk JOIN [Role] r on r.RoleID = rk.RoleID 
                                       JOIN Account a on a.RoleID = r.RoleID
                                       WHERE email = ?""";
            ps = connection.prepareStatement(query);
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("RoleKey");
            }
            return null;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error occurred while retrieving role key by email", e);
            return null;
        } finally {
            closeConnection(connection, ps, rs);
        }
    }

    public int getAccountRoleIDByEmail(String email) {
        try {
            connection = DBContext.getConnection();
            String query = """
                           SELECT RoleID FROM Account WHERE email = ?""";
            ps = connection.prepareStatement(query);
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("RoleId");
            }
            return -1;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error occurred while retrieving role id by email", e);
            return -1;
        } finally {
            closeConnection(connection, ps, rs);
        }
    }

    /**
     * Checks if an account exists in the database by email.
     *
     * @param email The email to check for existence.
     * @return {@code true} if the account exists, {@code false} otherwise.
     */
    public boolean checkAccountExistsByEmail(String email) {

        try {
            connection = DBContext.getConnection();
            String query = "SELECT * FROM Account WHERE Email = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1, email);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error occurred while checking account by email", e);
            return false;
        } finally {
            closeConnection(connection, ps, rs);
        }
    }

}

class TestAdminDAO {

    public static void main(String[] args) {
        AdminDAO ad = new AdminDAO();
        System.out.println(ad.getAccountRoleIDByEmail("hieulove0408@gmail.com"));
        System.out.println(ad.getAccountRoleIDByEmail("adminTest@gmail.com"));
    }
}
