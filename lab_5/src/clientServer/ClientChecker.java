package clientServer;

import kripthography.KriptoAlgorithms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

public class ClientChecker extends JFrame {
    private Scanner inMessage;
    private KriptoAlgorithms kriptoAlgorithms = new KriptoAlgorithms();
    private JTextField jtfMessage;
    private JTextField jtfN;
    private JTextField jtfD;
    private JTextArea jtaTextAreaMessage;
    protected Long secretC;
    protected Long publicN;
    protected Long publicd;
    public ClientChecker() {

            setBounds(600, 300, 500, 200);
            setTitle("Checker");
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            jtaTextAreaMessage = new JTextArea();
            jtaTextAreaMessage.setEditable(false);
            jtaTextAreaMessage.setLineWrap(true);
            JScrollPane jsp = new JScrollPane(jtaTextAreaMessage);
            add(jsp, BorderLayout.CENTER);

            JPanel bottomPanel = new JPanel(new BorderLayout());
            add(bottomPanel, BorderLayout.SOUTH);
            JButton jbSendMessage = new JButton("Проверить");
            bottomPanel.add(jbSendMessage, BorderLayout.CENTER);
            JButton jbSave = new JButton("Сохранить N, d");
            bottomPanel.add(jbSave, BorderLayout.EAST);

            jtfMessage = new JTextField("Введите ваше сообщение: ");

            bottomPanel.add(jtfMessage, BorderLayout.NORTH);
            JPanel box = new JPanel();
            box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
            JLabel labelN = new JLabel("N:");
            jtfN = new JTextField("    0    ");
            jtfN.setSize(70, 10);
            JLabel labelD = new JLabel("d:");
            jtfD = new JTextField("    0    ");
        jtfD.setSize(70, 10);
            box.add(labelN);
            add(box, BorderLayout.EAST);
            box.add(jtfN);
            box.add(labelD);
            box.add(jtfD);
            // обработчик события нажатия кнопки отправки сообщения
            jbSendMessage.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // если имя клиента, и сообщение непустые, то отправляем сообщение
                    if (!jtfMessage.getText().trim().isEmpty()) {
                        String message =  jtfMessage.getText();
                        checkCode(message);
                        jtfMessage.grabFocus();
                    }
                }
            });
            jbSave.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                   String Nstr = jtfN.getText();
                   String Dstr = jtfD.getText();
                   if(kriptoAlgorithms.isDigit(Dstr))
                       publicd = Long.parseLong(Dstr);
                   if(kriptoAlgorithms.isDigit(Nstr))
                    publicN = Long.parseLong(Nstr);

                   }
            });
            // при фокусе поле сообщения очищается
            jtfMessage.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    jtfMessage.setText("");
                }
            });
        jtfN.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jtfN.setText("");
            }
        });
        jtfD.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jtfD.setText("");
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
        String mess = msg.substring(0, msg.indexOf(","));
        String s = msg.substring(msg.indexOf(",") + 1, msg.length() - 1);
        String result = kriptoAlgorithms.check(mess, s, publicd, publicN);
        jtaTextAreaMessage.append(message + " - " + result);
        // добавляем строку перехода
        jtaTextAreaMessage.append("\n");

    }
}