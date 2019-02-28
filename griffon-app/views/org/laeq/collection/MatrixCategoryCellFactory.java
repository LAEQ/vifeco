package org.laeq.collection;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class MatrixCategoryCellFactory implements Callback<TableColumn, TableCell> {
    /**
     * The <code>call</code> method is called when required, and is given a
     * single argument of type P, with a requirement that an object of type R
     * is returned.
     *
     * @param param The single argument upon which the returned value should be
     *              determined.
     * @return An object of type R that may be determined based on the provided
     * parameter value.
     */
    @Override
    public TableCell call(TableColumn param) {
        TableCell<Object, Boolean> cell = new TableCell<Object, Boolean>(){

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);

                System.out.println(item + ":" + empty);

                if(empty){

                } else {
                    System.out.println(item);
                }

            }
        };

        return cell;
    }
}
