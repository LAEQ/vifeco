package org.laeq.db;

import javafx.util.Duration;
import org.laeq.model.Category;
import org.laeq.model.Point;
import org.laeq.model.Video;

import javax.annotation.Nonnull;
import java.sql.*;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class PointDAO extends AbstractDAO implements DAOInterface<Point> {
    public static String sequence_name = "point_id";

    public PointDAO(@Nonnull DatabaseManager manager, @Nonnull String sequenceName) {
        super(manager, sequenceName);
    }

    @Override
    public void insert(Point point) throws DAOException {
        int result = 0;

        String query = "INSERT INTO POINT(X, Y, VIDEO_ID, CATEGORY_ID, START) VALUES(?, ?, ?, ?, ?);";
        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
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

    public SortedSet<Point> findByVideo(Video video){
        String query = "SELECT P.ID, P.X, P.Y, P.START, C.ICON, C.ID AS C_ID, C.NAME AS C_NAME, C.COLOR AS C_COLOR FROM POINT AS P LEFT JOIN CATEGORY AS C ON P.CATEGORY_ID = C.ID WHERE VIDEO_ID = ?  ORDER BY P.START;";

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
        } catch (Exception e){
            getLogger().error(e.getMessage());
        }

        if(result !=1)
            throw new DAOException("Error deleting a point");
    }
}
