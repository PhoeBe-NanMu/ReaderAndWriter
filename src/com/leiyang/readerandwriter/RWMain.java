package com.leiyang.readerandwriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;


public class RWMain
{
    //定义常量
    public static final int count=20;//读者写者的数量
    public static int readCount=0;   //引入计数器readCount对读进程计数
    public static int allNum = 0;

    public static void main(String[] args)
    {

        Select();

    }


    public static boolean isEqualCount(int allNum){
        if (allNum == count){
            return true;
        }else {
            return false;
        }
    }

    public static void Select(){

        readCount = 0;
        allNum = 0;
        Semaphore readCountSemaphore=new Semaphore(1);  //readCountSemaphore 是用于对计数器rc 操作的互斥信号量
        Semaphore writeSemaphore=new Semaphore(1);      //writeSemaphore表示是否允许写的信号量
        Semaphore readerSemaphore = new Semaphore(1);   //用于保证读者和写者互斥地访问的信号量

        System.out.println("\n----------读者与写者----------");
        System.out.println("******----请选择功能----******\n");
        System.out.println("*【1】读者优先  【2】写者优先*\n");
        System.out.println("*【3】清除文件  【4】退出程序*\n");
        System.out.println("------------------------------\n");


        Scanner in = new Scanner(System.in);
        int input = in.nextInt();
        switch (input) {
            case 1:
                for(int i=0;i<count;i++)
                {
                    //随机生成读者和写者
                    if(new Random().nextBoolean()) {//假设是读者
                        new RFReadThread(i,readCountSemaphore,writeSemaphore).start();
                    }
                    else {
                        new RFWriteThread(i,writeSemaphore).start();
                    }
                }
                break;
            case 2:
                for(int i=0;i<count;i++)
                {
                    //随机生成读者和写者
                    if(new Random().nextBoolean()) {//假设是读者
                        new WFReadThread(i,readerSemaphore,readCountSemaphore,writeSemaphore).start();
                    }
                    else {
                        new WFWriteThread(i,readerSemaphore,writeSemaphore).start();
                    }
                }
                break;
            case 3:
                File file = new File("text.txt");
                if (file.exists()){
                    FileOutputStream fileOutputStream = null;
                    try {
                        fileOutputStream = new FileOutputStream(file);
                        fileOutputStream.write(new String("").getBytes());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("文件清理完成！");
                Select();
                break;
            case 4:
                System.out.println("退出...");
                break;
            default:
                System.out.println("输入错误！");
                Select();
                break;
        }




    }
}