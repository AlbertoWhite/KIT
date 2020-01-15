package clientServer;

import kripthography.KriptoAlgorithms;

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
    private KriptoAlgorithms kriptoAlgorithms = new KriptoAlgorithms();
    // исходящее сообщение
    private PrintWriter outMessage;
    // следующие поля отвечают за элементы формы
    private JTextField jtfMessage;
    private JTextField jtfName;
    private JTextArea jtaTextAreaMessage;
    // имя клиента
    private String clientName = "";
    private boolean flag = false;
    protected HashMap<String, String> tempKeys = new HashMap<>();
    protected boolean flagKeysIsReady = false;
    protected Long secretC;
    protected Long publicN;
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
            sendSystemInfo("##save##name##", clientName);
            // Задаём настройки элементов на форме
            setBounds(600, 300, 600, 500);
            setTitle("Client");
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


            // обработчик события нажатия кнопки отправки сообщения
            jbSendMessage.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // если имя клиента, и сообщение непустые, то отправляем сообщение
                    if (!jtfMessage.getText().trim().isEmpty() && (!clientName.isEmpty() && flag)) {//flag = true if sms was written once -> sms != введите sms
                        String message =  jtfMessage.getText();
                        //if((kriptoAlgorithms.isDigit(message)) && (Long.parseLong(message) <= KriptoAlgorithms.p)) {
                            sendCode();
                        //}
                       // else
                        //{
                        //    sendCharCode();
                        //}
                        flag = true;
                        jtfMessage.grabFocus();
                        jtaTextAreaMessage.append(clientName + ":" + message);
                        // добавляем строку перехода
                        jtaTextAreaMessage.append("\n");
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
            // при фокусе поле сообщения очищается
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
                                    sendSystemInfo("##ping##answer##", clientName);
                                }
                                else {
                                        if(inMes.equalsIgnoreCase("Public keys:")) // keys
                                        {
                                           tempKeys.clear();
                                           jtaTextAreaMessage.append(inMes);
                                        // добавляем строку перехода
                                        jtaTextAreaMessage.append("\n");
                                        if (inMessage.hasNext())
                                        {
                                            inMes = inMessage.nextLine();
                                             while (true)
                                            {
                                                if(inMes.equalsIgnoreCase("##finish##keys##"))
                                                {
                                                    flagKeysIsReady = true;
                                                    flagSystemMsg = true;
                                                    inMes = inMessage.nextLine();
                                                    break;
                                                }
                                                if(!flagSystemMsg) {
                                                    jtaTextAreaMessage.append(inMes);
                                                    // добавляем строку перехода
                                                    jtaTextAreaMessage.append("\n");
                                                }
                                                flagSystemMsg = false;
                                                String name = inMes.substring(0, inMes.lastIndexOf(":"));
                                                String key = inMes.substring(inMes.lastIndexOf(":") + 1, inMes.length());
                                                tempKeys.put(name, key);

                                                if (inMessage.hasNext()) {
                                                    inMes = inMessage.nextLine();
                                                }
                                                else
                                                {
                                                    break;
                                                }
                                            }

                                         }
                                    }
                                    if(! (inMes.equalsIgnoreCase("##finish##keys##"))) { // message
                                           if(inMes.startsWith("."))
                                           {
                                               // parsing messages
                                               String temp = inMes.substring(1, inMes.length());
                                               String nameFrom = temp.substring(0, temp.lastIndexOf(":"));
                                               String nameTo = temp.substring(temp.lastIndexOf(":") + 1, temp.lastIndexOf("->"));
                                               String msg = temp.substring(temp.lastIndexOf("->") + 2, temp.length());

                                               //System.out.println("have msg from" + nameFrom + " to " + nameTo + ":" + msg);
                                               if((nameTo.equalsIgnoreCase(clientName))&&(!nameFrom.equalsIgnoreCase(nameTo)))
                                               {     // if message for me from smb
                                                   System.out.println("from:" + nameFrom + ", to:" + nameTo + ", msg:" + msg);
                                                   String decodedMsg = "";
                                                   //if(msg.lastIndexOf("D") == 0)
                                                   //{
                                                    //   decodedMsg = kriptoAlgorithms.decodeBytes(msg, secretC);
                                                   //}
                                                   //else
                                                  // {
                                                       decodedMsg = kriptoAlgorithms.decode(msg, secretC, publicN);
                                                  // }
                                                   System.out.println("open message: " + decodedMsg);
                                                   jtaTextAreaMessage.append(nameFrom + ":" + decodedMsg);
                                                   // добавляем строку перехода
                                                   jtaTextAreaMessage.append("\n");
                                               }
                                           }
                                           else { // system messages
                                               jtaTextAreaMessage.append(inMes);
                                               // добавляем строку перехода
                                               jtaTextAreaMessage.append("\n");
                                           }
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
    /*
    public void sendCharCode()
    {
        String message = jtfMessage.getText();
        for (String key : tempKeys.keySet()) {
            String secretMessage = kriptoAlgorithms.codeBytes(message, tempKeys.get(key));
            System.out.println("send charCode: " + key + ": " + secretMessage);
            send(key, secretMessage);
        }
    }*/
    // отправка сообщения
    public void sendCode()
    {
            String message = jtfMessage.getText();
            //Long messageLong = Long.parseLong(message);
            for (String key : tempKeys.keySet()) {
                String publicKey = tempKeys.get(key);
                String publicKeyN = publicKey.substring(0, publicKey.lastIndexOf("."));
                String publicKeyD = publicKey.substring(publicKey.lastIndexOf(".") + 1, publicKey.length());
                String secretMessage = kriptoAlgorithms.code(message, Long.parseLong(publicKeyN), Long.parseLong(publicKeyD));
                send(key, secretMessage);
        }
    }
    private void send(String key, String secretMessage)
    {
        String messageStr = "." + clientName + ":" + key + "->" + secretMessage;
        System.out.println("send " + messageStr);
        outMessage.println(messageStr);
        outMessage.flush();
        jtfMessage.setText("");
    }

    public void sendMsg(String message) {
        // формируем сообщение для отправки на сервер
        String messageStr = "##public##key##";
        // отправляем сообщение
        sendSystemInfo(messageStr, clientName + ":" + message);
    }

   public void updatePublicKeys()
   {
       flagKeysIsReady = false;
       outMessage.println("##getlist##keys##");
       outMessage.flush();

   }
   private void sendSystemInfo(String message1, String message2)
   {
       outMessage.println(message1);
       outMessage.flush();
       outMessage.println(message2);
       outMessage.flush();
   }
}