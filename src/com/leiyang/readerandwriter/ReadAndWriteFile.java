package com.leiyang.readerandwriter;

import java.io.*;

/**
 * Created by 24436 on 2017/3/9.
 */
public class ReadAndWriteFile {

    ///写入到文件
    public void writerTxt(String str) {
        BufferedWriter fw = null;
        try {
            File file = new File("text.txt");
            fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8")); // 指定编码格式，以免读取时中文字符异常
            fw.append(str);
            fw.flush(); // 全部写入缓存中的内容
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 从文件中读取，打印到控制台
    public void readTxt() {
        String filePath = "text.txt"; // 文件和该类在同个目录下
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8")); // 指定读取文件的编码格式，要和写入的格式一致，以免出现中文乱码,
            String str = null;
            while ((str = reader.readLine()) != null) {
                System.out.println(str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
