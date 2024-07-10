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
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    /**
     *
     * @return
     */
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

    /**
     *
     * @param pageNumber
     * @param pageSize
     * @param genreList
     * @param minPrice
     * @param maxPrice
     * @param authorName
     * @param publisherName
     * @return
     */
    public List<Book> getBookListWithFilterSearch(int pageNumber, int pageSize, List<String> genreList, Double minPrice, Double maxPrice, String authorName, String publisherName) {
        List<Book> bookList = new ArrayList<>();
        int offset = (pageNumber - 1) * pageSize;
        try {
            connection = DBContext.getConnection();

            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("""
            SELECT b.*, a.AuthorName, p.PublisherName, p.PublisherImagePath
            FROM Book b
            JOIN Author a ON b.AuthorID = a.AuthorID
            JOIN Publisher p ON p.PublisherID = b.PublisherID
            JOIN Genre g ON g.BookID = b.BookID
            WHERE 1=1
        """);

            if (genreList != null && !genreList.isEmpty()) {
                queryBuilder.append(" AND g.Genre IN (");
                for (int i = 0; i < genreList.size(); i++) {
                    queryBuilder.append("?,");
                }
                queryBuilder.setLength(queryBuilder.length() - 1);
                queryBuilder.append(")");
            }

            if (minPrice != null && maxPrice != null) {
                queryBuilder.append(" AND b.Price BETWEEN ? AND ? ");
            }
            if (authorName != null && !authorName.isEmpty()) {
                queryBuilder.append(" AND a.AuthorName LIKE ? ");
            }

            if (publisherName != null && !publisherName.isEmpty()) {
                queryBuilder.append(" AND p.PublisherName LIKE ? ");
            }

            queryBuilder.append("""
            GROUP BY b.BookID, b.Title, b.ISBN13, b.Publication_date, b.PublisherID, b.Stock, b.Price, b.[Description], b.DiscountID, b.AuthorID, b.Cover_imagePath, a.AuthorName, p.PublisherName, p.PublisherImagePath
             """);
            if (genreList != null && !genreList.isEmpty()) {
                queryBuilder.append("HAVING COUNT(DISTINCT g.Genre) = ? ");
            }
            queryBuilder.append("""
            ORDER BY b.BookID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
        """);

            ps = connection.prepareStatement(queryBuilder.toString());
            int paramIndex = 1;
            if (genreList != null && !genreList.isEmpty()) {
                for (String genre : genreList) {
                    ps.setString(paramIndex++, genre);
                }
            }

            if (minPrice != null && maxPrice != null) {
                ps.setDouble(paramIndex++, minPrice);
                ps.setDouble(paramIndex++, maxPrice);
            }

            if (authorName != null && !authorName.isEmpty()) {
                ps.setString(paramIndex++, "%" + authorName + "%");
            }

            if (publisherName != null && !publisherName.isEmpty()) {
                ps.setString(paramIndex++, "%" + publisherName + "%");
            }
            if (genreList != null && !genreList.isEmpty()) {
                            ps.setInt(paramIndex++, genreList.size());
            }
            ps.setInt(paramIndex++, offset);
            ps.setInt(paramIndex++, pageSize);
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
        List<String> selectedGenres = Arrays.asList("Fantasy", "Mystery");
        System.out.println(bd.getBookListWithFilterSearch(1, 6, selectedGenres, 10.0, 20.0, null, null));
        System.out.println(bd.getBookListWithFilterSearch(1, 6, selectedGenres, null, null, null, null));
    }
}
