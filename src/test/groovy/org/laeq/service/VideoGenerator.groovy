package org.laeq.service

import javafx.util.Duration
import org.laeq.model.Category
import org.laeq.model.Collection
import org.laeq.model.Point
import org.laeq.model.User
import org.laeq.model.Video


class VideoGenerator {

    static Video generateVideo(){
        Collection collection = generateCollection(1)

        1.upto(10, {
            def category = generateCategory(it)
            collection.categorySet.add(category)
        })

        User user = new User(1, "test", "test", "test@test.com")
        Video video = new Video("path", Duration.millis(10000), user, collection)

        return video
    }

    static Collection generateCollection(int id){
        Collection collection = new Collection(id, "test", false)

        return collection
    }


    static Category generateCategory(int id){
        Category category = new Category()
        category.id = id

        return category
    }

    static void generatePoints(Video video, int id){
        1.upto(5, {
            Category category = video1.collection.categorySet.find { it.id = id}
            Duration start = Duration.millis(it)
            Point point = new Point(10, 10, start, video1, category)
            video.pointSet.add(point)
        })
    }

}
