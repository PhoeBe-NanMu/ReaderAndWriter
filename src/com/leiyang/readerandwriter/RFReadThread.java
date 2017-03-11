package com.leiyang.readerandwriter;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class RFReadThread extends Thread
{  
    public int id;                      // 读者ID
    public Semaphore readCountSemaphore;// 读者数量信号量
    public Semaphore writeSemaphore;    // 写者信号量
  
    public RFReadThread(int id, Semaphore readCountSemaphore, Semaphore writeSemaphore)
    {  
        this.id = id;
        this.readCountSemaphore = readCountSemaphore;
        this.writeSemaphore=writeSemaphore;
    }  
    //读者优先
    public void run()  
    {  
        try  
        {

            readCountSemaphore.acquire(); //开始对readCount共享变量进行互斥访问

            RWMain.readCount++;           //来了一个读进程，读进程数加1
            if (RWMain.readCount == 1)    //如是第一个读进程，判断是否有写进程在临界区，若有，读进程等待，若无，阻塞写进程
            {
               writeSemaphore.acquire();
            }

            readCountSemaphore.release();
              
            //允许其他读者读数据
            System.out.println("读者【"+id+"】开始读取...");
            Thread.sleep((long) (new Random().nextFloat()*1000));   //一段睡眠时间后可以读了
            new ReadAndWriteFile().readTxt();
            System.out.println("读者【"+id+"】完成读取...");


            readCountSemaphore.acquire();
            System.out.println("当前共有"+ RWMain.readCount+"个读者读取\n-----------------------------");

            RWMain.readCount--;                 //一个读进程读完，读进程数减1

            if(RWMain.readCount == 0)           //最后一个离开临界区的读进程需要判断是否有写进程需要进入临界区，若有，唤醒一个写进程进临界区
            {
                writeSemaphore.release();       //判断是否有写进程，需要进入临界区，若有，唤醒一个写进程进临界区
            }

            //记录所用执行完了的线程的数量，当等于预定所有线程数量时，再次打开主界面
            RWMain.allNum++;
            if (RWMain.isEqualCount(RWMain.allNum)){
                System.out.println("------------------------------\n---------操作已经完成---------");
                RWMain.Select();
            }
            readCountSemaphore.release();       //释放读者信号量,结束对readCount共享变量的互斥访问

        }  
        catch (InterruptedException e)  
        {
            e.printStackTrace();  
        }


    }  
}