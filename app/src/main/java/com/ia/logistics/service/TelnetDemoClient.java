package com.ia.logistics.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;

import org.apache.commons.net.telnet.TelnetClient;
public class TelnetDemoClient
{
    private TelnetClient telnet ;

    private DataInputStream in;

    private DataOutputStream out;

    private char prompt = '$';// 普通用户结束

    public TelnetDemoClient(String ip, int port )  throws Exception{
        //long startTime = System.currentTimeMillis();
        telnet =new TelnetClient();
        // 设置延迟3秒
        //telnet.setSoTimeout(3);
        telnet.setConnectTimeout(3000);
        telnet.connect(ip, port);
//          in = telnet.getInputStream();
//          out = new PrintStream(telnet.getOutputStream());
        OutputStream netOut = telnet.getOutputStream();
        out = new DataOutputStream(netOut);
        in = new DataInputStream(telnet.getInputStream());
        // 根据root用户设置结束符
        //this.prompt = user.equals("root") ? '#' : '>';
        //login(user, password);
    }

    /**
     * 登录
     * @param user
     * @param password
     */
    public void login(String user, String password) {
//        readUntil("login:");
        readUntil("login:");
        write(user);
        readUntil("Password:");
        write(password);
        readUntil(prompt + "");
    }

    /**
     * 读取分析结果
     *
     * @param pattern
     * @return
     */
    public String readUntil(String pattern){
        try
        {
            char lastChar = pattern.charAt(pattern.length() - 1);
            StringBuffer sb = new StringBuffer();

            //in = new DataInputStream(s.getInputStream());
            //log.info("message::::" +in.readLine());;

            //String line = in.readLine();
            DataInputStream datain = new DataInputStream( in );
            String result =  datain.readUTF();
            System.out.println( "========= ReadLine result:" + result );

            return result;

//            char ch = (char)in.read();
//            while (true)
//            {
//                sb.append(ch);
//                if (ch == lastChar)
//                {
//                    if (sb.toString().endsWith(pattern))
//                    {
//                        return sb.toString();
//                    }
//                }
//                ch = (char)in.read();
//                System.out.print(ch);
//            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "========";
    }

    /**
     * 写操作
     *
     * @param value
     */
    public void write(String value){
        try
        {
            out.writeUTF(value);
            out.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 向目标发送命令字符串
     *
     * @param command
     * @return
     */
    public String sendCommand(String command){
        try
        {
            write(command);
            return readUntil(prompt + "");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭连接
     */
    public void disconnect(){
        try
        {
            telnet.disconnect();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     *  发送gps的xml格式
     * @param sendXml
     * @return
     * @throws Exception
     */
    public static String sendGpsInfo(String sendXml) throws Exception {
        sendXml = sendXml.replaceAll("\"", "'");
        StringBuffer paramBuffer = new StringBuffer();
        paramBuffer.append( "{" ).append( "\"projectName\"" ).append( ":" ).append( "\"3plgps\"" )
                .append( "," ).append( "\"data\"" ).append( ":" ).append( "\""+sendXml+"\"" )
                .append( "}" );
        String replace=paramBuffer.toString();
        replace=replace.replaceAll("\n","");    //iplatmbs.baosteel.net.cn
        System.out.println( "param:" + replace); //10.60.17.77
        TelnetDemoClient she =new TelnetDemoClient("10.60.17.77",9527);
        System.out.println( "Start Telenet" );
        System.out.println( "Class:" +  she);
        String str=she.sendCommand(replace);
        System.out.println( "Result:" +str + "\n");
        System.out.println( "End Telenet" );
        she.disconnect();
        return sendXml;
    }
}