package jcardsimrunner;

import com.licel.jcardsim.smartcardio.CardSimulator;
import com.licel.jcardsim.utils.AIDUtil;
import javacard.framework.AID;
import BitaWalletCard.*;

import javax.smartcardio.*;
import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;

public class Server {

    public static CardSimulator simulator = null;
    public static AID appletAID = null;

    public static void main(String[] args) {

        simulator = new CardSimulator();
        simulator.changeProtocol("T=1");
        appletAID = AIDUtil.create(BitaWalletCard.AID);
        simulator.installApplet(appletAID, BitaWalletCard.class);

        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(4444), 0);
        } catch (Exception e) {
            System.out.println(e.toString());
            return;
        }
        server.createContext("/jcardsim", new MyHandler());
        server.setExecutor(null);
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange he) throws IOException {
            InputStreamReader isr = new InputStreamReader(he.getRequestBody());
            BufferedReader br = new BufferedReader(isr);
            int b;
            StringBuilder buf = new StringBuilder(512);
            while ((b = br.read()) != -1) {
                buf.append((char) b);
            }
            br.close();
            isr.close();

            String cmd = buf.toString();

            String response = "";

            if (cmd.equals("connect")) {
                simulator.selectApplet(appletAID);
            } else if (cmd.equals("disconnect")) {
                simulator.reset();
            } else {
                CommandAPDU commandAPDU = new CommandAPDU(hex2bytes(cmd));
                ResponseAPDU responseAPDU = simulator.transmitCommand(commandAPDU);
                System.out.println(">> " + cmd);
                System.out.println("<< " + responseAPDU.toString());
                response = bytes2Hex(responseAPDU.getBytes());
                System.out.println("   " + response);
            }
            he.sendResponseHeaders(200, response.length());
            OutputStream os = he.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    public static byte[] hex2bytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytes2Hex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
