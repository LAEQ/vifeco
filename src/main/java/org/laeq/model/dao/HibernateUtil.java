package org.laeq.model.dao;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.laeq.model.*;

public class HibernateUtil {
    public StandardServiceRegistry registry;
    public SessionFactory sessionFactory;

    private Configuration configuration(){
        Configuration configuration = new Configuration();

//        configuration.setProperty("connection.driver_class", "org.h2.Driver");
//        configuration.setProperty("connection.url", "jdbc:h2:~/vifeco/db/vifeco");
//        configuration.setProperty("connection.username", "");
//        configuration.setProperty("connection.password", "");
//        configuration.setProperty("connection.pool_size", "1");
//        configuration.setProperty("connection.autocommit","true");
//        configuration.setProperty("dialect","org.hibernate.dialect.H2Dialect");
//        configuration.setProperty("show_sql","false");
//        configuration.setProperty("current_session_context_class","thread");
//        configuration.setProperty("hbm2ddl.auto","update");
//        configuration.setProperty("hibernate.dbcp.initialSize","5");
//        configuration.setProperty("hibernate.dbcp.maxTotal","20");
//        configuration.setProperty("hibernate.dbcp.maxIdle","10");
//        configuration.setProperty("hibernate.dbcp.minIdle","5");
//        configuration.setProperty("hibernate.dbcp.maxWaitMillis","-1");
//
//        configuration.addAnnotatedClass(User.class);
//        configuration.addAnnotatedClass(Category.class);
//        configuration.addAnnotatedClass(Collection.class);
//        configuration.addAnnotatedClass(Video.class);
//        configuration.addAnnotatedClass(Point.class);

        return configuration;
    }

    public HibernateUtil(){
        Configuration config = configuration().configure();
        try{
            registry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
//            MetadataSources sources = new MetadataSources(registry);
//            Metadata metadata = sources.getMetadataBuilder().build();
            sessionFactory = config.buildSessionFactory(registry);

        } catch (Exception e){
            e.printStackTrace();
            if (registry != null){
                StandardServiceRegistryBuilder.destroy(registry);
            }
        }
    }

//    public HibernateUtil(String config){
//        if (sessionFactory == null){
//            try{
//                registry = new StandardServiceRegistryBuilder().configure(config).build();
//
//                MetadataSources sources = new MetadataSources(registry);
//                Metadata metadata = sources.getMetadataBuilder().build();
//                sessionFactory = metadata.getSessionFactoryBuilder().build();
//            } catch (Exception e){
//                e.printStackTrace();
//                if (registry != null){
//                    StandardServiceRegistryBuilder.destroy(registry);
//                }
//            }
//        }
//    }


    public void shutdown(){
        if(registry != null){
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
