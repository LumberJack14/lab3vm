import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Scanner;

public class Main {

    private static final int initialN = 4;

    private JFrame frame;
    private String[] functions = {"1/x", " √x", "x³ + 2x² - 3x", "1 / ( 0.3 + exp(1/(x-2)) )"};
    private String[] methods = {
            "левых прямоугольников",
            "средних прямоугольников",
            "правых прямоугольников",
            "трапеций",
            "Симпсона"
    };

    private JComboBox<String> functionsComboBox;
    private JComboBox<String> methodsComboBox;
    private JLabel errorLabel;
    private ResultPanel resultsPanel;
    private PlotPanel plotPanel;
    private Result displayingResult = new Result(0, 0.01, initialN);

    private int selectedFunction = 0;
    private int selectedMethod = 0;

    private double a = 0.0;
    private double b = 0.0;
    private double e = 0.01;
    private String fileName = "test.txt";

    private class ItemListenerImplementation implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getSource() == functionsComboBox) {
                selectedFunction = functionsComboBox.getSelectedIndex();
                plotPanel.setFunction(selectedFunction);
            }

            if (e.getSource() == methodsComboBox) {
                selectedMethod = methodsComboBox.getSelectedIndex();
            }
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }


    public Main() {
        frame = new JFrame("LAB 3");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel initialPanel = createInitialPanel();

        frame.add(initialPanel);
        frame.setVisible(true);
    }


    private JPanel createInitialPanel() {
        JPanel initialPanel = new JPanel(new FlowLayout());
        ItemListener itemListener = new ItemListenerImplementation();

        /*
            choose function dropdown
         */
        JLabel chooseFuncLabel = new JLabel("Выберите функцию");
        functionsComboBox = new JComboBox<>(functions);
        functionsComboBox.addItemListener(itemListener);
        initialPanel.add(chooseFuncLabel);
        initialPanel.add(functionsComboBox);


        /*
            choose method dropdown
         */

        JLabel chooseMethodLabel = new JLabel("Выберите метод");
        methodsComboBox = new JComboBox<>(methods);
        methodsComboBox.addItemListener(itemListener);
        initialPanel.add(chooseMethodLabel);
        initialPanel.add(methodsComboBox);

        JPanel boxPanel = new JPanel(new BorderLayout());

        /*
            Plot Panel
         */

        plotPanel = new PlotPanel();
        boxPanel.add(plotPanel);
        initialPanel.add(boxPanel);
        plotPanel.setFunction(selectedFunction);

        /*
            User options panel
         */

        JPanel userOptionsPanel = createUserOptionsPanel();
        initialPanel.add(userOptionsPanel);

        return initialPanel;
    }

    private JPanel createUserOptionsPanel() {
        JPanel userOptionsPanel = new JPanel();
        //userOptionsPanel.setLayout(new BoxLayout(userOptionsPanel, BoxLayout.Y_AXIS));
        userOptionsPanel.setLayout(new GridLayout(0, 1, 0, 50));

        JPanel abePanel = new JPanel();
        abePanel.setLayout(new BoxLayout(abePanel, BoxLayout.Y_AXIS));
        JPanel filePanel = new JPanel();
        filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.Y_AXIS));

        errorLabel = new JLabel("Проверьте корректность данных");
        errorLabel.setForeground(new Color(161, 13, 42));

        JPanel aPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel aLabel = new JLabel("a:");
        JTextField aField = new JTextField(10);
        aField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateA();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateA();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateA();
            }

            private void updateA() {
                try {
                    a = Double.parseDouble(aField.getText().replace(",", "."));
                    errorLabel.setText("Введенные данные корректны");
                    errorLabel.setForeground(new Color(38, 150, 68));
                } catch (NumberFormatException ex) {
                    errorLabel.setText("Проверьте корректность данных");
                    errorLabel.setForeground(new Color(161, 13, 42));
                }
            }
        });
        aPanel.add(aLabel);
        aPanel.add(aField);

        JPanel bPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel bLabel = new JLabel("b:");
        JTextField bField = new JTextField(10);
        bField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateB();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateB();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateB();
            }

            private void updateB() {
                try {
                    b = Double.parseDouble(bField.getText().replace(",", "."));
                    errorLabel.setText("Введенные данные корректны");
                    errorLabel.setForeground(new Color(38, 150, 68));
                } catch (NumberFormatException ex) {
                    errorLabel.setText("Проверьте корректность данных");
                    errorLabel.setForeground(new Color(161, 13, 42));
                }
            }
        });
        bPanel.add(bLabel);
        bPanel.add(bField);

        JPanel ePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel eLabel = new JLabel("e:");
        JTextField eField = new JTextField(10);
        eField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateE();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateE();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateE();
            }

            private void updateE() {
                try {
                    e = Double.parseDouble(eField.getText().replace(",", "."));
                    errorLabel.setText("Введенные данные корректны");
                    errorLabel.setForeground(new Color(38, 150, 68));
                } catch (NumberFormatException ex) {
                    errorLabel.setText("Проверьте корректность данных");
                    errorLabel.setForeground(new Color(161, 13, 42));
                }
            }
        });
        ePanel.add(eLabel);
        ePanel.add(eField);

        abePanel.add(aPanel);
        abePanel.add(bPanel);
        abePanel.add(ePanel);

        JPanel fileNamePanel = new JPanel();
        JLabel fileNameLabel = new JLabel("Название файла:");
        JTextField fileNameField = new JTextField(10);
        fileNameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            private void update() {
                try {
                    fileName = fileNameField.getText();
                } catch (NumberFormatException ex) {
                    errorLabel.setText("Проверьте корректность названия");
                    errorLabel.setForeground(new Color(161, 13, 42));
                }
            }
        });
        fileNamePanel.add(fileNameLabel);
        fileNamePanel.add(fileNameField);

        JButton fileButton = new JButton("Из файла ↑");
        fileButton.addActionListener(event -> {
            try {
                FileInputStream fileInputStream = new FileInputStream(fileName);
                Scanner scanner = new Scanner(fileInputStream);

                double a = scanner.nextDouble();
                double b = scanner.nextDouble();
                double e = scanner.nextDouble();

                aField.setText(Double.toString(a));
                bField.setText(Double.toString(b));
                eField.setText(Double.toString(e));

                scanner.close();
                fileInputStream.close();
            } catch (FileNotFoundException ex) {
                errorLabel.setText("Файл не найден");
                errorLabel.setForeground(new Color(161, 13, 42));
            } catch (Exception ex) {
                errorLabel.setText("Ошибка при чтении файла");
                errorLabel.setForeground(new Color(161, 13, 42));
            }
        });

        filePanel.add(fileNamePanel);
        filePanel.add(fileButton);
        filePanel.add(errorLabel);

        userOptionsPanel.add(abePanel);
        userOptionsPanel.add(filePanel);

        /*
            calculate button
         */

        JButton calculateButton = new JButton("Посчитать");
        calculateButton.setFont(new Font("", Font.BOLD, 18));
        calculateButton.addActionListener(event -> {
            int n = initialN;
            try {
                Algorithm.setSelectedMethod(selectedMethod);
                Algorithm.setSelectedFunction(selectedFunction);
                Result res = Algorithm.Integrate(a, b, n);

                while (res.getE() > e) {
                    n++;
                    res = Algorithm.Integrate(a, b, n);

                    if (n == 10000) {
                        errorLabel.setText("Рекомендуем увеличить погрешность");
                        errorLabel.setForeground(new Color(133, 78, 1));
                        break;
                    }
                }

                displayingResult = res;

                resultsPanel.updateResult(displayingResult.getResult(), displayingResult.getE(), displayingResult.getN());
                System.out.println(res);
            } catch (InvalidIntervalException e) {
                errorLabel.setText("Некорректный интервал (a > b)");
                errorLabel.setForeground(new Color(161, 13, 42));
            } catch (BreakPointException e) {
                errorLabel.setText("Невозможно посчитать: разрыв второго рода");
                errorLabel.setForeground(new Color(161, 13, 42));
            } catch (FunctionNotDefinedException e) {
                errorLabel.setText("Функция не определена на части отрезка");
                errorLabel.setForeground(new Color(161, 13, 42));
            }
        });
        userOptionsPanel.add(calculateButton);

        /*
           Results panel
         */
        resultsPanel = new ResultPanel(displayingResult.getResult(), displayingResult.getE(), displayingResult.getN());

        userOptionsPanel.add(resultsPanel);

        return userOptionsPanel;
    }

}
