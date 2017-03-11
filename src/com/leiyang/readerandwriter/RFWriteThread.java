package com.leiyang.readerandwriter;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class RFWriteThread extends Thread
{  
    public int id;                      //编号
    public Semaphore writeSemaphore;    //写者信号量

   public RFWriteThread(int id, Semaphore semaphore)
   {  
       this.id=id;  
       this.writeSemaphore=semaphore;
   }  
   public void run()  
   {
       try  
    {

        writeSemaphore.acquire();

        System.out.println("写者【"+this.id+"】正在写入...");
        Thread.sleep((long) (new Random().nextFloat()*1000));   //可以写了
        new ReadAndWriteFile().writerTxt("信息"+id+"|");
        System.out.println("写者【"+this.id+"】完成写入...");

        RWMain.allNum++;
        if (RWMain.isEqualCount(RWMain.allNum)){
            System.out.println("------------------------------\n---------操作已经完成---------");

            RWMain.Select();
        }
        System.out.println("-----------------------------");

        writeSemaphore.release();
    }  
    catch (InterruptedException e)  
    {
        e.printStackTrace();  
    }
   }


} 