package org.laeq.video


import javafx.util.Duration
import org.laeq.model.*
import spock.lang.Specification

class CategoryModelTest extends Specification {
    def "GetTotalCount"() {
        given: "A number of categories"

        String[] c = ["a", "b", "c", "d"]
        Set<Category> set = new HashSet<>()

        Collection collection = new Collection(1, "test", false)

        Video video = new Video(1, "path", Duration.millis(1000), new User(1, "test", "test", "test"), collection)

        for (int i = 0; i < c.length; i++) {
            set.add(new Category(i + 1, c[i].toString(), c[i].toString(), "F000000", c[i]))
        }

        collection.categorySet.addAll(set)

        Point point1 = new Point(1, 1, 1,Duration.millis(1000), video, set.find{it.id == 1})
        Point point2 = new Point(2, 1, 1,Duration.millis(1000), video, set.find{it.id == 1})
        Point point3 = new Point(3, 1, 1,Duration.millis(1000), video, set.find{it.id == 1})
        Point point4 = new Point(4, 1, 1,Duration.millis(1000), video, set.find{it.id == 2})

        when:
        CategoryModel model = new CategoryModel()
        model.video = video
        model.generateProperties()
        model.addPoint(point1)
        model.addPoint(point2)
        model.addPoint(point3)
        model.addPoint(point4)


        then:
        model.categorySet.size() == c.length
        model.getCategoryProperty(set.find {it.id == 1}) != null
        model.getCategoryProperty(set.find {it.id == 1}).get() == 3
        model.getCategoryProperty(set.find {it.id == 2}).get() == 1
    }
}
