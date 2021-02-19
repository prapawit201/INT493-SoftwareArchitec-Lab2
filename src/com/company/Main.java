package com.company;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static ArrayList<ClientHandler> clients = new ArrayList<>(); //anyone can join
    private static ExecutorService executorService = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws Exception {

            Scanner scanner = new Scanner(System.in);
            System.out.print("Connecting port : ");
            int port = Integer.parseInt(scanner.nextLine());
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(port));//open port 8080
            System.out.println("Listening on port : " + port + " \nServer started ... \n ");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.printf("Client Connected %s:%d\n", clientSocket.getInetAddress().getHostAddress()
                        , clientSocket.getPort()
                        , clientSocket.getLocalPort());
                ClientHandler clientThread = new ClientHandler(clientSocket, clients);
                clients.add(clientThread);
                executorService.execute(clientThread);
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
                System.out.printf("Got %s from %s:%d\n",
                        command,
                        clientSocket.getInetAddress().getHostAddress(),
                        clientSocket.getPort());
                //ถ้า client close หมายความว่าเขาออกจากห้องสนทนาเเล้ว
                if(command.equalsIgnoreCase("close")) {
                    System.out.printf(
                            clientSocket.getInetAddress().getHostAddress(),
                            clientSocket.getPort()," , This client close chat room %s:%d \n");
                    break;
                } else {
                    for (ClientHandler clientJoin:clients) {
                        clientJoin.clientSocket.getOutputStream().write((command +"\n").getBytes());
                        clientJoin.clientSocket.getOutputStream().flush();
                    }
                }
                clientSocket.getOutputStream().flush();
            }
            sc.close();
            clientSocket.close();
        } catch (Exception e){

        }
    }

}