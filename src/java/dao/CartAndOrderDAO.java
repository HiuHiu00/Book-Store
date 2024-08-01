package dao;

import dal.DBContext;
import entity.Book;
import entity.Cart;
import entity.Cart_Detail;
import entity.Discount;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    public void removeBookFromCartByBookID(int bookID) {
        try {
            connection = DBContext.getConnection();
            String query = """
                           Delete From Cart_Detail WHERE BookID = ?
                           """;
            ps = connection.prepareStatement(query);
            ps.setInt(1, bookID);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error occurred while remove book from cart by book ID", e);
        } finally {
            closeConnection(connection, ps, rs);
        }
    }

    public List<Cart> getCartListByAccountID(int accountID) {
        List<Cart> cartList = new ArrayList<>();
        try {
            connection = DBContext.getConnection();
            String query = """
                                 SELECT c.CartID, cd.Quantity,b.BookID, b.Title,b.Price,b.Cover_imagePath, d.DiscountPercent
                             FROM Cart c
                             LEFT JOIN Cart_Detail cd ON cd.CartID = c.CartID
                             JOIN Book b ON b.BookID = cd.BookID
                             LEFT JOIN Discount d ON d.DiscountID = b.DiscountID
                             WHERE AccountID = ?
                           """;
            ps = connection.prepareStatement(query);
            ps.setInt(1, accountID);
            rs = ps.executeQuery();
            while (rs.next()) {
                Discount discount = Discount.builder()
                        .DiscountPercent(rs.getDouble("DiscountPercent"))
                        .build();
                Book book = Book.builder()
                        .BookID(rs.getInt("BookID"))
                        .Title(rs.getString("Title"))
                        .Price(rs.getDouble("Price"))
                        .Cover_imagePath(rs.getString("Cover_imagePath"))
                        .build();
                Cart_Detail cartDetail = Cart_Detail.builder()
                        .Quantity(rs.getInt("Quantity"))
                        .build();
                Cart cart = Cart.builder()
                        .CartID(rs.getInt("CartID"))
                        .cartDetail(cartDetail)
                        .book(book)
                        .discount(discount)
                        .build();
                cartList.add(cart);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error occurred while get all book list", e);
        } finally {
            closeConnection(connection, ps, rs);
        }
        return cartList;
    }

    public boolean isBookExistedInCart(int bookID, int cartID) {
        try {
            connection = DBContext.getConnection();
            String query = "SELECT * FROM Cart_Detail WHERE BookID = ? AND CartID = ? ";
            ps = connection.prepareStatement(query);
            ps.setInt(1, bookID);
            ps.setInt(2, cartID);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error occurred while checking book existed int cart by bookID and cartID by", e);
            return false;
        } finally {
            closeConnection(connection, ps, rs);
        }
    }
    
    public void increaseBookQuantityByOne(int bookID, int cartID, int quantity) {
        try {
            connection = DBContext.getConnection();
            String query = "UPDATE Cart_Detail SET Quantity = Quantity + ? WHERE  BookID = ? AND CartID = ? ";
            ps = connection.prepareStatement(query);
            ps.setInt(1, quantity);
            ps.setInt(2, bookID);
            ps.setInt(3, cartID);
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error occurred while update book quantity of cart", e);
        } finally {
            closeConnection(connection, ps, rs);
        }
    }
}


class Test1 {

    public static void main(String[] args) {
        CartAndOrderDAO c = new CartAndOrderDAO();
        List<Cart> ss = c.getCartListByAccountID(2);
        for (Cart s : ss) {
            System.out.println(s.getBook().getBookID());
            System.out.println("============================");
        }
    }
}
