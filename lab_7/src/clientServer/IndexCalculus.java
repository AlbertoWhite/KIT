package clientServer;

import kripthography.KriptoAlgorithms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

public class IndexCalculus extends JFrame {
    private KriptoAlgorithms kriptoAlgorithms = new KriptoAlgorithms();
    private JTextField jtfY;
    private JTextField jtfA;
    private JTextField jtfP;
    private JTextArea jtaTextAreaMessage;
    private Long inputY;
    private Long inputA;
    private Long inputP;
    public IndexCalculus() {

            setBounds(600, 300, 500, 200);
            setTitle("Алгоритм исчисления порядка");
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            jtaTextAreaMessage = new JTextArea();
            jtaTextAreaMessage.setEditable(false);
            jtaTextAreaMessage.setLineWrap(true);
            JScrollPane jsp = new JScrollPane(jtaTextAreaMessage);
            add(jsp, BorderLayout.CENTER);

            JPanel bottomPanel = new JPanel(new BorderLayout());
            add(bottomPanel, BorderLayout.SOUTH);
            JButton jbFound = new JButton("Найти х");
            bottomPanel.add(jbFound, BorderLayout.NORTH);

            JPanel box = new JPanel();
            JPanel labelBox = new JPanel();
            JPanel textBox = new JPanel();
            labelBox.setLayout(new BoxLayout(labelBox, BoxLayout.X_AXIS));
            textBox.setLayout(new BoxLayout(textBox, BoxLayout.X_AXIS));
            box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
            box.add(labelBox);
            box.add(textBox);

            JLabel labelY = new JLabel("y = ");
            JLabel labelA = new JLabel("a^x  mod  p");
            labelBox.add(labelY);
            labelBox.add(labelA);

            jtfY = new JTextField(" 0 ");
            JLabel labelR = new JLabel(" = ");
            jtfA = new JTextField(" 0 ");
            JLabel labelX = new JLabel("^x mod ");
            jtfP = new JTextField(" 0 ");
            textBox.add(jtfY);
            textBox.add(labelR);
            textBox.add(jtfA);
            textBox.add(labelX);
            textBox.add(jtfP);

            bottomPanel.add(box, BorderLayout.CENTER);
            jbFound.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // если имя клиента, и сообщение непустые, то отправляем сообщение
                    if ((!jtfY.getText().trim().isEmpty()) && (!jtfA.getText().trim().isEmpty()) && (!jtfP.getText().trim().isEmpty())) {
                        if((kriptoAlgorithms.isDigit(jtfY.getText())) && (kriptoAlgorithms.isDigit(jtfA.getText())) && (kriptoAlgorithms.isDigit(jtfP.getText())))
                        {
                            inputY = Long.parseLong(jtfY.getText());
                            inputA = Long.parseLong(jtfA.getText());
                            inputP = Long.parseLong(jtfP.getText());
                            jtfY.grabFocus();
                            jtfA.grabFocus();
                            jtfP.grabFocus();
                            found();
                        }

                    }
                }
            });
            // при фокусе поле сообщения очищается

        jtfY.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jtfY.setText("");
            }
        });
        jtfA.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jtfA.setText("");
            }
        });
        jtfP.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jtfP.setText("");
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

    private void found()
    {
        int e = 1;
        int t = 3;
        ArrayList<Integer> s;
        if(inputP > 50)
        {
            if(inputP < 120)
                t = 7;
            else
                t = 10;
            e = 5;
        }
        s = kriptoAlgorithms.getPrimes(t);
        System.out.println("S: ");
        for (Integer sElem : s) {
            System.out.print(sElem + " ");
        }
        System.out.println(" ");
        HashMap<Integer,ArrayList<Integer>> smoothList = kriptoAlgorithms.getSmoothList(t + e, inputA, inputP, s);
        System.out.println("SmoothList:");
        for (Integer k : smoothList.keySet()) {
            System.out.print("(k : " + k + ") ");
            ArrayList<Integer> tempSmooth = smoothList.get(k);
            for (int i = 0; (i < tempSmooth.size()) && (i < s.size()); i ++) {
                Integer ci = tempSmooth.get(i);
                Integer tempS = s.get(i);
                //System.out.print(" c:" + ci + ", s: " + tempS);
                for(int j = ci; j > 0; j --)
                    System.out.print(tempS + " * ");
            }
            System.out.println(" ");
        }
        ArrayList<Integer> logS = kriptoAlgorithms.getLogList(smoothList, s, inputA);
        HashMap<Integer,ArrayList<Integer>> smoothListForYA = kriptoAlgorithms.getSmoothList4(inputY, inputA, inputP, s);
        Long x = kriptoAlgorithms.calculateX(smoothListForYA, logS, inputP, inputA, s);
       // Long x;
        jtaTextAreaMessage.append(inputY.toString() + " = " + inputA + "^" + x + " mod " + inputP + ". (x = " + x + ")");
        // добавляем строку перехода
        jtaTextAreaMessage.append("\n");
    }
}