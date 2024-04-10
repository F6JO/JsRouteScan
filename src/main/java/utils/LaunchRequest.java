package utils;

import burp.BurpExtender;
import burp.IHttpRequestResponse;
import burp.IHttpService;
import burp.IRequestInfo;
import core.Content.HostContent;
import core.Content.RouteContent;
import core.Storage.RequestStorage;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class LaunchRequest {
    private BurpExtender burp;
    public ThreadPoolExecutor executor;

    public LaunchRequest(BurpExtender burpExtender) {
        this.burp = burpExtender;
        this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(this.burp.config.RequestThread);;
    }
    public void updateThreadPoolSize() {
        int newSize = this.burp.config.RequestThread;
        this.executor.setMaximumPoolSize(newSize);
        this.executor.setCorePoolSize(newSize);

    }

    public void fuckGO(IHttpService httpService, String route, List<String> head, HostContent findhost, String passiveScanPath) {
        List<String> headers = new ArrayList<>(head);
        String url = RequestOper.getServiceUri(httpService) + passiveScanPath;
        if (url.endsWith("/")) {
            url += route.substring(1);
        } else {
            url += route;
        }
        URL U = null;
        try {
            U = new URL(url);
        } catch (MalformedURLException e) {
            this.burp.call.printError("url error");
            return;
        }
        byte[] request = this.burp.helpers.buildHttpRequest(U);
        if (this.burp.config.Carryhead) {
            headers.add(0, this.burp.helpers.analyzeRequest(request).getHeaders().get(0));
            request = this.burp.helpers.buildHttpMessage(headers, new byte[]{});
        }
        if (this.burp.config.RequestMethod.equals("POST")) {
            request = this.burp.helpers.toggleRequestMethod(request);
        }
        try {
            goThread(httpService, request, findhost);

        } catch (Exception err) {
            this.burp.call.printError("Request Error");
        }

    }
    private Future<?> goThread(IHttpService httpService, byte[] request, HostContent findhost) {
        try {
            Future<?> submit = executor.submit(() -> sendRequest(httpService, request, findhost));
            return submit;
        }catch (Exception e) {
            this.burp.call.printError("Thread Request Error: " + e.getMessage());
        }
        return null;

    }

    private void sendRequest(IHttpService httpService, byte[] request, HostContent findhost) {
        IHttpRequestResponse iHttpRequestResponse = this.burp.call.makeHttpRequest(httpService, request);
        synchronized (findhost.requestStorages) {
            String id = findhost.getMaxId() + 1 + "";
            RequestStorage requestStorage = new RequestStorage(id, iHttpRequestResponse, this.burp);
            findhost.addRequestStorage(requestStorage);
        }
    }

    public void recursionScan(HostContent hostContent, List<IHttpRequestResponse> iHttpRequestResponses) {
        ArrayList<String> paths = new ArrayList<>();
        for (IHttpRequestResponse iHttpRequestResponse : iHttpRequestResponses) {
            IRequestInfo iRequestInfo = this.burp.helpers.analyzeRequest(iHttpRequestResponse);
            String path = iRequestInfo.getUrl().getPath();
            if (!path.equals("/")) {
                String[] split = path.split("/");
                if (!paths.contains(path) && !split[split.length - 1].contains(".")) {
                    paths.add(path);
                }
                String pa = "";
                for (String s : split) {
                    if (!s.equals(split[split.length - 1]) && !s.equals("")) {
                        pa += "/" + s;
                        if (!paths.contains(pa)) {
                            paths.add(pa);
                        }
                    }
                }

            }
        }
        burp.tab.prompt(hostContent.routeContents.size() * paths.size() + " requests in total");
        for (String i : paths) {
            for (RouteContent routeContent : hostContent.routeContents) {
                this.fuckGO(hostContent.getHttpService(), routeContent.getRoute(), hostContent.getHeaders(), hostContent, i);
            }
        }
    }
}
