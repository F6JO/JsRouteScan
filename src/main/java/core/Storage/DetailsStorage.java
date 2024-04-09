package core.Storage;


import burp.*;

public class DetailsStorage {

//    private BurpExtender burp;
    public byte[] request;
    public byte[] response;
    public IHttpService httpService;
    public DetailsStorage(IHttpRequestResponse iHttpRequestResponse){
//        this.burp = burp;
        this.request = iHttpRequestResponse.getRequest();
        this.response = iHttpRequestResponse.getResponse();
        this.httpService = iHttpRequestResponse.getHttpService();

    }


}
