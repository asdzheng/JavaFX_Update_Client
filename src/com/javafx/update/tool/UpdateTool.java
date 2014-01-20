/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javafx.update.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;

public class UpdateTool {

    public static String getNowVer(String LocalVerFileName) {
        System.out.println("LocalVerFileName ： " + LocalVerFileName);
        File verFile = new File(LocalVerFileName);
        FileReader is = null;
        BufferedReader br = null;
        String ver = null;
        try {
            is = new FileReader(verFile);
            br = new BufferedReader(is);
            ver = br.readLine();
            br.close();
            is.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null,"FileNotFound");
            System.out.println("can not find the version file !/n");
        } catch (IOException ex) {
            System.out.println("read file fail!/n");
        }
        return ver;
    }
    
    public static String getProjectFolderPath() {
        return System.getProperty("user.dir") + "/";
    }
}
