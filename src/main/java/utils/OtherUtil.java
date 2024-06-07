package utils;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
public class OtherUtil {

    public static String getPasteString(){
        try {
            // 获取系统剪切板
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            // 获取剪切板内容
            return (String) clipboard.getData(DataFlavor.stringFlavor);
        } catch (Exception e) {
            return "";
        }
    }
}
