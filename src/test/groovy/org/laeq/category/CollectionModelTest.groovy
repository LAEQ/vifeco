package org.laeq.category

import org.laeq.CollectionModel
import spock.lang.Specification

class CollectionModelTest extends Specification {
    CollectionModel model;
    void setup() {
        model = new CollectionModel();
    }

//    def "test entity generation"() {
//        setup:
//        model.setName("Mock collection")
//        model.setIsDefault(true)
//
//        Set<Category> categories = new HashSet<>()
//
//        1.upto(10, {
//            def cat = new Category(it, "name_", "A", "F000000", "icon_")
//            categories.add(cat)
//
//        })
//
//        model.categories(categories)
//
//        when:
//        Collection result = model.generateEntity();
//
//        then:
//        result.name == "Mock collection"
//        result.isDefault == true
//        result.categorySet.size() == 0
//    }
//
//    def "test entity generation with few categories selected"() {
//        setup:
//        model.setName("Mock collection")
//        model.setIsDefault(true)
//
//        Set<Category> categories = new HashSet<>();
//
//        List<Category> selectedCat = []
//
//        1.upto(10, {
//            def cat = new Category(it, "name_", "A", "F00000","icon_")
//            categories.add(cat)
//
//
//            if(it % 2 == 0){
//                selectedCat.add(cat)
//            }
//        })
//
//        selectedCat.each{
//            model.categorySet.forEach{ k, v ->
//                if(v == it) {
//                    k.set(true)
//                }
//            }
//        }
//
//
//        model.categories.addAll(categories)
//
//        when:
//        Collection result = model.generateEntity()
//
//        then:
//        result.name == "Mock collection"
//        result.isDefault == true
//        result.categorySet.size() == 5
//        result.categorySet.collect{it.id} == [2 , 4 , 6, 8, 10]
//    }
}
