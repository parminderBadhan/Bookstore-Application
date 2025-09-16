package bookstoreapp;

import bookstoreapp.entity.Book;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;

public class CheckBoxTableCell extends TableCell<Book, Boolean> {
    private final CheckBox checkBox = new CheckBox();
    
    public CheckBoxTableCell() {
        checkBox.setOnAction(e -> {
            Book book = getTableView().getItems().get(getIndex());
            book.setSelected(checkBox.isSelected());
        });
    }
    
    @Override
    protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        
        if (empty || item == null) {
            setGraphic(null);
        } else {
            Book book = getTableView().getItems().get(getIndex());
            checkBox.setSelected(book.isSelected());
            setGraphic(checkBox);
        }
    }
}