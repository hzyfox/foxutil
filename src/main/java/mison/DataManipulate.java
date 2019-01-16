package mison;

import java.io.File;
import java.io.IOException;

import com.sun.xml.internal.ws.api.pipe.FiberContextSwitchInterceptor;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * create with mison
 * USER: guyue
 */
public class DataManipulate {
    private static File tartgetFile;
    private static boolean hasFile;

    public static void main(String[] args) throws IOException, WriteException, BiffException {
        {
            filecheck("/Users/guyue/Desktop/jxl.xls");

        }
    }


    public static boolean filecheck(String filename) {
        tartgetFile = new File(filename);
        if (tartgetFile.exists()) {
            hasFile = true;
        }
        return hasFile;
    }

    /**
     * @return the hasFile
     */
    public static File getFile() {
        return tartgetFile;
    }

}
