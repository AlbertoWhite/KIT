package clientServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class ClientHandler implements Runnable {

    // экземпляр нашего сервера
    private Server server;
    // исходящее сообщение
    private PrintWriter outMessage;
    // входящее собщение
    private Scanner inMessage;
    private static final String HOST = "localhost";
    private static final int PORT = 3443;
    // клиентский сокет
    private boolean flagName = false;
    private boolean flagKey = false;
    private boolean flagNeedToDelate = false;
    private Socket clientSocket = null;
    private static int clients_count = 0;
    private static ArrayList<String> clientsMessages = new ArrayList<String>();
    private static ArrayList<String> clientsNames = new ArrayList<String>();
    private static HashMap<String, String> clientsStatuses = new HashMap<String, String>();

    private static long currentTime = System.currentTimeMillis(), prevTime= System.currentTimeMillis() - 10;

    //принимает клиентский сокет и сервер
    public ClientHandler(Socket socket, Server server) {
        try {
            clients_count++;
            this.server = server;
            this.clientSocket = socket;
            this.outMessage = new PrintWriter(socket.getOutputStream());
            this.inMessage = new Scanner(socket.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    // отправляем сообщение
    public void sendMsg(String msg) {
        try {
            outMessage.println(msg);
            outMessage.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void run() {
        try {
            while (true) {
                // сервер отправляет сообщение
                server.sendMessageToAllClients("Новый участник вошёл в чат!");
                server.sendMessageToAllClients("Клиентов в чате = " + clients_count);
                break;
            }

            while (true) {
                // Если от клиента пришло сообщение
                boolean flIsSystemMessage = false;
                if (inMessage.hasNext()) {
                    String clientMessage = inMessage.nextLine();
                    if (clientMessage.endsWith("##session##end##")) {
                        flIsSystemMessage = true;
                        flagNeedToDelate = true;
                        for (String nameUs : clientsNames) {
                            if (clientMessage.startsWith(nameUs + ":")) {
                                clientsNames.remove(nameUs);
                                clientsStatuses.remove(nameUs);
                                break;
                            }
                        }
                        break;
                    }
                    if(flagName)
                    {
                        flIsSystemMessage = true;
                        if(clientsNames.contains(clientMessage)) // name is in list
                        {
                            sendMsg("Name already used, try again");
                        }
                        else
                        {
                            clientsStatuses.put(clientMessage, "online");
                            clientsNames.add(clientMessage);
                        }
                        flagName = false;
                    }

                    if (clientMessage.equalsIgnoreCase("##ping##answer##")) {
                        flIsSystemMessage = true;
                        if (inMessage.hasNext())
                        {
                            clientMessage = inMessage.nextLine();//name
                        }
                        clientsStatuses.put(clientMessage, "online");
                    }
                    if (clientMessage.equalsIgnoreCase("##save##name##")) {
                        flIsSystemMessage = true;
                        flagName = true;
                    }
                    if (clientMessage.equalsIgnoreCase("##getlist##names##")) {
                        flIsSystemMessage = true;

                        sendMsg("Names of active users:");

                        for (String nameUs : clientsNames) {
                            sendMsg(nameUs + " : " + clientsStatuses.get(nameUs));
                        }
                    }

                    // отправляем данное сообщение всем клиентам
                    if(!flIsSystemMessage){
                        server.sendMessageToAllClients(clientMessage);
                        clientsMessages.add(clientMessage);
                        for (String nameUs : clientsNames) {
                            if(clientMessage.startsWith(nameUs + ":"))
                            {
                                clientsStatuses.put(nameUs, "online");
                            }
                        }
                    }

                    currentTime = System.currentTimeMillis();
                    if(currentTime > prevTime + 1000)
                    {
                        prevTime = System.currentTimeMillis();//update
                        for (String nameUs : clientsNames)
                        {
                            clientsStatuses.put(nameUs, "offline");
                        }
                        server.sendMessageToAllClients("##ping##");
                    }
                    flIsSystemMessage = false;
                }
                // останавливаем выполнение потока на 100 мс
                Thread.sleep(100);
            }
        }
        catch (InterruptedException ex) {
            close();
            ex.printStackTrace();
        }
        finally {
            close();
        }
    }

    // клиент выходит из чата
    public void close() {
        // удаляем клиента из списка
        server.removeClient(this);
        clients_count--;
        server.sendMessageToAllClients("Клиентов в чате = " + clients_count);
    }
}
