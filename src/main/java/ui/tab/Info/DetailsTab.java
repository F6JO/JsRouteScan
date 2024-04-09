package ui.tab.Info;

import burp.BurpExtender;
import burp.IHttpService;
import burp.IMessageEditor;
import burp.IMessageEditorController;
import core.Storage.DetailsStorage;
import ui.Interface.TabInterface;

import javax.swing.*;
import java.awt.*;

public class DetailsTab implements TabInterface, IMessageEditorController {

    private BurpExtender burp;
    public JSplitPane splitPane;
    public IMessageEditor requestInfo;
    public IMessageEditor responseInfo;

    private DetailsStorage detailsStorage;
    public DetailsTab(BurpExtender burp) {
        this.burp = burp;
        this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        this.requestInfo = this.burp.call.createMessageEditor(this,false);
        this.responseInfo = this.burp.call.createMessageEditor(this,false);
        this.init();
    }

    private void init(){
        this.splitPane.add(this.requestInfo.getComponent());
        this.splitPane.add(this.responseInfo.getComponent());
        this.splitPane.setResizeWeight(0.5);
    }
    public void setDetailsStorage(DetailsStorage detailsStorage){
       this.detailsStorage = detailsStorage;
       this.updateAll();
    }

    public void updateAll(){
        this.requestInfo.setMessage(detailsStorage.request,false);
        this.responseInfo.setMessage(detailsStorage.response,false);
    }


    @Override
    public Component getTab() {
        return this.splitPane;
    }

    @Override
    public IHttpService getHttpService() {
        return this.detailsStorage.httpService;
    }

    @Override
    public byte[] getRequest() {
        return this.detailsStorage.request;
    }

    @Override
    public byte[] getResponse() {
        return this.detailsStorage.response;
    }

    public void clear() {
        this.requestInfo.setMessage(new byte[]{},false);
        this.responseInfo.setMessage(new byte[]{},false);
    }
}
