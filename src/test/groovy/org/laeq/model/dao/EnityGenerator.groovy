package org.laeq.model.dao

import org.laeq.model.Category
import org.laeq.model.Point
import org.laeq.model.User
import org.laeq.model.Collection
import org.laeq.model.Video

import java.time.Duration

class EntityGenerator {
    static def createUser(){
        return new User("mock first name", "mock last name", "mock@email.com")
    }

    static def createCategory(String shortcut){
        return new Category("mock name", "mock path", "#FF1234", shortcut)
    }

    static def createCollection(){
        def collection = new Collection("mock name")
        collection.addCategory(createCategory("A"))
        collection.addCategory(createCategory("B"))

        return collection
    }

    static def createVideo(){
        def video = new Video("mock/path", Duration.ofMillis(1000), createCollection(), createUser())

        return video
    }

    static def createPoint(Video video){
        Point point = new Point(Math.round(Math.random() * 100),
                Math.round(Math.random() * 100),
                Duration.ofMillis(Math.random() * 10000),
                video.collection.getCategories().getAt(0), video)

        return point
    }
}
