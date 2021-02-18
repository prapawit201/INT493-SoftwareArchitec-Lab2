package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ServerConnection implements Runnable {
    private Socket serverSocket;
    private BufferedReader inMessage;
    private PrintWriter outMessage;

    public ServerConnection(Socket s){
        serverSocket = s;
        try {
            inMessage = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            outMessage = new PrintWriter(serverSocket.getOutputStream(), true);
        } catch (IOException e) {
        }
    }

    @Override
    public void run() {
        this.serverSocket = serverSocket;
        try {
            while (true) {
              String  command = inMessage.readLine();
                System.out.print("user port : " + serverSocket.getLocalPort() + " -> : "+ command +"\n");
                System.out.print("message : ");
            }
        }catch (Exception e){
        }
    }
}