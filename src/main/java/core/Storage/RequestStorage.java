package core.Storage;

import burp.BurpExtender;
import burp.IHttpRequestResponse;
import utils.RequestOper;

public class RequestStorage {

    public String  id;
    public String Uri;
    public String Route;
    public String Status;
    public String Size;
    public String Method;
    public String Time;
    public DetailsStorage detailsStorage;
    public RequestStorage(String id, IHttpRequestResponse iHttpRequestResponse, BurpExtender burp) {
//    public RequestStorage(String id, String Uri, String Route, String Status, String Size, String Time, BurpExtender burp) {
        this.id = id;
        this.Uri = RequestOper.getUri(iHttpRequestResponse);
        this.Route = RequestOper.getRoute(burp,iHttpRequestResponse);
        this.Status = RequestOper.getStatus(burp,iHttpRequestResponse);;
        this.Size = iHttpRequestResponse.getResponse().length + "";
        this.Method = burp.helpers.analyzeRequest(iHttpRequestResponse).getMethod();
        this.Time = RequestOper.getTime();
        this.detailsStorage = new DetailsStorage(iHttpRequestResponse);
    }


}
