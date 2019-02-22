package org.laeq.collection;

import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import griffon.transform.Threading;
import org.laeq.db.CategoryCollectionDAO;
import org.laeq.db.CategoryDAO;
import org.laeq.db.DatabaseService;
import org.laeq.model.CategoryCollection;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class ContainerController extends AbstractGriffonController {
    @MVCMember @Nonnull private ContainerModel model;

    @Inject
    private DatabaseService dbService;
    @Inject private DialogService dialogService;

    private CategoryCollectionDAO dao;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        dao = dbService.getCategoryCollectionDAO();
        model.getCollections().addAll(dao.findAll());
    }
}