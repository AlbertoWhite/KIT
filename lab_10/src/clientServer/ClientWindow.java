package clientServer;

import cryptography.CryptoAlgorithms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class ClientWindow extends JFrame {
    // адрес сервера
    private static final String SERVER_HOST = "localhost";
    // порт
    private static final int SERVER_PORT = 3443;
    // клиентский сокет
    private Socket clientSocket;
    // входящее сообщение
    private Scanner inMessage;
    private CryptoAlgorithms cryptoAlgorithms = new CryptoAlgorithms();
    // исходящее сообщение
    private PrintWriter outMessage;
    // следующие поля отвечают за элементы формы
    private JTextField jtfMessage;
    private JTextField jtfName;
    private JTextArea jtaTextAreaMessage;
    // имя клиента
    private String clientName = "";
    private boolean flag = false;
    protected HashMap<String, Long> tempKeys = new HashMap<>();
    protected HashMap<String, Long> secretKeys = new HashMap<>();
    protected boolean flagKeysIsReady = false;
    public String getClientName() {
        return clientName;
    }

    // конструктор
    public ClientWindow(String name) {
        try {
            // подключаемся к серверу
            clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
            inMessage = new Scanner(clientSocket.getInputStream());
            outMessage = new PrintWriter(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Cant connect to the server :c");
            //sleep()
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
            // подключаемся к серверу
            try {
                clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
                inMessage = new Scanner(clientSocket.getInputStream());
                outMessage = new PrintWriter(clientSocket.getOutputStream());

            } catch (IOException exep) {
                System.out.println("Absolutely cant connect to the server :c");
                flag = true;
            }
        }
        if (!flag)// flag = true if we cant connected with server->need to close all
        {
            clientName = name;
            outMessage.println("##save##name##");
            outMessage.flush();
            outMessage.println(clientName);
            outMessage.flush();

            // Задаём настройки элементов на форме
            setBounds(600, 300, 600, 300);
            setTitle(clientName);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            jtaTextAreaMessage = new JTextArea();
            jtaTextAreaMessage.setEditable(false);
            jtaTextAreaMessage.setLineWrap(true);
            JScrollPane jsp = new JScrollPane(jtaTextAreaMessage);
            add(jsp, BorderLayout.CENTER);
            JLabel jlNumberOfClients = new JLabel("Количество клиентов в чате: ");
            add(jlNumberOfClients, BorderLayout.NORTH);

            JPanel bottomPanel = new JPanel(new BorderLayout());
            add(bottomPanel, BorderLayout.SOUTH);
            JButton jbSendMessage = new JButton("Отправить");
            bottomPanel.add(jbSendMessage, BorderLayout.CENTER);

            JPanel namePanel = new JPanel(new BorderLayout());
            add(namePanel, BorderLayout.EAST);

            jtfMessage = new JTextField("Введите ваше сообщение: ");
            bottomPanel.add(jtfMessage, BorderLayout.NORTH);

            JButton jbListofUsers = new JButton("Список участников");
            namePanel.add(jbListofUsers, BorderLayout.SOUTH);
            jbSendMessage.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // если имя клиента, и сообщение непустые, то отправляем сообщение
                    if (!jtfMessage.getText().trim().isEmpty() && (!clientName.isEmpty() && flag)) {//flag = true if sms was written once -> sms != введите sms
                        jtaTextAreaMessage.append(clientName + ":" + jtfMessage.getText());
                        jtaTextAreaMessage.append("\n");
                        sendCode();
                        flag = true;
                        jtfMessage.grabFocus();

                    }
                }
            });
            jbListofUsers.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    outMessage.println("##getlist##names##");
                    outMessage.flush();
                }
            });
        jtfMessage.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    jtfMessage.setText("");
                    flag = true;// -> sms != введите sms
                }
            });
   // в отдельном потоке начинаем работу с сервером
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            boolean flagSystemMsg = false;
                            // если есть входящее сообщение
                            if (inMessage.hasNext()) {
                                // считываем его
                                String inMes = inMessage.nextLine();
                                String clientsInChat = "Клиентов в чате = ";
                                if (inMes.indexOf(clientsInChat) == 0) {
                                    jlNumberOfClients.setText(inMes);
                                }
                                else if (inMes.equalsIgnoreCase("##ping##"))
                                {
                                    outMessage.println("##ping##answer##");
                                    outMessage.flush();
                                    outMessage.println(clientName);
                                    outMessage.flush();
                                }
                                else {
                                           if(inMes.startsWith("."))
                                           {
                                               String temp = inMes.substring(1, inMes.length());
                                               String nameFrom = temp.substring(0, temp.lastIndexOf(":"));
                                               String msg = temp.substring(temp.lastIndexOf(":") + 1);

                                               if(!nameFrom.equalsIgnoreCase(clientName)) {
                                                   String decodedMsg = cryptoAlgorithms.decode(msg);
                                                   jtaTextAreaMessage.append(nameFrom + ":" + decodedMsg);
                                                   jtaTextAreaMessage.append("\n");
                                               }
                                           }
                                           else {
                                               jtaTextAreaMessage.append(inMes);
                                               jtaTextAreaMessage.append("\n");
                                           }
                                      }
                                }
                        }
                    } catch (Exception e) {
                    }
                }
            }).start();
            // добавляем обработчик события закрытия окна клиентского приложения
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    try {
                        // здесь проверяем, что имя клиента непустое и не равно значению по умолчанию
                        if (!clientName.isEmpty() && clientName != "Введите ваше имя: ") {
                            outMessage.println(clientName + " вышел из чата!");
                        } else {
                            outMessage.println("Участник вышел из чата, так и не представившись!");
                        }
                        // отправляем служебное сообщение, которое является признаком того, что клиент вышел из чата
                        outMessage.println(clientName + ": ##session##end##");
                        outMessage.flush();
                        outMessage.close();
                        inMessage.close();
                        clientSocket.close();
                    } catch (IOException exc) {

                    }
                }
            });
            // отображаем форму
            setVisible(true);
        }
    }
    // отправка сообщения
    public void sendCode()
    {
        String message = jtfMessage.getText();
        String secretMessage = cryptoAlgorithms.code(message);// for himself?
        String messageStr = "." + clientName + ":" + secretMessage;
        outMessage.println(messageStr);
        outMessage.flush();
        jtfMessage.setText("");
     }
}

