package dao;

import dal.DBContext;
import entity.Author;
import entity.Book;
import entity.Genre;
import entity.Publisher;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class BrowseDAO {

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

    public List<Book> getBookList() {
        List<Book> bookList = new ArrayList<>();
        try {
            connection = DBContext.getConnection();
            String query = """
                           SELECT  b.*, a.AuthorName, p.PublisherName, p.PublisherImagePath
                           FROM Book b JOIN Author a ON b.AuthorID=a.AuthorID
                           JOIN Publisher p ON p.PublisherID = b.PublisherID""";
            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Author author = Author.builder()
                        .AuthorName(rs.getString("AuthorName"))
                        .build();
                Publisher publisher = Publisher.builder()
                        .PublisherName(rs.getString("PublisherName"))
                        .PublisherImagePath(rs.getString("PublisherImagePath"))
                        .build();
                Book book = Book.builder()
                        .BookID(rs.getInt("BookID"))
                        .Title(rs.getString("Title"))
                        .ISBN13(rs.getString("ISBN13"))
                        .Publication_date(rs.getDate("Publication_date"))
                        .Stock(rs.getInt("Stock"))
                        .Price(rs.getDouble("Price"))
                        .Description(rs.getString("Description"))
                        .Cover_imagePath(rs.getString("Cover_imagePath"))
                        .author(author)
                        .publisher(publisher)
                        .build();
                bookList.add(book);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            closeConnection(connection, ps, rs);
        }
        return bookList;
    }

    public List<Book> getBookListWithGenreFilter(List<String> genreList) {
        List<Book> bookList = new ArrayList<>();
        try {
            connection = DBContext.getConnection();
            StringBuilder placeholders = new StringBuilder();
            for (int i = 0; i < genreList.size(); i++) {
                placeholders.append("?");
                if (i < genreList.size() - 1) {
                    placeholders.append(", ");
                }
            }

            String query = """
                        SELECT b.*, a.AuthorName, p.PublisherName, p.PublisherImagePath
                        FROM Book b
                        JOIN Author a ON b.AuthorID = a.AuthorID
                        JOIN Publisher p ON p.PublisherID = b.PublisherID
                        JOIN Genre g ON g.BookID = b.BookID
                        WHERE g.Genre IN (""" + placeholders.toString() + """
                )GROUP BY b.BookID, b.Title, b.ISBN13, b.Publication_date, b.PublisherID, b.Stock, b.Price, b.[Description], b.DiscountID, b.AuthorID, b.Cover_imagePath, a.AuthorName, p.PublisherName, p.PublisherImagePath""";

            ps = connection.prepareStatement(query);
            for (int i = 0; i < genreList.size(); i++) {
                ps.setString(i + 1, genreList.get(i));
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                Author author = Author.builder()
                        .AuthorName(rs.getString("AuthorName"))
                        .build();
                Publisher publisher = Publisher.builder()
                        .PublisherName(rs.getString("PublisherName"))
                        .PublisherImagePath(rs.getString("PublisherImagePath"))
                        .build();
                Book book = Book.builder()
                        .BookID(rs.getInt("BookID"))
                        .Title(rs.getString("Title"))
                        .ISBN13(rs.getString("ISBN13"))
                        .Publication_date(rs.getDate("Publication_date"))
                        .Stock(rs.getInt("Stock"))
                        .Price(rs.getDouble("Price"))
                        .Description(rs.getString("Description"))
                        .Cover_imagePath(rs.getString("Cover_imagePath"))
                        .author(author)
                        .publisher(publisher)
                        .build();
                bookList.add(book);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            closeConnection(connection, ps, rs);
        }
        return bookList;
    }

}

class Test {

    public static void main(String[] args) {
        BrowseDAO bd = new BrowseDAO();
        System.out.println(bd.getBookList());
    }
}
