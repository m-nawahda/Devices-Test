package services;

import com.jcraft.jsch.*;
import devicesInformation.ServerInformation;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SSHServices {
    public ServerInformation serverInfo;
    Session session;
    InputStream in;
    Channel channel;
    StringBuilder responseBuffer;

    public SSHServices() {
        serverInfo = new ServerInformation();
    }


    public void initConnection() throws JSchException {
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        JSch jsch = new JSch();
        session = jsch.getSession(serverInfo.getUser(), serverInfo.getHost(), 22);
        session.setPassword(serverInfo.getPassword());
        session.setConfig(config);
        session.connect();
        session.setTimeout(100);
        System.out.println("Connected to 192.168.200.91 via ssh");

    }

    public void setCommand(String command) throws JSchException, IOException {
        channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);
        channel.setInputStream(null);
        ((ChannelExec) channel).setErrStream(System.err);
        in = channel.getInputStream();
        responseBuffer = new StringBuilder();
        channel.connect();
        System.out.println("set command to get sysName via ssh");
    }

    public String getRegex(String response) {
        final String regex = "STRING:\\s([^\\n\\r]*)\n";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(response);
        while (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    public String readResponse(String command) throws JSchException, IOException {
        initConnection();
        setCommand(command);
        try {
            while (true) {
                for (int c; ((c = in.read()) >= 0); ) {
                    responseBuffer.append((char) c);
                }

                if (channel.isClosed()) {
                    if (in.available() > 0) continue;
                    break;
                }
            }
        } catch (Exception ioEx) {
            System.err.println(ioEx.toString());
            return "";
        } finally {
            channel.disconnect();
            session.disconnect();
            return getRegex(responseBuffer.toString());
        }
    }
}
