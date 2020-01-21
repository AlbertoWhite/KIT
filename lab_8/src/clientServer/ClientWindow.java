package clientServer;

import kripthography.KriptoAlgorithms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
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
    //protected HashMap<String, String> tempKeys = new HashMap<>();
    //protected boolean flagKeysIsReady = false;
    protected Long secretC;
    protected Long secretD;
    private Integer cart1;//3
    private Integer cart2;//7
    private Integer cart3;//туз
    private Integer cartN1;
    private Integer cartN2;
    private Integer cartN3;
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
        if (!flag)
        {
            clientName = name;
            sendSystemInfo("##save##name##", clientName);
            // Задаём настройки элементов на форме
            setBounds(600, 300, 300, 200);
            setTitle("Ментальный покер: " + clientName);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            jtaTextAreaMessage = new JTextArea();
            jtaTextAreaMessage.setEditable(false);
            jtaTextAreaMessage.setLineWrap(true);
            JScrollPane jsp = new JScrollPane(jtaTextAreaMessage);
            add(jsp, BorderLayout.CENTER);

            JPanel bottomPanel = new JPanel(new BorderLayout());
            add(bottomPanel, BorderLayout.SOUTH);
            JButton jbSendMessage = new JButton("Раздать");
            bottomPanel.add(jbSendMessage, BorderLayout.CENTER);


            JPanel namePanel = new JPanel(new BorderLayout());
            add(namePanel, BorderLayout.EAST);
            jbSendMessage.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cart1 = kriptoAlgorithms.getRandom(1l, kriptoAlgorithms.p);
                    cart2 = kriptoAlgorithms.getRandom(1l, kriptoAlgorithms.p);
                    cart3 = kriptoAlgorithms.getRandom(1l, kriptoAlgorithms.p);
                    while(cart1 == cart2)
                    {
                        cart2 = kriptoAlgorithms.getRandom(1l, kriptoAlgorithms.p);
                    }
                    while ((cart1 == cart3) || (cart2 == cart3))
                    {
                        cart3 = kriptoAlgorithms.getRandom(1l, kriptoAlgorithms.p);
                    }
                    outMessage.println("."+clientName+".0."+cart1 + "." + cart2+"."+cart3);
                    outMessage.flush();
                    System.out.println("Send: " + "."+clientName+".0."+cart1 + "." + cart2+"."+cart3);
                    step1();
                }
            });
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
                                           if(inMes.startsWith("."))
                                           {
                                               String msg = inMes.substring(1, inMes.length());
                                               //System.out.println("msg:" + msg);
                                               String from = msg.substring(0, msg.indexOf("."));
                                               //System.out.println("from:" + from + ", that:" + clientName+ ", check:" + !from.equalsIgnoreCase(clientName));
                                               if(!from.equalsIgnoreCase(clientName))
                                               {
                                                   String mess = msg.substring(msg.indexOf("."));
                                                   if(mess.startsWith(".0."))
                                                   {
                                                       String message = mess.substring(3, mess.length());
                                                       String cart1S = message.substring(0, message.indexOf("."));
                                                       String cart2S = message.substring(message.indexOf(".") + 1, message.lastIndexOf("."));
                                                       String cart3S = message.substring(message.lastIndexOf(".") + 1);
                                                       cartN1 = Integer.parseInt(cart1S);
                                                       cartN2 = Integer.parseInt(cart2S);
                                                       cartN3 = Integer.parseInt(cart3S);
                                                       System.out.println("have carts: " + cartN1 + ", " + cartN2 + ", " + cartN3);
                                                   }
                                                   if(mess.startsWith(".1."))
                                                   {
                                                       String message = mess.substring(3, mess.length());
                                                       String cart1S = message.substring(0, message.indexOf("."));
                                                       String cart2S = message.substring(message.indexOf(".") + 1, message.lastIndexOf("."));
                                                       String cart3S = message.substring(message.lastIndexOf(".") + 1);
                                                       Long tmp1 = Long.parseLong(cart1S);
                                                       Long tmp2 = Long.parseLong(cart2S);
                                                       Long tmp3 = Long.parseLong(cart3S);
                                                       ArrayList<Long> mixedCards = kriptoAlgorithms.mixCards(tmp1, tmp2, tmp3);
                                                       outMessage.println("."+clientName+".2."+mixedCards.get(0));
                                                       outMessage.flush();
                                                       System.out.println("Send: " + "."+clientName+".2."+mixedCards.get(0));

                                                       Long v1 = kriptoAlgorithms.degreeByMod(mixedCards.get(1), secretC, kriptoAlgorithms.p);
                                                       Long v2 = kriptoAlgorithms.degreeByMod(mixedCards.get(2), secretC, kriptoAlgorithms.p);
                                                       ArrayList<Long> mixed2Cards = kriptoAlgorithms.mixCards(v1, v2);
                                                       outMessage.println("."+clientName+".3."+mixed2Cards.get(0) + "."+mixed2Cards.get(1));
                                                       outMessage.flush();
                                                       System.out.println("Send: " + "."+clientName+".3."+mixed2Cards.get(0) + "."+mixed2Cards.get(1));
                                                   }
                                                   if(mess.startsWith(".2."))
                                                   {
                                                       String message = mess.substring(3, mess.length());
                                                       Long number = Long.parseLong(message);
                                                       Long cart = kriptoAlgorithms.degreeByMod(number, secretD, kriptoAlgorithms.p);
                                                       System.out.println("cart: " + cart);
                                                       if(cart.toString().equalsIgnoreCase(cart1.toString()))//3
                                                       {
                                                           jtaTextAreaMessage.append("Ваша карта: 3");
                                                           // добавляем строку перехода
                                                           jtaTextAreaMessage.append("\n");
                                                       }
                                                       if(cart.toString().equalsIgnoreCase(cart2.toString()))//7
                                                       {
                                                           jtaTextAreaMessage.append("Ваша карта: 7");
                                                           // добавляем строку перехода
                                                           jtaTextAreaMessage.append("\n");
                                                       }
                                                       if(cart.toString().equalsIgnoreCase(cart3.toString()))//туз
                                                       {
                                                           jtaTextAreaMessage.append("Ваша карта: туз");
                                                           // добавляем строку перехода
                                                           jtaTextAreaMessage.append("\n");
                                                       }
                                                   }
                                                   if(mess.startsWith(".3."))
                                                   {
                                                       String message = mess.substring(3, mess.length());
                                                       String cart1S = message.substring(0, message.indexOf("."));
                                                       String cart2S = message.substring(message.indexOf(".") + 1);
                                                       Long tmp1 = Long.parseLong(cart1S);
                                                       Long tmp2 = Long.parseLong(cart2S);

                                                       ArrayList<Long> mixedCards = kriptoAlgorithms.mixCards(tmp1, tmp2);
                                                       Long openedCard = kriptoAlgorithms.degreeByMod(mixedCards.get(0), secretD, kriptoAlgorithms.p);


                                                       outMessage.println("."+clientName+".4."+openedCard);
                                                       outMessage.flush();
                                                       System.out.println("Send: " + "."+clientName+".4."+openedCard);
                                                   }
                                                   if(mess.startsWith(".4."))
                                                   {
                                                       String message = mess.substring(3, mess.length());
                                                       Long tmp1 = Long.parseLong(message);
                                                       Long cart = kriptoAlgorithms.degreeByMod(tmp1, secretD, kriptoAlgorithms.p);

                                                       System.out.println("cart: " + cart);
                                                       if(cart.toString().equalsIgnoreCase(cartN1.toString()))//3
                                                       {
                                                           jtaTextAreaMessage.append("Ваша карта: 3");
                                                           // добавляем строку перехода
                                                           jtaTextAreaMessage.append("\n");
                                                       }
                                                       if(cart.toString().equalsIgnoreCase(cartN2.toString()))//7
                                                       {
                                                           jtaTextAreaMessage.append("Ваша карта: 7");
                                                           // добавляем строку перехода
                                                           jtaTextAreaMessage.append("\n");
                                                       }
                                                       if(cart.toString().equalsIgnoreCase(cartN3.toString()))//туз
                                                       {
                                                           jtaTextAreaMessage.append("Ваша карта: туз");
                                                           // добавляем строку перехода
                                                           jtaTextAreaMessage.append("\n");
                                                       }
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
    private void step1()
    {
        Long u1 = kriptoAlgorithms.degreeByMod(Long.parseLong(cart1.toString()), secretC, kriptoAlgorithms.p);
        Long u2 = kriptoAlgorithms.degreeByMod(Long.parseLong(cart2.toString()), secretC, kriptoAlgorithms.p);
        Long u3 = kriptoAlgorithms.degreeByMod(Long.parseLong(cart3.toString()), secretC, kriptoAlgorithms.p);
        System.out.println("u: "+ u1 + ", " + u2 + ", " + u3);

        ArrayList<Long> mixedCards = kriptoAlgorithms.mixCards(u1, u2, u3);
        outMessage.println("."+clientName+".1."+mixedCards.get(0) + "." + mixedCards.get(1)+"."+mixedCards.get(2));
        outMessage.flush();
        System.out.println("Send: " + "."+clientName+".1."+mixedCards.get(0) + "." + mixedCards.get(1)+"."+mixedCards.get(2));
    }
    private void sendSystemInfo(String message1, String message2)
   {
       outMessage.println(message1);
       outMessage.flush();
       outMessage.println(message2);
       outMessage.flush();
   }
}