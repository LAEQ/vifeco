package org.laeq.model.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.laeq.model.Point;
import org.laeq.model.Video;

import java.util.List;
import java.util.UUID;

public class VideoDAO extends AbstractDAO<Video> {
    public VideoDAO(HibernateUtil hib) {
        super(hib);
    }

    @Override
    public Boolean create(Video video){
        return super.saveOrUpdate(video);
    }

    @Override
    public Boolean delete(Video video) {
        Boolean result = true;
        Session currentSession = this.hib.sessionFactory.openSession();
        Transaction tx = currentSession.beginTransaction();
        try {
            for(Point pt : video.getPoints()){
                currentSession.delete(pt);
            }
            currentSession.delete(video);
            tx.commit();
        } catch (Exception e) {
            result = Boolean.FALSE;
            tx.rollback();
            e.printStackTrace();
        } finally {
            currentSession.close();
        }

        return result;
    }

    @Override
    public List<Video> findAll() throws Exception {
        return super.findAll(Video.class);
    }

    @Override
    public Video findOneById(int id) throws Exception {
        return super.findById(id, Video.class);
    }

    public Video findOneByUUID(UUID id) throws Exception {
        Video result = null;
        try{
            startOperation();
            result = session.get(Video.class, id);
        } finally {
            session.close();
        }

        return result;
    }
}
