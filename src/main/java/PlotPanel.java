import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.TextAnchor;

import javax.swing.*;
import java.awt.*;

class PlotPanel extends JPanel {
    private int selectedFunction = 0;
    private ChartPanel chartPanel;

    public PlotPanel() {
        setLayout(new BorderLayout());
    }

    public void setFunction(int function) {
        this.selectedFunction = function;
        updateChart();
    }

    private void updateChart() {
        if (selectedFunction == 1 || selectedFunction == 2) {
            XYSeries series = generateContinuousFunctionSeries();
            drawSeries(new XYSeries[]{series});
        } else {
            XYSeries[] series = generateFunctionWithBreakSeries();
            drawSeries(new XYSeries[]{series[0], series[1]});
        }
    }

    private XYSeries generateContinuousFunctionSeries() {
        XYSeries series = new XYSeries("Function");

        for (int x = -100; x <= 100; x++) { // Adjust number of points
            double xx = x / 10.0; // Scale x values for better plotting
            double y;
            switch (selectedFunction) {
                case 1:
                    y = Math.sqrt(xx);
                    break;
                case 2:
                    y = Math.pow(xx, 3) + 2 * Math.pow(xx, 2) - 3 * xx;
                    break;
                default:
                    y = 0;
            }
            // Only add points within the specified y-range
            if (y >= -8 && y <= 8) {
                series.add(xx, y);
            }
        }

        return series;
    }

    private XYSeries[] generateFunctionWithBreakSeries() {
        XYSeries series1 = new XYSeries("FunctionL");
        XYSeries series2 = new XYSeries("FunctionR");

        for (int x = -1000; x <= 1000; x++) { // Adjust number of points
            double xx = x / 100.0; // Scale x values for better plotting
            double y;
            switch (selectedFunction) {
                case 0:
                    if (xx == 0) continue;
                    y = 1 / xx;
                    break;
                case 3:
                    if ((xx - 2) == 0) continue;
                    double possibleError = 0.3 + Math.exp(1 / (xx - 2));
                    if (possibleError == 0) {
                        continue;
                    }
                    y = 1 / possibleError;
                    break;
                default:
                    y = 0;
            }
            // Only add points within the specified y-range
            if (!(y >= -8 && y <= 8)) {
                continue;
            }

            if (selectedFunction == 0) {
                if (xx <= 0) {
                    series1.add(xx, y);
                } else {
                    series2.add(xx, y);
                }
            }

            if (selectedFunction == 3) {
                if (xx <= 2) {
                    series1.add(xx, y);
                } else {
                    series2.add(xx, y);
                }
            }
        }

        return new XYSeries[]{series1, series2};
    }

    private void drawSeries(XYSeries[] series) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        for (XYSeries s : series) {
            dataset.addSeries(s);
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                "График функции",
                "x",
                "y",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false
        );

        // Customize plot
        XYPlot plot = chart.getXYPlot();

        plot.getRenderer().setSeriesPaint(0, Color.RED);
        plot.getRenderer().setSeriesPaint(1, Color.RED);

        // Set a specific range for the x-axis and y-axis
        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setRange(-10, 10); // x range

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(-5, 5); // y range

        // OX axis (y=0) marker
        ValueMarker oxMarker = new ValueMarker(0);
        oxMarker.setPaint(Color.BLACK);
        oxMarker.setStroke(new BasicStroke(2));
        oxMarker.setLabel("OX");
        oxMarker.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
        oxMarker.setLabelTextAnchor(TextAnchor.BOTTOM_RIGHT);
        plot.addRangeMarker(oxMarker);

        // OY axis (x=0) marker
        ValueMarker oyMarker = new ValueMarker(0);
        oyMarker.setPaint(Color.BLACK);
        oyMarker.setStroke(new BasicStroke(2));
        oyMarker.setLabel("OY");
        oyMarker.setLabelAnchor(RectangleAnchor.BOTTOM_LEFT);
        oyMarker.setLabelTextAnchor(TextAnchor.TOP_LEFT);
        plot.addDomainMarker(oyMarker);

        // Remove the old chart panel if it exists
        if (chartPanel != null) {
            remove(chartPanel);
        }

        chartPanel = new ChartPanel(chart);
        add(chartPanel, BorderLayout.CENTER);

        // Refresh the panel
        revalidate();
        repaint();
    }

    public int getSelectedFunction() {
        return selectedFunction;
    }
}