package com.leiyang.readerandwriter;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class WFReadThread extends Thread
{  
    public int id;// 读者ID
    public Semaphore readCountSemaphore;// 读者数量信号量
    public Semaphore writeSemaphore;// 写者信号量
    public Semaphore readerSemaphore;
  
    public WFReadThread(int id, Semaphore readerSemaphore, Semaphore readCountSemaphore, Semaphore writeSemaphore)
    {  
        this.id = id;
        this.readCountSemaphore = readCountSemaphore;
        this.writeSemaphore=writeSemaphore;
        this.readerSemaphore = readerSemaphore;
    }
    //读者优先
    public void run()  
    {

        try  
        {
            readerSemaphore.acquire();    //若有写进程，后续读进程等待，在read队列上排队

            readCountSemaphore.acquire(); //用于对共享变量readCount操作的互斥信号量

            RWMain.readCount++;           //来了一个读进程，读进程数加1

            if (RWMain.readCount == 1)    //如是第一个读进程，判断是否有写进程在临界区，若有，读进程等待，若无，阻塞写进程
            {
               writeSemaphore.acquire();  //若有，读进程等待，若无，阻塞写进程
            }

            readCountSemaphore.release(); //结束对readCount共享变量的互斥访问

            readerSemaphore.release();    //从 read队列中唤醒一个进程
            /**********************************/

            //此刻才可以允许其他读者读数据
            System.out.println("读者【"+id+"】开始读取...");
            Thread.sleep((long) (new Random().nextFloat()*1000));   //可以读了
            new ReadAndWriteFile().readTxt();
            System.out.println("读者【"+id+"】完成读取...");

            /**********************************/
            readCountSemaphore.acquire();
            System.out.println("当前共有"+ RWMain.readCount+"个读者读取\n-----------------------------");
            RWMain.readCount--;       //一个读进程读完，读进程数减1

            if(RWMain.readCount == 0)//最后一个离开临界区的读进程需要判断是否有写进程//需要进入临界区，若有，唤醒一个写进程进临界区
            {
                writeSemaphore.release();
            }

            RWMain.allNum++;
            if (RWMain.isEqualCount(RWMain.allNum)){
                System.out.println("------------------------------\n---------操作已经完成---------");

                RWMain.Select();
            }

            readCountSemaphore.release();//释放读者信号量,结束对rc共享变量的互斥访问
              
        }  
        catch (InterruptedException e)  
        {
            e.printStackTrace();  
        }
    }  
}