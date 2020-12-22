package org.laeq.model.dao;

import org.laeq.db.HibernateUtil;
import org.laeq.model.Point;
import org.laeq.model.Video;

import java.util.List;

public class VideoDAO extends AbstractDAO<Video> {
    public VideoDAO(HibernateUtil hib) {
        super(hib);
    }

    @Override
    public void create(Video video) {
        super.saveOrUpdate(video);
    }

    @Override
    public void delete(Video video){
        super.delete(video);
    }

    @Override
    public List<Video> findAll(){
        return super.findAll(Video.class);
    }
}
