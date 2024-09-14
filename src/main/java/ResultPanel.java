import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class ResultPanel extends JPanel {
    private double result;
    private double e;
    private int n;

    public ResultPanel(double res, double e, int n) {
        if (res == -0.0) {
            res = 0;
        }

        this.result = res;
        this.e = e;
        this.n = n;
        setLayout(new GridLayout(0, 1, 0, 1));
        updateDisplay();
    }

    public void updateResult(double result, double e, int n) {
        this.result = result;
        this.e = e;
        this.n = n;
        updateDisplay();
        revalidate();
        repaint();
    }

    private void updateDisplay() {
        removeAll();
        add(new JLabel("Результат: " + new DecimalFormat("##.##").format(result)));
        add(new JLabel("Число разбиений: " + n));
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public double getE() {
        return e;
    }

    public void setE(double e) {
        this.e = e;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }
}
