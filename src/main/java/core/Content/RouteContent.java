package core.Content;

import core.Interface.ContentClass;

public class RouteContent implements ContentClass {

    public String route;

    public String originRoute = "";
    public String url = "";
    public RouteContent(String a) {
        this.route = a;
    }

    public void setOrignRoute(String a) {
        this.originRoute = a;
    }

    public void setUrl(String a) {
        this.url = a;
    }
    public String getRoute() {
        return this.route;
    }

    @Override
    public Object add(Object a) {
        return null;
    }

    @Override
    public Object find(String a) {
        return null;
    }
}
