package project.gui.components.student;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.*;
import org.jfree.date.DayAndMonthRule;
import project.database.GradeDAO;
import project.database.objects.Grade;
import project.gui.components.rounded.RoundedPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;

public class GradesChartPanel extends ChartPanel {

    private ArrayList<Grade> studentGrades;
    public GradesChartPanel(String studentId) {
        super(null);
        studentGrades = (ArrayList<Grade>) new GradeDAO().getAllStudentGrades(studentId);

        JFreeChart chart = createChart(createDataset());

        chart.setBorderPaint(new Color(242,242,242));
        setChart(chart);
        setPreferredSize(new Dimension(400, 250));
    }

    private XYDataset createDataset() {

        var dataset = new TimeSeriesCollection();
        dataset.addSeries(createPersonalGrades());

        return dataset;
    }

    private TimeSeries createPersonalGrades() {
        var series = new TimeSeries("Your grades");

        //int i = 0;
        for(Grade g : studentGrades) {
            try {
                series.add(new Day(g.getData()), g.getVoto());
            } catch (SeriesException e) {
                series.addOrUpdate(new Day(new Date(g.getData().getTime())), new GradeDAO().getAVGInDate(g.getStudenteId(), g.getData()));
            }
            //i++;
        }

        return series;
    }

    private JFreeChart createChart(XYDataset dataset) {

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "",
                "",
                "",
                dataset,
                true,
                false,
                false
        );

        XYPlot plot = chart.getXYPlot();

        var renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, new Color(155,81,224));
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(new Color(242,242,242));

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(false);
        plot.setDomainGridlinePaint(Color.BLACK);


        chart.getLegend().setFrame(BlockBorder.NONE);

        chart.setTitle(new TextTitle("",
                        new Font(Font.SANS_SERIF,Font.BOLD, 12)
                )
        );

        return chart;
    }
}
