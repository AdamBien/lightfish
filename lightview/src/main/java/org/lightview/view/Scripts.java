package org.lightview.view;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.lightview.presenter.EscalationsPresenterBindings;
import org.lightview.presenter.ScriptsPresenter;

/**
 *
 * @author adam bien, adam-bien.com
 */
public class Scripts {
    
    private FXMLLoader loader;
    private ScriptsPresenter presenter;
    private final AnchorPane root;
    private EscalationsPresenterBindings bindings;
    private Stage dialogStage;

    public Scripts(EscalationsPresenterBindings bindings) {
        this.bindings = bindings;
        this.loader = new FXMLLoader(Scripts.class.getResource("scripts.fxml"));
        try {
            this.loader.load();
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot load scripts.fxml");
        }
        this.root = this.loader.getRoot();
        this.presenter = this.loader.getController();
        this.presenter.setBindings(bindings);
    }
    
    
    
    public void createView(){
        this.dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(root);

        dialogStage.setScene(scene);
        dialogStage.setTitle("New Script");
        dialogStage.centerOnScreen();
        dialogStage.show();
    }

    public void close() {
        this.dialogStage.close();
    }

}
