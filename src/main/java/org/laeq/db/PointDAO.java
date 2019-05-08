package org.laeq.db;

import javafx.util.Duration;
import org.apache.commons.lang3.StringUtils;
import org.laeq.model.Category;
import org.laeq.model.Point;
import org.laeq.model.Video;

import javax.annotation.Nonnull;
import java.sql.*;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class PointDAO extends AbstractDAO implements DAOInterface<Point> {
    private String insertQuery = "INSERT INTO POINT(X, Y, VIDEO_ID, CATEGORY_ID, START) VALUES(?, ?, ?, ?, ?);";

    public PointDAO(@Nonnull DatabaseManager manager) {
        super(manager);
    }

    @Override
    public void insert(Point point) throws DAOException {
        int result = 0;

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS))
        {
            statement.setDouble(1, point.getX());
            statement.setDouble(2, point.getY());
            statement.setInt(3, point.getVideo().getId());
            statement.setInt(4, point.getCategory().getId());
            statement.setDouble(5, point.getStart().toMillis());

            result = statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();

            if(keys.next()){
                point.setId(keys.getInt(1));
            }

        } catch (Exception e){
            getLogger().error(String.format("%s. %s", point, e.getMessage()));
        }

        if(result != 1)
            throw new DAOException("PointDAO: cannot create point.");
    }

    @Override
    public Set<Point> findAll() {
        SortedSet<Point> result = new TreeSet<>();

        return result;
    }

    public int updateOnCollectionChange(Video video) throws SQLException {

        String query = "DELETE FROM POINT WHERE VIDEO_ID=? AND CATEGORY_ID NOT IN (SELECT CATEGORY_ID FROM CATEGORY_COLLECTION WHERE COLLECTION_ID=?);";

        try(Connection connection = getManager().getConnection();
        PreparedStatement statement = connection.prepareStatement(query)){

            String ids = StringUtils.join(video.getCollection().getCategoryIds(), ",");

            statement.setInt(1, video.getId());
            statement.setInt(2, video.getCollection().getId());

            return statement.executeUpdate();
        }
    }

    public int count() {
        int result = 0;

        String query = "SELECT count(*) AS total FROM POINT";

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){

            ResultSet queryResult = statement.executeQuery();

            if(queryResult.next()){
                result = queryResult.getInt("total");
            }

        } catch (SQLException e){
            getLogger().error(e.getMessage());
        }

        return result;
    }

    public int count(Video video) {
        int result = 0;

        String query = "SELECT count(*) AS total FROM POINT WHERE VIDEO_ID=?";

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){

            statement.setInt(1, video.getId());

            ResultSet queryResult = statement.executeQuery();

            if(queryResult.next()){
                result = queryResult.getInt("total");
            }

        } catch (SQLException e){
            getLogger().error(e.getMessage());
        }

        return result;
    }

    public SortedSet<Point> findByVideo(Video video){
        String query = "SELECT P.ID, P.X, P.Y, P.START, C.ICON, C.ID AS C_ID, C.NAME AS C_NAME, C.COLOR AS C_COLOR, C.SHORTCUT AS C_SHORTCUT FROM POINT AS P LEFT JOIN CATEGORY AS C ON P.CATEGORY_ID = C.ID WHERE VIDEO_ID = ?  ORDER BY P.START;";

        SortedSet<Point> result = new TreeSet<>();

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){

            statement.setInt(1, video.getId());

            ResultSet queryResult = statement.executeQuery();
            result = getResult(queryResult, video);

        } catch (SQLException e){
            getLogger().error(e.getMessage());
        }

        return result;
    }

    private SortedSet<Point> getResult(ResultSet datas) throws SQLException {
        SortedSet<Point> result = new TreeSet<>();

        while(datas.next()){
            Point point = new Point();
            point.setId(datas.getInt("ID"));
            point.setX(datas.getDouble("X"));
            point.setY(datas.getDouble("Y"));
            point.setStart(Duration.millis(datas.getDouble("START")));
            result.add(point);
        }

        return result;
    }

    private SortedSet<Point> getResult(ResultSet datas, Video video) throws SQLException {
        SortedSet<Point> result = new TreeSet<>();

        while(datas.next()){
            Point point = new Point();
            point.setId(datas.getInt("ID"));
            point.setX(datas.getDouble("X"));
            point.setY(datas.getDouble("Y"));
            point.setStart(Duration.millis(datas.getDouble("START")));
            Category category = new Category();
            category.setIcon(datas.getString("ICON"));
            category.setId(datas.getInt("C_ID"));
            category.setName(datas.getString("C_NAME"));
            category.setColor(datas.getString("C_COLOR"));
            category.setShortcut(datas.getString("C_SHORTCUT"));

            point.setCategory(category);
            point.setVideo(video);

            result.add(point);
        }

        return result;
    }

    @Override
    public void delete(Point point) throws DAOException {
        int result = 0;
        String query = "DELETE FROM POINT WHERE ID=?";

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setInt(1, point.getId());
            result = statement.executeUpdate();
        } catch (SQLException e){
            getLogger().error(e.getMessage());
        }

        if(result !=1)
            throw new DAOException("Error deleting a point.");
    }

    public void insert(Video video) throws SQLException {
        try(Connection connection = getManager().getConnection()){
            PreparedStatement stmt = connection.prepareStatement(insertQuery);

            connection.setAutoCommit(false);

            for (Point point : video.getPointSet()) {
                stmt.setDouble(1, point.getX());
                stmt.setDouble(2, point.getY());
                stmt.setInt(3, video.getId());
                stmt.setInt(4, point.getCategory().getId());
                stmt.setDouble(5, point.getStart().toMillis());
                stmt.addBatch();
            }

            stmt.executeBatch();

            connection.commit();
        }
    }

    public Point findPrevious(Point point){
        Point result = null;
        String query = "SELECT START FROM POINT WHERE VIDEO_ID=? AND CATEGORY_ID = ? and `START` < ? ORDER BY `START` DESC LIMIT 1";

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){

            statement.setInt(1, point.getVideo().getId());
            statement.setInt(2, point.getCategoryId());
            statement.setDouble(3, point.getStartDouble());

            ResultSet queryResult = statement.executeQuery();

            if(queryResult.next()){
                Double startDouble = queryResult.getDouble(1);
                result.setStart(Duration.millis(startDouble));
            }

        } catch (SQLException e){
            getLogger().error(e.getMessage());
        }

        return result;
    }
}
