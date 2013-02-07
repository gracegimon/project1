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
/**
 * @Author Gimbet
 */
public class XYChart {
    public static void getChart(double[][] errors){
        makeChart(errors);
    }

    public static void makeChart(double[][] errors)
    {
        XYSeries[] series = new XYSeries[errors.length];

        for (int i = 0; i < errors.length; i++)
        {
            XYSeries serie = new XYSeries("" + i);
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


        try {
            ChartUtilities.saveChartAsJPEG(new File("XYchart.jpg"), chart, 1920, 1080);
        } catch (IOException e) {
            System.err.println("Error creando grafico.");
        }
    }
    
}
