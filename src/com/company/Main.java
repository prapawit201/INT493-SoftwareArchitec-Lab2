package com.company;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static ArrayList<ClientHandler> clients = new ArrayList<>(); //anyone can join
    private static ExecutorService pool = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws Exception {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(8080));
            System.out.println("Listening on 8080 \n Start server \n ... Waiting for Client request");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.printf("Client Connected %s:%d\n", clientSocket.getInetAddress().getHostAddress()
                        , clientSocket.getPort()
                        , clientSocket.getLocalPort());
                ClientHandler clientThread = new ClientHandler(clientSocket, clients);
                clients.add(clientThread);
                pool.execute(clientThread);
            }
    }
}

class  ClientHandler implements Runnable {

    private Socket clientSocket;
    private ArrayList<ClientHandler> clients;

    public ClientHandler(Socket clientSocket, ArrayList<ClientHandler> clients) {
        this.clientSocket = clientSocket;
        this.clients = clients;
    }

    @Override
    public void run() {
        try {
            Scanner sc = new Scanner(clientSocket.getInputStream());
            while (sc.hasNextLine()) {
                String command = sc.nextLine();
                System.out.printf("Got %s form %s:%d\n",
                        command,
                        clientSocket.getInetAddress().getHostAddress(),
                        clientSocket.getPort());
                //ถ้า client close หมายความว่าเขาออกจากห้องสนทนาเเล้ว
                if(command.equalsIgnoreCase("close")) {
                    System.out.printf("Stream End for %s:%d\n",
                            clientSocket.getInetAddress().getHostAddress(),
                            clientSocket.getPort());
                    break;
                } else {
                    outToAll(command);
                }
                clientSocket.getOutputStream().flush();
            }
            clientSocket.close();
        } catch (Exception e){
        }
    }

    private void outToAll(String command) {
        try {
            for (ClientHandler aClient : clients) {
                int i = 0;
                i++;
                aClient.clientSocket.getOutputStream().write((command +"\n").getBytes());
                aClient.clientSocket.getOutputStream().flush();
            }
        }catch (Exception e){
        }
    }
}