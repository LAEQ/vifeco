package org.laeq.db;

import org.laeq.model.CategoryCollection;
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

    public static CategoryCollection generateCategoryCollection(ResultSet result) throws SQLException {
        CategoryCollection categoryCollection = new CategoryCollection();

        categoryCollection.setId(result.getInt("CC_ID"));
        categoryCollection.setName(result.getString("CC_NAME"));

        return categoryCollection;
    }
}
