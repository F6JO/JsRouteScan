package common;

import burp.*;
import core.Content.HostContent;
import core.Content.RouteContent;
import utils.RequestOper;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BurpListening implements IHttpListener {

    public BurpExtender burpExtender;

    public BurpListening(BurpExtender burpExtender) {
        this.burpExtender = burpExtender;
    }

//    @Override
//    public void processHttpMessage(int i, boolean b, IHttpRequestResponse iHttpRequestResponse) {
//        if (!b) {
//            List<String> headers = RequestOper.getHeaders(this.burpExtender, iHttpRequestResponse);
//            headers.remove(0);
//            String host = RequestOper.getHost(iHttpRequestResponse);
//            HostContent findhost = this.burpExtender.tab.reqDisplay.hosttab.find(host);
//
//            String url = RequestOper.geturl(this.burpExtender, iHttpRequestResponse);
//            boolean contains = this.burpExtender.config.ExSuffix.stream().anyMatch(url::endsWith);
//            // Determine whether the blacklist suffix is not included in the request path
//            if (!contains) {
//
//                boolean isFirstMatch = true;
//                for (String regex : this.burpExtender.config.REGEXS) {
//                    Pattern p = Pattern.compile(regex, Pattern.DOTALL);
//                    String textBody = new String(iHttpRequestResponse.getResponse());
//                    Matcher m = p.matcher(textBody);
//
//                    int findStart = 0;
//                    while (m.find(findStart)) {
//                        if (isFirstMatch) {
//                            this.burpExtender.call.printOutput("Route found in: " + url);
//                            if (findhost == null) {
//                                findhost = (HostContent) this.burpExtender.tab.reqDisplay.hosttab.add(host);
//                            }
//                            if (findhost.headers == null) {
//                                findhost.setHeaders(headers);
//                            }
//                            findhost.setHttpService(iHttpRequestResponse.getHttpService());
//                            isFirstMatch = false;
//                        }
//                        String group = m.group(1).trim();
//                        if (this.checkRouteSuffix(group)) {
//                            findStart = m.end() - 160;
//                            continue;
//                        }
//
//                        String route = this.proceMatch(group);
//                        if (!Objects.equals(route, "") && !Objects.equals(route, "/")) {
//                            RouteContent routeContent = (RouteContent) findhost.find(route);
//                            if (routeContent == null) {
//                                findhost.add(new String[]{route, group, url});
//                                this.burpExtender.tab.reqDisplay.infotab.pathTab.leftTab.updateAll();
//                                if (burpExtender.config.PassiveScan) {
//                                    this.burpExtender.launchRequest.fuckGO(iHttpRequestResponse.getHttpService(), route, headers, findhost, this.burpExtender.config.PassiveScanPath);
//                                }
//
//                            }
//                        }
//                        findStart = m.end() - 160;
//
//                    }
//                }
//
//
//            }
//        }
//    }

    @Override
    public void processHttpMessage(int i, boolean b, IHttpRequestResponse iHttpRequestResponse) {
        if (!b) {
            List<String> headers = RequestOper.getHeaders(this.burpExtender, iHttpRequestResponse);
            headers.remove(0);
            String host = RequestOper.getHost(iHttpRequestResponse);
            HostContent findhost = this.burpExtender.tab.reqDisplay.hosttab.find(host);

            String url = RequestOper.geturl(this.burpExtender, iHttpRequestResponse);
            boolean contains = this.burpExtender.config.ExSuffix.stream().anyMatch(url::endsWith);
            // Determine whether the blacklist suffix is not included in the request path
            if (!contains) {

                boolean isFirstMatch = true;
                for (String regex : this.burpExtender.config.REGEXS) {
                    Pattern p = Pattern.compile(regex, Pattern.DOTALL);
                    String textBody = new String(iHttpRequestResponse.getResponse());
                    Matcher m = p.matcher(textBody);

                    while (m.find()) {
                        if (isFirstMatch) {
                            this.burpExtender.call.printOutput("Route found in: " + url);
                            if (findhost == null) {
                                findhost = (HostContent) this.burpExtender.tab.reqDisplay.hosttab.add(host);
                            }
                            if (findhost.headers == null) {
                                findhost.setHeaders(headers);
                            }
                            findhost.setHttpService(iHttpRequestResponse.getHttpService());
                            isFirstMatch = false;
                        }
                        String group = this.removeQuotes(m.group().trim());
                        if (this.checkRouteSuffix(group)) {
                            continue;
                        }

                        String route = this.proceMatch(group);
                        if (!Objects.equals(route, "") && !Objects.equals(route, "/")) {
                            RouteContent routeContent = (RouteContent) findhost.find(route);
                            if (routeContent == null) {
                                findhost.add(new String[]{route, group, url});
                                this.burpExtender.tab.reqDisplay.infotab.pathTab.leftTab.updateAll();
                                if (burpExtender.config.PassiveScan) {
                                    this.burpExtender.launchRequest.fuckGO(iHttpRequestResponse.getHttpService(), route, headers, findhost, this.burpExtender.config.PassiveScanPath);
                                }

                            }
                        }

                    }
                }


            }
        }
    }

    public  String removeQuotes(String str) {
        return str.replaceAll("^[\"']+|[\"']+$", "");
    }
    public String proceMatch(String str) {
        str = str.replaceAll(" ", "");
        for (String exRouteRegex : this.burpExtender.config.ExRouteRegexs) {
            Pattern p = Pattern.compile(exRouteRegex);
            Matcher m = p.matcher(str);
            if (m.find()) {
                return "";
            }
        }
        if (str.startsWith("../")) {
            str = str.replaceFirst("\\.\\./", "/");
        }

        if (!str.startsWith("/")) {
//            if (this.burpExtender.config.ExStart.stream().anyMatch(str::startsWith)) {
//                return "";
//            }
            str = "/" + str;
        }
        int index = str.indexOf("/", 2);
        if (str.startsWith("//") && index != -1 && index <= str.length() - 1) {
            str = str.substring(index);
        }
        str = str.replaceAll("/+", "/");

        return str;
    }


    public boolean checkRouteSuffix(String group) {
        for (String suffix : this.burpExtender.config.ExSuffix) {
            if (group.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }


}
