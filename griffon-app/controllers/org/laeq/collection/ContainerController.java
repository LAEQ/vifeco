package org.laeq.collection;

import griffon.core.artifact.GriffonController;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.laeq.CRUDController;
import org.laeq.db.CollectionDAO;
import org.laeq.db.CategoryDAO;
import org.laeq.db.DAOException;
import org.laeq.model.Collection;
import org.laeq.service.MariaService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class ContainerController extends CRUDController<Collection> {
    @Inject private MariaService dbService;
    private CollectionDAO collectionDAO;
    private CategoryDAO categoryDAO;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        collectionDAO = dbService.getCollectionDAO();
        categoryDAO = dbService.getCategoryDAO();
        model.getCollections().addAll(collectionDAO.findAll());
        model.addCategories(categoryDAO.findAll());

        view.initForm();
    }

    @Threading(Threading.Policy.OUTSIDE_UITHREAD)
    public void save(){
        if(model.valid()){
            Collection collection = model.generateCollection();

            try {
                collectionDAO.save(collection);
                model.update(collection);
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

    public void delete(Collection collection) {
        runInsideUISync(() -> {
            if(confirm("org.laeq.model.collection.delete.confirmation")){
                try {
                    collectionDAO.delete(collection);
                    this.model.delete(collection);
                } catch (DAOException e) {
                    alert("org.laeq.dao.error", String.format("Cannot delete %s", collection));
                }
            }
        });
    }
}