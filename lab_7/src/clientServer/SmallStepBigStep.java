package clientServer;

import kripthography.KriptoAlgorithms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class SmallStepBigStep extends JFrame {
    private KriptoAlgorithms kriptoAlgorithms = new KriptoAlgorithms();
    private JTextField jtfY;
    private JTextField jtfA;
    private JTextField jtfP;
    private JTextArea jtaTextAreaMessage;
    private Long inputY;
    private Long inputA;
    private Long inputP;
    public SmallStepBigStep() {

            setBounds(600, 300, 500, 200);
            setTitle("Шаг младенца, шаг великана");
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
        Long m = kriptoAlgorithms.getRandom(2l, inputP/2);
        Long k = kriptoAlgorithms.getRandom(2l, inputP/2);
        while(m * k < inputP)
        {
            System.out.println("m:" + m + " k:" + k + " m * k = " + m * k);
            k = kriptoAlgorithms.getRandom(2l, inputP/2);
        }
      //  m = 6l;
       // k = 4l;
        System.out.print("m:" + m + " k:" + k);
        ArrayList<Long> smallList = kriptoAlgorithms.calculateSmallList(inputA, inputP, k, m);
        ArrayList<Long> bigList = kriptoAlgorithms.calculateBigList(inputY, inputA, inputP, m);
        System.out.println("bigList:");
        for (Long aLong : bigList) {
            System.out.print(aLong.toString() + " ");
        }
        System.out.println("smallList:");
        for (Long aLong : smallList) {
            System.out.print(aLong.toString() + " ");
        }

        ArrayList<Integer> indexes = kriptoAlgorithms.getIJOfTheSame(smallList, bigList);
        Integer indexI = indexes.get(0), indexJ = indexes.get(1);
        System.out.print("i:" + indexI + " j:" + indexJ);
        Long x = indexI * m - indexJ;
        jtaTextAreaMessage.append(inputY.toString() + " = " + inputA + "^" + x + " mod " + inputP + ". (x = " + x + ")");
        // добавляем строку перехода
        jtaTextAreaMessage.append("\n");
    }
}