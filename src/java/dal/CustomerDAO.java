package dal;

import context.DBContext;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Logger;
import java.util.logging.Level;

public class CustomerDAO {

    private static final Logger logger = Logger.getLogger("Log");
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

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

    public void addAccount(String email, String password) {
        try {
            connection = DBContext.getConnection();
            String query = "INSERT INTO Account(Email, Password, RoleID) VALUES(?,?,1)";
            ps = connection.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error occurred while retrieving password by email", e);
        } finally {
            closeConnection(connection, ps, rs);
        }
    }

}

class TestCustomerDAO {

    public static void main(String[] args) {
        String filePath = "web/views/EmailSenderForm.html";

        StringBuilder content = new StringBuilder();

        try ( BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String updatedContent = content.toString().replace("XXXXXX", "123321");

        System.out.println(updatedContent);

    }
}