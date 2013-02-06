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
    public static void getChart(double[] errors){
        makeChart(errors);
    }
    public static void makeChart(double[] errors){
    XYSeries series = new XYSeries("Error vs Iteraciones");
    for (int i= 0 ; i<errors.length; i++){
        series.add(i,errors[i]);
    }
    //Add the series to your data set
    XYSeriesCollection dataset = new XYSeriesCollection();
    dataset.addSeries(series);
    //Generate the graph
    JFreeChart chart = ChartFactory.createXYAreaChart("Error vs Iteraciones", // Title
    "Iteraciones", // x-axis Label
    "Error", // y-axis Label
    dataset, // Dataset
    PlotOrientation.VERTICAL, // Plot Orientation
    true, // Show Legend
    true, // Use tooltips
    false // Configure chart to generate URLs?
    );
    try {
    ChartUtilities.saveChartAsJPEG(new File("XYchart.jpg"), chart, 600, 400);
    } catch (IOException e) {
    System.err.println("Error creando grafico.");
    }
    }
    
}
