package org.laeq.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Nonnull;
import java.sql.*;

public abstract class AbstractDAO {
    private Logger logger;

    @Nonnull
    private String sequenceName;

    @Nonnull
    private final DatabaseManager manager;

    public AbstractDAO(@Nonnull DatabaseManager manager, @Nonnull String sequenceName) {
        this.sequenceName = sequenceName;
        this.manager = manager;
        logger = LoggerFactory.getLogger(this.getClass().getName());
    }

    /**
     * Retrieve the next id (primaray key) from the database
     *
     * @return nextId
     */
    public Integer getNextValue(){
        Integer nextID = null;
        String query = String.format("CALL NEXT VALUE for %s;", sequenceName);


        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
        {
            CallableStatement call = connection.prepareCall(query);
            ResultSet result = call.executeQuery();

            if(result.next()){
                return result.getInt(1);
            }

        } catch (Exception e) {
            getLogger().error(e.getMessage());
        }

        return nextID;
    }

    protected Logger getLogger(){
        return logger;
    }

    protected DatabaseManager getManager(){
        return manager;
    }
}
