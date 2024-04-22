package burp;

import org.yaml.snakeyaml.Yaml;
import utils.YamlUtil;

import java.util.*;

public class Config {

    private BurpExtender burp;
    public ArrayList<String> REGEXS;
//    public Boolean Start;

    public ArrayList<String> ExSuffix;
//    public ArrayList<String> ExStart;
    public boolean PassiveScan;
    public String PassiveScanPath;
    public boolean Carryhead;
    public int RequestThread;
    public String RequestMethod;
    public ArrayList<String> ExRouteRegexs;

    public Config(BurpExtender burp) {
        this.burp = burp;
        this.REGEXS = new ArrayList<String>();
        this.ExSuffix = new ArrayList<String>();
        this.ExRouteRegexs = new ArrayList<String>();
        this.init();
    }

    private void init(){
//        this.Start = false;
        this.PassiveScan = false;
        this.Carryhead = false;
        this.RequestThread = 10;
        this.RequestMethod = "GET";
        this.PassiveScanPath = "/";

        Map<String, Object> config = YamlUtil.readYaml(BurpExtender.CONFIGPATH);
        if (config == null){
            Map<String,Object> data = new HashMap<>();
//            data.put("Regexs",Arrays.asList("(?:\"|')(((?:[a-zA-Z]{1,10}://|//)[^\"'/]{1,}.[a-zA-Z]{2,}[^\"']{0,})|((?:/|../|./)[^\"'><,;| *()(%%$^/\\\\\\[\\]][^\"'><,;|()]{1,})|([a-zA-Z0-9_-/]{1,}/[a-zA-Z0-9_-/]{1,}.(?:[a-zA-Z]{1,4}|action)(?:[?|/][^\"|']{0,}|))|([a-zA-Z0-9_-]{1,}.(?:php|asp|aspx|jsp|json|action|html|js|txt|xml)(?:?[^\"|']{0,}|))|((http|ftp|https)://[\\w-]+(.[\\w-]+)+([\\w-.,@?^=%&:/+#]*[\\w-@?^=%&/+#])?))(?:\"|')"));
//            data.put("Regexs",Arrays.asList(".{10}[\"'`]([a-zA-Z0-9/=_{}\\.\\?&!-]+/[a-zA-Z0-9/=_{}\\.\\?&!-]+(\\.jspx|\\.jsp|\\.html|\\.php|\\.do|\\.aspx|\\.action|\\.json)*)[\"'`].{160}"));
            data.put("Regexs",Arrays.asList("[a-zA-Z0-9/=_{}\\.\\?&!-]+/[a-zA-Z0-9/=_{}\\.\\?&!-]+(\\.jspx|\\.jsp|\\.html|\\.php|\\.do|\\.aspx|\\.action|\\.json)*[\"'`]"));
            data.put("ExSuffix",Arrays.asList(".jpg", ".png", ".css", ".jpeg", ".gif",".zip",".tar",".mp3", ".wav", ".flac", ".mp4", ".mov", ".avi", ".mkv", ".bmp", ".tiff", ".pdf", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", ".odt", ".ods", ".odp", ".ps", ".eps", ".ai", ".psd", ".indd", ".fla", ".swf"));
            data.put("ExRouteRegexs",Arrays.asList("\\.css$","\\.js$","^text/css" ,"^text/html","^text/plain","^text/xml","^image/gif","^image/jpeg","^image/png","^application/xhtml+xml","^application/xml","^application/atom+xml","^application/json","^application/pdf","^application/msword","^application/octet-stream","^application/x-www-form-urlencoded","^multipart/form-data","^image/tiff","^application/x-001","^application/x-301","^text/h323","^application/x-906","^drawing/907","^application/x-a11","^audio/x-mei-aac","^application/postscript","^audio/aiff","^application/x-anv","^text/asa","^video/x-ms-asf","^text/asp","^audio/basic","^video/avi","^application/vnd.adobe.workflow","^text/xml","^application/x-bmp","^application/x-bot","^application/x-c4t","^application/x-c90","^application/x-cals","^application/vnd.ms-pki.seccat","^application/x-netcdf","^application/x-cdr","^application/x-cel","^application/x-x509-ca-cert","^application/x-g4","^application/x-cgm","^application/x-cit","^java/*","^application/x-cmp","^application/x-cmx","^application/x-cot","^application/pkix-crl","^application/x-dib","^application/x-msdownload","^application/x-drw","^application/x-ebx","^application/x-emf","^message/rfc822","^application/x-epi","^application/x-ps","^application/x-frm","^application/fractals","^text/xml","^application/x-frm","^application/x-g4","^application/x-gbr","^image/gif","^application/x-gl2","^application/x-gp4","^application/x-hgl","^application/x-hmr","^application/x-hpgl","^application/x-hpl","^application/mac-binhex40","^application/x-hrf","^application/hta","^text/x-component","^text/html","^text/webviewhtml","^text/html","^application/x-icb","^image/x-icon","^application/x-ico","^application/x-iff","^application/x-iphone","^application/x-img","^application/x-internet-signup"));
            String s = YamlUtil.writeYaml(data, BurpExtender.CONFIGPATH);
            if(s != null){
                this.burp.tab.prompt(s);
                return;
            }
            config = YamlUtil.readYaml(BurpExtender.CONFIGPATH);
        }
        List<String> regexs = (List<String>) config.get("Regexs");
        List<String> exSuffix = (List<String>) config.get("ExSuffix");
        List<String> exRouteRegexs = (List<String>) config.get("ExRouteRegexs");
        this.REGEXS.addAll(regexs);
        this.ExSuffix.addAll(exSuffix);
        this.ExRouteRegexs.addAll(exRouteRegexs);

//        this.REGEXS.add(".{10}[\"'`]([a-zA-Z0-9/=_{}\\.\\?&!-]+/[a-zA-Z0-9/=_{}\\.\\?&!-]+(\\.jspx|\\.jsp|\\.html|\\.php|\\.do|\\.aspx|\\.action|\\.json)*)[\"'`].{160}");
//        this.ExSuffix.addAll(Arrays.asList(".jpg", ".png", ".css", ".jpeg", ".gif",".zip",".tar",".mp3", ".wav", ".flac", ".mp4", ".mov", ".avi", ".mkv", ".bmp", ".tiff", ".pdf", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", ".odt", ".ods", ".odp", ".ps", ".eps", ".ai", ".psd", ".indd", ".fla", ".swf"));
//        this.ExRouteRegexs.addAll(Arrays.asList("\\.css$","\\.js$","^text/css" ,"^text/html","^text/plain","^text/xml","^image/gif","^image/jpeg","^image/png","^application/xhtml+xml","^application/xml","^application/atom+xml","^application/json","^application/pdf","^application/msword","^application/octet-stream","^application/x-www-form-urlencoded","^multipart/form-data","^image/tiff","^application/x-001","^application/x-301","^text/h323","^application/x-906","^drawing/907","^application/x-a11","^audio/x-mei-aac","^application/postscript","^audio/aiff","^application/x-anv","^text/asa","^video/x-ms-asf","^text/asp","^audio/basic","^video/avi","^application/vnd.adobe.workflow","^text/xml","^application/x-bmp","^application/x-bot","^application/x-c4t","^application/x-c90","^application/x-cals","^application/vnd.ms-pki.seccat","^application/x-netcdf","^application/x-cdr","^application/x-cel","^application/x-x509-ca-cert","^application/x-g4","^application/x-cgm","^application/x-cit","^java/*","^application/x-cmp","^application/x-cmx","^application/x-cot","^application/pkix-crl","^application/x-dib","^application/x-msdownload","^application/x-drw","^application/x-ebx","^application/x-emf","^message/rfc822","^application/x-epi","^application/x-ps","^application/x-frm","^application/fractals","^text/xml","^application/x-frm","^application/x-g4","^application/x-gbr","^image/gif","^application/x-gl2","^application/x-gp4","^application/x-hgl","^application/x-hmr","^application/x-hpgl","^application/x-hpl","^application/mac-binhex40","^application/x-hrf","^application/hta","^text/x-component","^text/html","^text/webviewhtml","^text/html","^application/x-icb","^image/x-icon","^application/x-ico","^application/x-iff","^application/x-iphone","^application/x-img","^application/x-internet-signup"));

    }
}
