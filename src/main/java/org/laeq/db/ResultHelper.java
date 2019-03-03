package org.laeq.db;

import org.laeq.model.Collection;
import org.laeq.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultHelper {

    public static User generateUser(ResultSet result) throws SQLException {
        User user = new User();

        user.setId(result.getInt("USER_ID"));
        user.setFirstName(result.getString("FIRST_NAME"));
        user.setLastName(result.getString("LAST_NAME"));

        return user;
    }

    public static Collection generateCategoryCollection(ResultSet result) throws SQLException {
        Collection collection = new Collection();

        collection.setId(result.getInt("CC_ID"));
        collection.setName(result.getString("CC_NAME"));

        return collection;
    }
}
