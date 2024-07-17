package dao;

import dal.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CartAndOrderDAO {

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

    public Integer getCartIdByAccountID(int accountID) {
        try {
            connection = DBContext.getConnection();
            String query = "SELECT CartID FROM Cart WHERE AccountID = ?";
            ps = connection.prepareStatement(query);
            ps.setInt(1, accountID);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("CartID");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error occurred while get cart ID by account ID", e);
        } finally {
            closeConnection(connection, ps, rs);
        }
        return null;
    }

    public Integer getProductNumbersOfCartByAccountID(int accountID) {
        try {
            connection = DBContext.getConnection();
            String query = """
                             SELECT COUNT(cd.CartDetailID) AS ProductNumbersOfCart
                              FROM Cart_Detail cd
                              JOIN Cart c ON c.CartID = cd.CartID
                              WHERE c.AccountID = ?
                          """;
            ps = connection.prepareStatement(query);
            ps.setInt(1, accountID);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("ProductNumbersOfCart");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error occurred while get numbers of product of account by account ID", e);
        } finally {
            closeConnection(connection, ps, rs);
        }
        return null;
    }

    public void addBookToCartByBookID(int bookID, int quantity, int cartID) {
        try {
            connection = DBContext.getConnection();
            String query = """
                           INSERT INTO Cart_Detail (BookID, Quantity, CartID)
                             VALUES(?, ?, ?)
                           """;
            ps = connection.prepareStatement(query);
            ps.setInt(1, bookID);
            ps.setInt(2, quantity);
            ps.setInt(3, cartID);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error occurred while add book to cart by book ID", e);
        } finally {
            closeConnection(connection, ps, rs);
        }
    }
    
//      SELECT c.CartID, cd.Quantity,b.Title,b.Price,b.Cover_imagePath, d.DiscountPercent
//  FROM Cart c
//  LEFT JOIN Cart_Detail cd ON cd.CartID = c.CartID
//  JOIN Book b ON b.BookID = cd.BookID
//  LEFT JOIN Discount d ON d.DiscountID = b.DiscountID
//  WHERE AccountID = 2
}

class Test1 {

    public static void main(String[] args) {
        CartAndOrderDAO c = new CartAndOrderDAO();
        System.out.println(c.getProductNumbersOfCartByAccountID(2));
    }
}
