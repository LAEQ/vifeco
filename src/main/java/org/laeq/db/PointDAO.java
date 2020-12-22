package org.laeq.db;

import org.laeq.model.Point;

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

    }

    @Override
    public Set<Point> findAll() {
        SortedSet<Point> result = new TreeSet<>();

        String query = "SELECT * FROM POINT";

        return result;
    }

    public int updateOnCollectionChange(Point video) throws SQLException {

        return 0;

//        String query = "DELETE FROM POINT WHERE VIDEO_ID=? AND CATEGORY_ID NOT IN (SELECT CATEGORY_ID FROM CATEGORY_COLLECTION WHERE COLLECTION_ID=?);";
//
//        try(Connection connection = getManager().getConnection();
//        PreparedStatement statement = connection.prepareStatement(query)){
//
//            String ids = StringUtils.join(video.getCollection().getCategoryIds(), ",");
//
//            statement.setInt(1, video.getId());
//            statement.setInt(2, video.getCollection().getId());
//
//            return statement.executeUpdate();
//        }
    }

    public int count() {
        int result = 0;

//        String query = "SELECT count(*) AS total FROM POINT";
//
//        try(Connection connection = getManager().getConnection();
//            PreparedStatement statement = connection.prepareStatement(query)){
//
//            ResultSet queryResult = statement.executeQuery();
//
//            if(queryResult.next()){
//                result = queryResult.getInt("total");
//            }
//
//        } catch (SQLException e){
//            getLogger().error(e.getMessage());
//        }

        return result;
    }

    public int count(Point video) {
        int result = 0;

//        String query = "SELECT count(*) AS total FROM POINT WHERE VIDEO_ID=?";
//
//        try(Connection connection = getManager().getConnection();
//            PreparedStatement statement = connection.prepareStatement(query)){
//
//            statement.setInt(1, video.getId());
//
//            ResultSet queryResult = statement.executeQuery();
//
//            if(queryResult.next()){
//                result = queryResult.getInt("total");
//            }
//
//        } catch (SQLException e){
//            getLogger().error(e.getMessage());
//        }

        return result;
    }

    public SortedSet<Point> findByVideo(Point video){
        String query = "SELECT P.ID, P.X, P.Y, P.START, C.ICON, C.ID AS C_ID, C.NAME AS C_NAME, C.COLOR AS C_COLOR, C.SHORTCUT AS C_SHORTCUT FROM POINT AS P LEFT JOIN CATEGORY AS C ON P.CATEGORY_ID = C.ID WHERE VIDEO_ID = ?  ORDER BY P.START;";

        SortedSet<Point> result = new TreeSet<>();

//        try(Connection connection = getManager().getConnection();
//            PreparedStatement statement = connection.prepareStatement(query)){
//
//            statement.setInt(1, video.getId());
//
//            ResultSet queryResult = statement.executeQuery();
//            result = getResult(queryResult, video);
//
//        } catch (SQLException e){
//            getLogger().error(e.getMessage());
//        }

        return result;
    }

    private SortedSet<Point> getResult(ResultSet datas) throws SQLException {
        SortedSet<Point> result = new TreeSet<>();

//        while(datas.next()){
//            Point point = new Point();
//            point.setId(datas.getInt("ID"));
//            point.setX(datas.getDouble("X"));
//            point.setY(datas.getDouble("Y"));
//            point.setStart(Duration.millis(datas.getDouble("START")));
//            result.add(point);
//        }

        return result;
    }

    private SortedSet<Point> getResult(ResultSet datas, Point video) throws SQLException {
        SortedSet<Point> result = new TreeSet<>();

//        while(datas.next()){
//            Point point = new Point();
//            point.setId(datas.getInt("ID"));
//            point.setX(datas.getDouble("X"));
//            point.setY(datas.getDouble("Y"));
//            point.setStart(Duration.millis(datas.getDouble("START")));
//            Category category = new Category();
//            category.setIcon(datas.getString("ICON"));
//            category.setId(datas.getInt("C_ID"));
//            category.setName(datas.getString("C_NAME"));
//            category.setColor(datas.getString("C_COLOR"));
//            category.setShortcut(datas.getString("C_SHORTCUT"));
//
//            point.setCategory(category);
//            point.setVideo(video);
//
//            result.add(point);
//        }

        return result;
    }

    @Override
    public void delete(Point point) throws DAOException {
        int result = 0;
        String query = "DELETE FROM POINT WHERE ID=?";

//        try(Connection connection = getManager().getConnection();
//            PreparedStatement statement = connection.prepareStatement(query))
//        {
//            statement.setInt(1, point.getId());
//            result = statement.executeUpdate();
//        } catch (SQLException e){
//            getLogger().error(e.getMessage());
//        }
//
//        if(result !=1)
//            throw new DAOException("Error deleting a point.");
    }



    public Point findPrevious(Point point){
        Point result = null;


        return result;
    }

    public Point findNext(Point point){
        Point result = null;


        return result;
    }
}
