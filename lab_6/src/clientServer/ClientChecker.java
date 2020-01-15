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
    private JTextField jtfY;
    private JTextArea jtaTextAreaMessage;
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
            JButton jbSave = new JButton("Сохранить Y");
            bottomPanel.add(jbSave, BorderLayout.EAST);

            jtfMessage = new JTextField("Введите ваше сообщение: ");

            bottomPanel.add(jtfMessage, BorderLayout.NORTH);
            JPanel box = new JPanel();
            box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
            JLabel labelY = new JLabel("Y:");
            jtfY = new JTextField("    0    ");
            jtfY.setSize(70, 10);
            box.add(labelY);
            add(box, BorderLayout.EAST);
            box.add(jtfY);
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
                   String Ystr = jtfY.getText();
                   if(kriptoAlgorithms.isDigit(Ystr))
                       publicd = Long.parseLong(Ystr);
                   }
            });
            // при фокусе поле сообщения очищается
            jtfMessage.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    jtfMessage.setText("");
                }
            });
        jtfY.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jtfY.setText("");
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
        String r = msg.substring(msg.indexOf(",") + 1, msg.lastIndexOf(","));
        String s = msg.substring(msg.lastIndexOf(",") + 1, msg.length() - 1);
        String result = kriptoAlgorithms.check(mess, r, s, publicd);
        jtaTextAreaMessage.append(message + " - " + result);
        // добавляем строку перехода
        jtaTextAreaMessage.append("\n");

    }
}