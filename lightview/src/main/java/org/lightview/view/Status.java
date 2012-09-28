package org.lightview.view;

import javafx.beans.property.StringProperty;
import javafx.scene.Node;

/**
 *
 * @author adam-bien.com
 */
import javafx.scene.text.Text;
public class Status {

    private StringProperty messageContent;

    public Status(StringProperty messageContent) {
        this.messageContent = messageContent;
    }
    
    public Node view(){
        Text label = new Text();
        label.textProperty().bind(this.messageContent);
        return label;
    }
}
