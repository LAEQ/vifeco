package org.laeq.db;

import org.laeq.model.*;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VideoUserDAO extends AbstractDAO {

    public VideoUserDAO(@Nonnull DatabaseManager manager, @Nonnull String sequenceName) {
        super(manager, sequenceName);
    }


    public List<VideoUser> findAll() {
        String query = "SELECT V.CATEGORY_COLLECTION_ID as CAT_COL_ID, count(P.VIDEO_ID) AS TOTAL, V.ID AS VIDEO_ID, MAX(P.START) AS LAST, U.FIRST_NAME AS FIRST_NAME , U.LAST_NAME AS LAST_NAME, U.ID AS USER_ID, V.PATH, V.DURATION from POINT AS P LEFT JOIN USER AS U ON P.USER_ID = U.ID full join VIDEO AS V on P.video_id = V.id GROUP BY P.VIDEO_ID, P.USER_ID, V.PATH, V.DURATION, V.ID, U.FIRST_NAME, U.LAST_NAME, U.ID ORDER BY V.ID, U.ID, V.CATEGORY_COLLECTION_ID";

        List<VideoUser> result = new ArrayList<>();

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);){

            ResultSet queryResult = statement.executeQuery();

            result = getResult(queryResult);

        } catch (SQLException e){
            getLogger().error(e.getMessage());
        }

        return result;
    }

    private List<VideoUser> getResult(ResultSet datas) throws SQLException {
        List<VideoUser> result = new ArrayList<>();

        while(datas.next()){
            VideoUser videoUser = new VideoUser();
            Video video = new Video();
            video.setPath(datas.getString("PATH"));
            video.setDuration(datas.getDouble("DURATION"));
            video.setId(datas.getInt("VIDEO_ID"));

            CategoryCollection categoryCollection = new CategoryCollection();
            categoryCollection.setId(datas.getInt("CAT_COL_ID"));
            video.setCategoryCollection(categoryCollection);

            videoUser.setVideo(video);

            User user = new User();
            user.setId(datas.getInt("USER_ID"));
            user.setFirstName(datas.getString("FIRST_NAME"));
            user.setLastName(datas.getString("LAST_NAME"));

            videoUser.setUser(user);


            videoUser.setTotal(datas.getInt("TOTAL"));
            videoUser.setLast(datas.getDouble("LAST"));


            result.add(videoUser);
        }

        return result;
    }
}
