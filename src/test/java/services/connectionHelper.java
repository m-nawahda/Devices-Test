package services;

import com.jcraft.jsch.*;
import devicesInformation.SwitchInformation;

import java.io.IOException;
import java.io.InputStream;

public class connectionHelper {
    SwitchInformation switchInfo;
    Session session;
    InputStream in;
    Channel channel;


    public connectionHelper() {
        switchInfo = new SwitchInformation();
    }

    public void initConnection() throws JSchException {
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        JSch jsch = new JSch();
        session = jsch.getSession(switchInfo.getUser(), switchInfo.getHost(), 22);
        session.setPassword(switchInfo.getPassword());
        session.setConfig(config);
        session.connect();
        session.setTimeout(100);
        System.out.println("Connected");

    }

    public void setUpChannel(String command) throws JSchException, IOException {
        channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);
        channel.setInputStream(null);
        ((ChannelExec) channel).setErrStream(System.err);
        in = channel.getInputStream();
        channel.connect();
        System.out.println("Channel connected");
    }

    public void readResponse(String command) throws JSchException, IOException {
        initConnection();
        setUpChannel(command);
        try {
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    System.out.print(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    System.out.println("exit-status: " + channel.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.disconnect();
            session.disconnect();
            System.out.println("DONE");
        }

    }


}
