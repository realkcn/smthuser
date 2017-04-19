package cn.kcn;

import java.io.*;
import java.util.HashMap;

public class Main {

    static HashMap<String, String> userIp = new HashMap<>();

    public static void readRegisterIP() {
        try (FileInputStream fis = new FileInputStream("registerip.txt");
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)
         ){
            String str;
            while ((str = br.readLine()) != null) {
                String[] data = str.split(" ");
                if (data.length==3) {
                    userIp.put(data[0], data[1].replace("*","0"));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("找不到指定文件");
        } catch (IOException e) {
            System.out.println("读取文件失败");
        }
    }


    public static int localuser=0;
    public static int alluser=0;
    public static int xinjianguser=0;
    public static int xizanguser=0;
    public static int notfounduser=0;

    public static void getUserRegisterIP() {
        try (FileInputStream fis = new FileInputStream("useridlist");
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)
        ){
            String str;
            while ((str = br.readLine()) != null) {
                if (str.length()==0) continue;
                alluser++;
                String ip = userIp.get(str);
                if (ip==null) {
                    notfounduser++;
                    System.out.println("user "+str+" not found");
                } else {
                    try {
                        String[] iplocs = IP.find(ip);
                        String loc = String.join("-", iplocs);
                        if (loc.indexOf("中国")!=-1) {
                            localuser++;
                        }
                        if (loc.indexOf("新疆")!=-1) {
                            xinjianguser++;
                        }
                        if (loc.indexOf("西藏")!=-1) {
                            xizanguser++;
                        }
                    } catch (Exception ex) {
                        System.out.println(ip);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("找不到指定文件");
        } catch (IOException e) {
            System.out.println("读取文件失败");
        }
    }
    public static void main(String[] args) {
        IP.enableFileWatch = false; // 默认值为：false，如果为true将会检查ip库文件的变化自动reload数据

        IP.load("17monipdb.dat");

        readRegisterIP();
        
        getUserRegisterIP();

        System.out.println("总用户:"+alluser
                +"\n未找到用户:"+notfounduser
                +"\n国内用户:"+localuser
                +"\n新疆用户:"+xinjianguser
                +"\n西藏用户:"+xizanguser);
    }
}
