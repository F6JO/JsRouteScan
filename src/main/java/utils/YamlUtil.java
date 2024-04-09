package utils;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YamlUtil {

    private static Yaml yaml = new Yaml();
    public static Map<String, Object> readYaml(String filePath) {
        try (InputStream in = new FileInputStream(filePath)) {
            Map<String, Object> load = yaml.load(in);
            in.close();
            return load;
        } catch (Exception e) {
            return null;
        }
    }

    public static String writeYaml(Map<String, Object> data, String filePath) {
        try {
            String fileIfNotExists = YamlUtil.createFileIfNotExists(filePath);
            if (fileIfNotExists == null){
                PrintWriter writer = new PrintWriter(new File(filePath));
                yaml.dump(data, writer);
                writer.close();
            }else {
                return fileIfNotExists;
            }
        } catch (FileNotFoundException e) {
            return "Profile initialization failed2";
        }
        return null;
    }
    public static String createFileIfNotExists(String filePath) {
        Path path = Paths.get(filePath);
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            }
        } catch (Exception e) {
            return "Profile initialization failed";
        }
        return null;
    }

    public static void removeYaml(String id, String filePath) {
        Map<String, Object> Yaml_Map = YamlUtil.readYaml(filePath);
        List<Map<String, Object>> List1 = (List<Map<String, Object>>) Yaml_Map.get("Load_List");
        ArrayList<Map<String, Object>> List2 = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> zidian : List1) {
            if (!zidian.get("id").toString().equals(id)) {
                List2.add(zidian);
            }
        }
        Map<String, Object> save = (Map<String, Object>) new HashMap<String, Object>();
        save.put("Load_List", List2);
        save.put("Bypass_List", Yaml_Map.get("Bypass_List"));
        YamlUtil.writeYaml(save, filePath);
    }

    public static void updateYaml(Map<String, Object> up, String filePath) {
        Map<String, Object> Yaml_Map = YamlUtil.readYaml(filePath);
        List<Map<String, Object>> List1 = (List<Map<String, Object>>) Yaml_Map.get("Load_List");
        List<Map<String, Object>> List2 = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> zidian : List1) {
            if (zidian.get("id").toString().equals(up.get("id").toString())) {
                List2.add(up);
            } else {
                List2.add(zidian);
            }
        }
        Map<String, Object> save = (Map<String, Object>) new HashMap<String, Object>();
        save.put("Load_List", List2);
        save.put("Bypass_List", Yaml_Map.get("Bypass_List"));
        YamlUtil.writeYaml(save, filePath);

    }

    public static void addYaml(Map<String, Object> add, String filePath) {
        Map<String, Object> Yaml_Map = YamlUtil.readYaml(filePath);
        List<Map<String, Object>> List1 = (List<Map<String, Object>>) Yaml_Map.get("Load_List");
        int panduan = 0;
        for (Map<String, Object> zidian : List1) {
            if (zidian.get("id").toString().equals(add.get("id").toString())) {
                panduan += 1;
            }
        }
        if (panduan == 0) {
            Map<String, Object> save = (Map<String, Object>) new HashMap<String, Object>();
            List1.add(add);
            save.put("Load_List", List1);
            save.put("Bypass_List", Yaml_Map.get("Bypass_List"));
            YamlUtil.writeYaml(save, filePath);
        }

    }

    public static Map<String, Object> readStrYaml(String str){
        Map<String, Object> data = null;
        Yaml yaml = new Yaml();
        data = yaml.load(str);
        return data;
    }


    public static boolean inYamlList(List<Map<String, Object>> mapList,Map<String, Object> oneMap){
        for (Map<String, Object> i : mapList){
            if (YamlUtil.ifmapEqual(i,oneMap)){
                return true;
            }
        }
        return false;

    }

    public static boolean ifmapEqual(Map<String, Object> i, Map<String, Object> oneMap){
        boolean mapEqual = true;
        for (String key : i.keySet()){
            if (!key.equals("loaded") && !key.equals("id") && !key.equals("type")){
                if (!i.get(key).equals(oneMap.get(key))){
                    mapEqual = false;
                    break;
                }
            }
        }
        return mapEqual;
    }



}


