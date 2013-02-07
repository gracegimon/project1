/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package redneural;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import java.io.File;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.*;
import org.jfree.chart.plot.*;
import java.io.*;
import java.awt.*;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
/**
 * @Author Gimbet
 */
public class XYChart {
    public static void getChart(double[][] errors, String[] rateNames, int runType){
        makeChart(errors, rateNames, runType);
    }

    public static void makeChart(double[][] errors , String[] rateNames, int runType)
    {
        XYSeries[] series = new XYSeries[errors.length];

        for (int i = 0; i < errors.length; i++)
        {
            XYSeries serie = new XYSeries(rateNames[i]);
            for (int j = 0 ; j<errors[i].length; j++){
                serie.add(j, errors[i][j]);
            }
            series[i] = serie;
        }

        //Add the serie to your data set
        XYSeriesCollection dataset = new XYSeriesCollection();


        for (int i = 0; i < series.length; i++)
            dataset.addSeries(series[i]);


        //Generate the graph
        JFreeChart chart = ChartFactory.createXYLineChart("Error vs Iteraciones", // Title
            "Iteraciones", // x-axis Label
            "Error", // y-axis Label
            dataset, // Dataset
            PlotOrientation.VERTICAL, // Plot Orientation
            true, // Show Legend
            true, // Use tooltips
            false // Configure chart to generate URLs?
        );

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.white);
    //    plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
        //plot.setDomainGridlinePaint(Color.white);
        //plot.setRangeGridlinePaint(Color.white);
        //plot.getRenderer().setSeriesStroke(1, new BasicStroke(2.0f));

     //LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
//        renderer.setDrawShapes(true);

        if (runType < 4)
        {
            for (int i = 0; i < rateNames.length; i++)
                plot.getRenderer().setSeriesStroke(
                    i, new BasicStroke(
                        5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[] {(i+1)*4,.0f, 6.0f}, 0.0f
                    )
                );
        }
        else
        {
             for (int i = 0; i < rateNames.length; i++)
                plot.getRenderer().setSeriesStroke(
                    i, new BasicStroke(
                        1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[] {(i+1)*4,.0f, 6.0f}, 0.0f
                    )
                );           
        }

        /*
        plot.getRenderer().setSeriesStroke(
            1, new BasicStroke(
                2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1.0f, new float[] {6.0f, 6.0f}, 0.0f
            )
        );
        plot.getRenderer().setSeriesStroke(
            2, new BasicStroke(
                2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1.0f, new float[] {2.0f, 6.0f}, 0.0f
            )
        );*/
        try {
            ChartUtilities.saveChartAsPNG(new File("XYchart.png"), chart, 1920, 1080);
        } catch (IOException e) {
            System.err.println("Error creando grafico.");
        }
    }
    
}
