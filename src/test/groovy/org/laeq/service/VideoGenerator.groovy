package org.laeq.service

import javafx.util.Duration
import org.laeq.model.*

class VideoGenerator {
    static Video generateVideo(int totalCategory){
        Collection collection = generateCollection(1)

        1.upto(totalCategory, {
            def category = generateCategory(it)
            collection.categories.add(category)
        })

        User user = new User("test", "test", "test@test.com")
        Video video = new Video("path", Duration.millis(60000), collection, user)
        video.id = UUID.randomUUID()

        return video
    }

    static Collection generateCollection(int id){
        Collection collection = new Collection("test")

        return collection
    }


    static Category generateCategory(int id){
        Category category = new Category()
        category.name = "cat_"+ id
        category.id = id

        return category
    }

    static void generatePoints(Video video, int categoryId, double seconds, int total){
        Category category = video.collection.categories.find { it.id == categoryId}
        1.upto(total, {
            Duration start = Duration.seconds(seconds * it)
            Point point = new Point(10.0, 10.0, start, category, video)
            point.id = UUID.randomUUID()
            video.points.add(point)
        })
    }

}
