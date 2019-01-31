package org.laeq.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Nonnull;

public abstract class AbstractDAO {
    private Logger logger;

    @Nonnull
    private final DatabaseManager manager;

    public AbstractDAO(@Nonnull DatabaseManager manager) {
        this.manager = manager;
        logger = LoggerFactory.getLogger(this.getClass().getName());
    }

    abstract public Integer getNextValue();

    protected Logger getLogger(){
        return logger;
    }

    protected DatabaseManager getManager(){
        return manager;
    }
}
