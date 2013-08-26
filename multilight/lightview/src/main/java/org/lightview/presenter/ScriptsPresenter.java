/*
Copyright 2012 Adam Bien, adam-bien.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.lightview.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 *
 * @author adam bien, adam-bien.com
 */
public class ScriptsPresenter {
    
    private EscalationsPresenterBindings bindings;
    
    @FXML
    private TextField name;
    
    @FXML
    private TextArea content;
    

    public void setBindings(EscalationsPresenterBindings bindings) {
        this.bindings = bindings;
    }

    public void save(){
        String nameTxt = name.getText();
        String contentTxt = content.getText();
        this.bindings.newScript(nameTxt, contentTxt);
    }
    
    public void cancel(){

    }
    
}
