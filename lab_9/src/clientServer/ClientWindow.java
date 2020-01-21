package clientServer;

import kripthography.KriptoAlgorithms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

public class ClientWindow extends JFrame {
    private Scanner inMessage;
    private KriptoAlgorithms kriptoAlgorithms = new KriptoAlgorithms();

    private JTextField jtfMessage;
    private JTextArea jtaTextAreaMessage;
    protected Long secretC;
    public Long publicN;

    public ClientWindow() {
            setBounds(600, 300, 400, 300);
            setTitle("Client");
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            jtaTextAreaMessage = new JTextArea();
            jtaTextAreaMessage.setEditable(false);
            jtaTextAreaMessage.setLineWrap(true);
            JScrollPane jsp = new JScrollPane(jtaTextAreaMessage);
            add(jsp, BorderLayout.CENTER);
            JPanel bottomPanel = new JPanel(new BorderLayout());
            add(bottomPanel, BorderLayout.SOUTH);
            JButton jbSendMessage = new JButton("Подписать");
            bottomPanel.add(jbSendMessage, BorderLayout.CENTER);


            JPanel namePanel = new JPanel(new BorderLayout());
            add(namePanel, BorderLayout.EAST);

            jtfMessage = new JTextField("Введите ваше сообщение: ");
            bottomPanel.add(jtfMessage, BorderLayout.NORTH);

            jbSendMessage.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // если имя клиента, и сообщение непустые, то отправляем сообщение
                    if (!jtfMessage.getText().trim().isEmpty()) {//flag = true if sms was written once -> sms != введите sms
                        String message =  jtfMessage.getText();
                        sendCode();
                        jtfMessage.grabFocus();
                    }
                }
            });

            jtfMessage.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    jtfMessage.setText("");
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
    }

    public void sendCode()
    {
            String message = jtfMessage.getText();
            String secretMessage = kriptoAlgorithms.code(message, publicN, secretC);
            jtaTextAreaMessage.append(secretMessage);
            jtaTextAreaMessage.append("\n");
    }
}