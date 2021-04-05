package org.laeq.model.dao;

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
        return super.delete(video);
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
