package com.siyue.one;



import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class FtpPool {

    //存放线程池的容器
    private static Queue<FTPClient> ftpClients = new LinkedList<>();

    static {
        for (int i = 1; i <= 10; i++) {
            FTPClient ftpClient = new FTPClient();
            try {
                ftpClient.connect(FtpPoolConfig.getHostname(),FtpPoolConfig.getPort());
                ftpClient.login(FtpPoolConfig.getUser(),FtpPoolConfig.getPass());
                String replyString = ftpClient.getReplyString();
                System.out.println("replyString = " + replyString);
                ftpClients.add(ftpClient);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //从线程池中获取ftpClient
    public static FTPClient getClient(){
        FTPClient ftpClient = ftpClients.poll();
        String replyString = ftpClient.getReplyString();
        System.out.println("getClient()==>replyString = " + replyString);
        if("230 Login successful".equals(replyString)){
            return ftpClient;
        }else{
            try {
                ftpClient.disconnect();
                ftpClient.connect(FtpPoolConfig.getHostname(),FtpPoolConfig.getPort());
                ftpClient.login(FtpPoolConfig.getUser(),FtpPoolConfig.getPass());
            }catch (Exception e){
                e.printStackTrace();
            }
            return ftpClient;
        }

    }
    //将ftpClient返回到线程池中
    public static void disClient(FTPClient ftpClient){
        ftpClients.add(ftpClient);
    }

    public static void main(String[] args) throws IOException {
        int num = 1;
        while (true){
            System.out.println("ftpClients.size() = " + ftpClients.size());
            FTPClient client = getClient();
            client.storeFile(new String("SC".getBytes(StandardCharsets.UTF_8)) + num,new FileInputStream(new File("C:\\Users\\12314\\Desktop\\aaa.txt")));
            num++;
            System.out.println("文件数量：" + client.listFiles().length);
            System.out.println("归还前ftpClients.size() = " + ftpClients.size());
            System.out.println(client.getReplyString()  );
            disClient(client);
            System.out.println("归还后ftpClients.size() = " + ftpClients.size());

            try {
                System.out.println("进入等待时间");
                TimeUnit.SECONDS.sleep(120);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }



    }
}
