package basetool;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

/**
 * @author linxi
 * www.leftvalue.top
 * tools
 * Date 23/01/2018 9:12 PM
 * call the system clipboard to get / write
 */
public class SystemClipboardTools {
    public static Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();

    /**
     * get string from system clipboard
     *
     * @return
     */
    public static String get() {
        try {
            Transferable clipTf = sysClip.getContents(null);
            if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String line = (String) clipTf.getTransferData(DataFlavor.stringFlavor);
                return line;
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * write string into system clipboard
     *
     * @param str
     * @return
     */
    public static boolean write(String str) {
        try {
            Transferable tText = new StringSelection(str.toString());
            sysClip.setContents(tText, null);
            return true;
        } catch (Exception e) {
            System.err.println("something was wrong when write into system slipboard");
            return false;
        }
    }
}
