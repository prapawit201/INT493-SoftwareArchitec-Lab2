package com.company;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception {

        Scanner userHost = new Scanner(System.in);
        System.out.print("Please input IP : ");
        String host = userHost.nextLine();
        Socket clientSocket = new Socket();
        System.out.println("Connecting ...");
        clientSocket.connect(new InetSocketAddress(host, 8080));
        System.out.printf("Connected from port %d\n",clientSocket.getLocalPort());

        ServerConnection serverCon = new ServerConnection(clientSocket);

        Scanner userInput = new Scanner(System.in);

        new Thread(serverCon).start();
        System.out.print("message : ");
        while (true) {

            String cmd  = userInput.nextLine();
            if (cmd.equalsIgnoreCase("close")) {
                String data = cmd + "\n";
                clientSocket.getOutputStream().write(data.getBytes());
                clientSocket.getOutputStream().flush();
                break;
            }
            String data = cmd + "\n";
            clientSocket.getOutputStream().write(data.getBytes());
            clientSocket.getOutputStream().flush();
        }
        clientSocket.close();
    }
}