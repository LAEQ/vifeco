package org.laeq.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public abstract class AbstractDAO {
    private static Logger logger = LoggerFactory.getLogger(AbstractDAO.class.getName());

    @Nonnull private final DatabaseManager manager;

    public AbstractDAO(@Nonnull DatabaseManager manager) {
        this.manager = manager;
    }

    protected Logger getLogger(){
        return logger;
    }

    protected DatabaseManager getManager(){
        return manager;
    }
}
