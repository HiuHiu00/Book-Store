package dao;

import dal.DBContext;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.logging.Level;

public class CustomerDAO {

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
     * Adds a new account to the database.
     *
     * @param email The email of the new account.
     * @param password The password of the new account.
     */
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

    /**
     * Generates a random OTP (One Time Password) with the specified length.
     *
     * @param otpLength the length of the OTP to be generated.
     * @return a randomly generated OTP of the specified length.
     */
    public String generateRandomOTP(int otpLength) {
        String digits = "0123456789";
        Random random = new Random();
        StringBuilder otp = new StringBuilder(otpLength);

        for (int i = 0; i < otpLength; i++) {
            otp.append(digits.charAt(random.nextInt(digits.length())));
        }
        return otp.toString();
    }

    /**
     * Add OTP Code for Account with the given Email, It also includes a
     * scheduled task to automatically delete the OTP code after a specified
     * delay in minutes.
     *
     * @param code The OTP Code to be add.
     * @param email The Email of the account for which the OTP Code added.
     */
    public void addOTPForAccountByEmail(String code, String email) {
        try {
            connection = DBContext.getConnection();
            String query = "UPDATE Account Set VerifyCode = ? Where Email = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1, code);
            ps.setString(2, email);
            ps.executeUpdate();

            //Schedule a task to delete the OTP associated with the given Email after a specified delay.
            scheduleTaskToDeleteOTP(email, 5);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error occurred while add otp code by email", e);
        } finally {
            closeConnection(connection, ps, rs);
        }
    }

    /**
     * Schedule a task to delete the OTP Code associated with the given Email
     * after a specified delay.
     *
     * @param email The email for which the OTP Code is to be deleted.
     * @param delayInMinutes The delay in minutes before deleting the OTP Code.
     */
    public void scheduleTaskToDeleteOTP(String email, int delayInMinutes) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(() -> {
            try {
                deleteOTPByEmail(email);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, delayInMinutes, TimeUnit.MINUTES);
    }

    /**
     * Delete OTP Code associated with the given Email.
     *
     * @param email The email for which the OTP Code is to be deleted.
     * @throws SQLException If a database access error occurs.
     */
    public void deleteOTPByEmail(String email) throws SQLException {
        try {
            connection = DBContext.getConnection();
            String deleteQuery = "UPDATE Account Set VerifyCode = NULL Where Email = ?";
            ps = connection.prepareStatement(deleteQuery);
            ps.setString(1, email);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error occurred while delete otp code for email", e);
        } finally {
            closeConnection(connection, ps, rs);
        }
    }

    /**
     * Add new password for Account with the given Email.
     *
     * @param newPassword The new password to be add.
     * @param email The Email of the account for which the OTP Code added.
     */
    public void addNewPasswordForAccountByEmail(String newPassword, String email) {
        try {
            connection = DBContext.getConnection();
            String query = "UPDATE Account Set Password = ? Where Email = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1, newPassword);
            ps.setString(2, email);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error occurred while add new password by email", e);
        } finally {
            closeConnection(connection, ps, rs);
        }
    }

    /**
     * Generates a random password with the specified length using alphanumeric
     * characters.
     *
     * @param passwordLength The length of the password to be generated.
     * @return A randomly generated password of the specified length.
     */
    public String generateRandomPassword(int passwordLength) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder password = new StringBuilder(passwordLength);

        for (int i = 0; i < passwordLength; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

    /**
     * Retrieves the verify code of an account by email.
     *
     * @param email The email of the account.
     * @return {@code verify code} of the account, or {@code null} if no account
     * is found.
     */
    public String getVerifyCodeByEmail(String email) {
        try {
            connection = DBContext.getConnection();
            String query = "SELECT VerifyCode FROM Account WHERE Email=?";
            ps = connection.prepareStatement(query);
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("VerifyCode");
            }
            return null;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error occurred while get verify code by email", e);
            return null;
        } finally {
            closeConnection(connection, ps, rs);
        }
    }

}

class TestCustomerDAO {

    public static void main(String[] args) {
        CustomerDAO cd = new CustomerDAO();
        cd.checkAccountExistsByEmail("hieulove0408@gmail.com");

    }
}
