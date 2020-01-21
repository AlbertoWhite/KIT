package clientServer;

import kripthography.KriptoAlgorithms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

public class Shop extends JFrame {
    private Scanner inMessage;
    private KriptoAlgorithms kriptoAlgorithms = new KriptoAlgorithms();
   // private JTextField jtfMessage;
    private JTextField jtfSumm;
    private Long summ;
    private JTextArea jtaTextAreaMessage;
    protected Long secretC;
    private Long n;
    private Long lastN;
    private String money;
    private Label shopMoney;
    private Label userMoney;
    //protected Long publicN;
    //protected Long publicd;
    private Bank bank;
    public Shop() {
            bank = new Bank();
            bank.setBankAccount(1000l);
            n = kriptoAlgorithms.getRandomLong(2l, bank.publicN);

            setBounds(600, 300, 500, 300);
            setTitle("Shop");
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            jtaTextAreaMessage = new JTextArea();
            jtaTextAreaMessage.setEditable(false);
            jtaTextAreaMessage.setLineWrap(true);
            JScrollPane jsp = new JScrollPane(jtaTextAreaMessage);
            add(jsp, BorderLayout.CENTER);

            JPanel bottomPanel = new JPanel(new BorderLayout());
            add(bottomPanel, BorderLayout.SOUTH);
            JButton jbPay = new JButton("Заплатить магазину");
            bottomPanel.add(jbPay, BorderLayout.CENTER);
            JButton jbSave = new JButton("Снять купюру");
            bottomPanel.add(jbSave, BorderLayout.EAST);

           // jtfMessage = new JTextField("Введите сумму: ");

            //bottomPanel.add(jtfMessage, BorderLayout.NORTH);
            JPanel box = new JPanel();
            box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
            JLabel labelSumm = new JLabel("Сумма:");
            jtfSumm = new JTextField("    0    ");
            jtfSumm.setSize(70, 10);
        JLabel labelNumber = new JLabel("n:");
        JTextField jtfNumber = new JTextField(n.toString());
        jtfNumber.setSize(70, 10);

            box.add(labelSumm);
            add(box, BorderLayout.EAST);
            box.add(jtfSumm);
        box.add(labelNumber);
        box.add(jtfNumber);
        Label shopM = new Label("shop money: ");
        shopMoney = new Label(bank.getShopAccount().toString());
        Label userM = new Label("user money: ");
        userMoney = new Label(bank.getBankAccount().toString());
        box.add(shopM);
        box.add(shopMoney);
        box.add(userM);
        box.add(userMoney);

        // обработчик события нажатия кнопки отправки сообщения
            jbPay.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // если имя клиента, и сообщение непустые, то отправляем сообщение
                    if (summ > 0) {
                        //String message =  jtfMessage.getText();
                        if(! bank.checkUsed(lastN, summ))
                        {
                            checkCode(money);
                        }
                        else
                        {
                            jtaTextAreaMessage.append(money + " - already used!");
                            // добавляем строку перехода
                            jtaTextAreaMessage.append("\n");
                        }
                        shopMoney.setText(bank.getShopAccount().toString());
                        shopMoney.repaint();
                        userMoney.setText(bank.getBankAccount().toString());
                        userMoney.repaint();
                        //jtfMessage.grabFocus();
                    }
                }
            });
            jbSave.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                   String summStr = jtfSumm.getText();
                   String numberStr = jtfNumber.getText();
                   if(kriptoAlgorithms.isDigit(summStr))
                       summ = Long.parseLong(summStr);
                   if(kriptoAlgorithms.isDigit(numberStr))
                    n = Long.parseLong(numberStr);
                    String s = bank.getMoney(kriptoAlgorithms.oneWayFunction(n), summ);
                    money = "<" + n + "," + s + ">";
                    jtaTextAreaMessage.append("money: "+ money);
                    // добавляем строку перехода
                    jtaTextAreaMessage.append("\n");
                    lastN = n;
                    n = kriptoAlgorithms.getRandomLong(2l, bank.publicN);
                    jtfNumber.setText(n.toString());
                    jtfNumber.repaint();
                    jtfNumber.grabFocus();
                    jtfSumm.grabFocus();
                    userMoney.setText(bank.getBankAccount().toString());
                    userMoney.repaint();
                    shopMoney.setText(bank.getShopAccount().toString());
                    shopMoney.repaint();
                }
            });
            // при фокусе поле сообщения очищается
           /* jtfMessage.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    jtfMessage.setText("");
                }
            });*/
        jtfSumm.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jtfSumm.setText("");
            }
        });
            // добавляем обработчик события закрытия окна клиентского приложения
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);

                }
            });
            // отображаем форму
            setVisible(true);
       // }
    }

    public void checkCode(String message)
    {
        String msg = message.substring(1);
        String nStr = msg.substring(0, msg.indexOf(","));
        String sf = msg.substring(msg.indexOf(",") + 1, msg.length() - 1);
        String result = kriptoAlgorithms.check(nStr, sf, bank.d, bank.publicN);
        jtaTextAreaMessage.append(message + " - " + result);
        // добавляем строку перехода
        jtaTextAreaMessage.append("\n");
        if(result.equalsIgnoreCase("true"))
        {
            jtaTextAreaMessage.append("Платеж успешно прошел");
            // добавляем строку перехода
            jtaTextAreaMessage.append("\n");
        }

    }
}