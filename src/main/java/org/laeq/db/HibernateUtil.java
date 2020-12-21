package org.laeq.db;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
    public StandardServiceRegistry registry;
    public SessionFactory sessionFactory;

    public HibernateUtil(String config){
        if (sessionFactory == null){
            try{
                registry = new StandardServiceRegistryBuilder().configure(config).build();

                MetadataSources sources = new MetadataSources(registry);
                Metadata metadata = sources.getMetadataBuilder().build();
                sessionFactory = metadata.getSessionFactoryBuilder().build();
            } catch (Exception e){
                e.printStackTrace();
                if (registry != null){
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
    }


    public void shutdown(){
        if(registry != null){
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
