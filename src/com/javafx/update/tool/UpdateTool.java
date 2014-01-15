/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javafx.update.tool;

import static com.javafx.update.tool.UpdateTool.getProjectFolder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Richard
 */
public class UpdateTool {

    public static String getNowVer(String LocalVerFileName) {
        System.out.println("LocalVerFileName ： " + LocalVerFileName);
        //本地版本文件
        File verFile = new File(LocalVerFileName);
        FileReader is = null;
        BufferedReader br = null;
        String ver = null;
        //读取本地版本
        try {
            is = new FileReader(verFile);
            br = new BufferedReader(is);
            ver = br.readLine();
            br.close();
            is.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null,"FileNotFound");
            //System.out.println("本地版本文件未找到/n");
        } catch (IOException ex) {
            System.out.println("本地版本文件读取错误/n");
        }
        return ver;
    }

    public static void changeMainJar(String jarName) {
        FileReader is = null;
        BufferedReader br = null;
        String temp = null;
        ArrayList<String> contexts = new ArrayList<String>();
        String filePath = getProjectFolder() + "package.cfg";
        PrintWriter pw = null;
        try {
            //释放资源
            is = new FileReader(filePath);
            br = new BufferedReader(is);
            while ((temp = br.readLine()) != null) {
                if (temp.contains("mainjar=")) {
                    temp = "mainjar=" + jarName;
                }
                contexts.add(temp);
            }
            is.close();
            br.close();
            pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filePath)), true);
            for (String buf : contexts) {
                pw.println(buf);
            }
            pw.close();
        } catch (FileNotFoundException ex) {
            System.out.println("本地版本文件未找到/n");
        } catch (IOException ex) {
            System.out.println("本地版本文件读取错误/n");
        }
    }

    public static String getProjectFolder() {
        return System.getProperty("user.dir") + "/";
    }
}
