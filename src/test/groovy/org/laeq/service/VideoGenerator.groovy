package org.laeq.service

import javafx.util.Duration
import org.laeq.model.*

class VideoGenerator {
    static Point generateVideo(int videoId, int totalCategory){
        Collection collection = generateCollection(1)

        1.upto(totalCategory, {
            def category = generateCategory(it)
            collection.categorySet.add(category)
        })

        User user = new User(1, "test", "test", "test@test.com")
        Point video = new Point(videoId,"path", Duration.millis(10000), user, collection)

        return video
    }

    static Collection generateCollection(int id){
        Collection collection = new Collection(id, "test", false)

        return collection
    }


    static Category generateCategory(int id){
        Category category = new Category()
        category.name = "cat_"+ id
        category.id = id

        return category
    }

    static void generatePoints(Point video, int categoryId, int seconds, int total, int pointId){
        Category category = video.collection.categorySet.find { it.id == categoryId}
        1.upto(total, {
            Duration start = Duration.seconds(seconds + it)
            Point point = new Point(pointId++,10, 10, start, video, category)
            video.pointSet.add(point)
        })
    }

}
