package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerConnection implements Runnable {
    private Socket serverSocket;
    private BufferedReader inMessage;
    private PrintWriter outMessage;

    public ServerConnection(Socket socket){
        serverSocket = socket;
        try {
            inMessage = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            outMessage = new PrintWriter(serverSocket.getOutputStream(), true);
        } catch (Exception e) {

        }
    }

    @Override
    public void run() {
        this.serverSocket = serverSocket;
        while (true) {
            try {
                String command = inMessage.readLine();
                System.out.print("user port : " + serverSocket.getLocalPort() + " -> : " + command + "\n");
                System.out.print("message : ");
            } catch (Exception e) {
                System.out.println("Port : "+ serverSocket.getLocalPort()+ " left from the server ...");
                break;
            }
        }
    }
}