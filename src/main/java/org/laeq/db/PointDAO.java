package org.laeq.db;

import javafx.util.Duration;
import org.laeq.model.Category;
import org.laeq.model.Point;
import org.laeq.model.User;
import org.laeq.model.Video;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class PointDAO extends AbstractDAO implements DAOInterface<Point> {

    public PointDAO(@Nonnull DatabaseManager manager, @Nonnull String sequenceName) {
        super(manager, sequenceName);
    }

    @Override
    public void insert(Point data) throws DAOException {

    }

    @Override
    public Set<Point> findAll() {
        SortedSet<Point> result = new TreeSet<>();

        return result;
    }

    public SortedSet<Point> findByVideoAndUser(Video video, User user){
        String query = "SELECT P.ID, P.X, P.Y, P.START, C.ICON  FROM POINT AS P LEFT JOIN CATEGORY AS C ON P.CATEGORY_ID = C.ID WHERE VIDEO_ID = ? AND USER_ID = ? ORDER BY P.START;";

        SortedSet<Point> result = new TreeSet<>();

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){

            statement.setInt(1, video.getId());
            statement.setInt(2, user.getId());

            ResultSet queryResult = statement.executeQuery();
            result = getResult(queryResult);

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
            Category category = new Category();
            category.setIcon(datas.getString("ICON"));

            point.setCategory(category);

            result.add(point);
        }

        return result;
    }

    @Override
    public void delete(Point data) throws DAOException {



    }
}
