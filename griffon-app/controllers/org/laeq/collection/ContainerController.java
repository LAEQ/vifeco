package org.laeq.collection;

import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.CRUDController;
import org.laeq.db.CategoryCollectionDAO;
import org.laeq.db.CategoryDAO;
import org.laeq.db.DAOException;
import org.laeq.db.DatabaseService;
import org.laeq.model.CategoryCollection;
import org.laeq.model.User;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class ContainerController extends CRUDController<CategoryCollection> {

    private CategoryCollectionDAO collectionDAO;
    private CategoryDAO categoryDAO;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        collectionDAO = dbService.getCategoryCollectionDAO();
        categoryDAO = dbService.getCategoryDAO();
        model.getCollections().addAll(collectionDAO.findAll());
        model.addCategories(categoryDAO.findAll());

        view.initForm();
    }

    @Threading(Threading.Policy.OUTSIDE_UITHREAD)
    public void save(){
        if(model.valid()){
            CategoryCollection categoryCollection = model.generateCollection();

            try {
                collectionDAO.save(categoryCollection);
                model.update(categoryCollection);
            } catch (DAOException e) {
                alert("key.to_implement", e.getMessage());
            }

        } else {
            System.out.println(model.getErrors());
            alert("org.laeq.model.collection.form.alert.title", model.getErrors());
        }
    }

    @Threading(Threading.Policy.OUTSIDE_UITHREAD)
    public void clear(){
        model.clearForm();
    }

    public void delete(CategoryCollection categoryCollection) {
        runInsideUISync(() -> {
            if(confirm("org.laeq.model.collection.delete.confirmation")){
                try {
                    collectionDAO.delete(categoryCollection);
                    this.model.delete(categoryCollection);
                } catch (DAOException e) {
                    alert("org.laeq.dao.error", String.format("Cannot delete %s", categoryCollection));
                }
            }
        });
    }
}