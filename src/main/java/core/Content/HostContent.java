package core.Content;

import burp.IHttpService;
import core.Interface.ContentClass;
import core.Storage.RequestStorage;

import java.util.ArrayList;
import java.util.List;

public class HostContent implements ContentClass {

    public String host;
    private List<String> headers;
    public ArrayList<RouteContent> routeContents;
    public ArrayList<RequestStorage> requestStorages;
    private IHttpService httpService;

    public HostContent(String host) {
        this.host = host;
        this.requestStorages = new ArrayList<RequestStorage>();
        this.routeContents = new ArrayList<RouteContent>();
    }

    public void addRequestStorage(RequestStorage requestStorage){
        this.requestStorages.add(requestStorage);
    }

    @Override
    public Object add(Object a) {
        String[] b = (String[]) a;
        String route = b[0];
        String originRoute = b[1];
        String url = b[2];
        RouteContent routeContent = new RouteContent(route);
        routeContent.setOrignRoute(originRoute);
        routeContent.setUrl(url);
        this.routeContents.add(routeContent);
        return routeContent;
    }

    @Override
    public ContentClass find(String a) {
        for (RouteContent routeContent : this.routeContents) {
            if (routeContent.getRoute().equals(a)) {
                return routeContent;
            }
        }
        return null;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }
    public List<String> getHeaders() {
        return headers;
    }


    public String getHost() {
        return this.host;
    }

    public int getMaxId(){
        int maxId = 0;
        for (RequestStorage requestStorage :this.requestStorages){
            int id = Integer.parseInt(requestStorage.id);
            if ( id > maxId){
                maxId = id;
            }
        }
        return maxId;
    }

    public void removeRoute(String selectedRoute){
        for (RouteContent routeContent : this.routeContents){
            if (routeContent.getRoute().equals(selectedRoute)){
                this.routeContents.remove(routeContent);
                break;
            }
        }

    }

    public void removeScan(String selectedScan){
        for (RequestStorage requestStorage : this.requestStorages){
            if (requestStorage.id.equals(selectedScan)){
                this.requestStorages.remove(requestStorage);
                break;
            }
        }

    }

    public void clearRoute(){
        this.routeContents.clear();
    }

    public void clearScan(){
        this.requestStorages.clear();
    }

    public void setHttpService(IHttpService httpService) {
        this.httpService = httpService;
    }

    public String getUrl(){
        if (this.httpService.getPort() == 80 || this.httpService.getPort() == 443){
            return this.httpService.getProtocol() + "://" + this.httpService.getHost();
        }
        return this.httpService.getProtocol() + "://" + this.httpService.getHost() + ":" + this.httpService.getPort();
    }
    public boolean is80or443(){
        return this.httpService.getPort() == 80 || this.httpService.getPort() == 443;
    }

    public IHttpService getHttpService() {
        return this.httpService;
    }
}
