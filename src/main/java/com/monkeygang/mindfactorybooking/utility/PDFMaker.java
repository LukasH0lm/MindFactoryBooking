package com.monkeygang.mindfactorybooking.utility;

import java.awt.Graphics2D;
import java.io.*;

import com.itextpdf.text.DocumentException;
import com.monkeygang.mindfactorybooking.HelloApplication;
import org.jfree.chart.*;


import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;



import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class PDFMaker {
//Lukas er sej

    public static void HelloWordPDF() throws IOException, DocumentException {

        JFreeChart pieChart = createPieChart();

        String pdfFilePath = HelloApplication.class.getResource("hello-view.fxml").getPath() + "HelloWorld.pdf";
        OutputStream fos = new FileOutputStream(pdfFilePath);
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, fos);

        document.open();

        PdfContentByte pdfContentByte = writer.getDirectContent();
        int width = 400; //width of BarChart
        int height = 300; //height of BarChart
        PdfTemplate pdfTemplate = pdfContentByte.createTemplate(width, height);

        //create graphics
        Graphics2D graphics2d = pdfTemplate.createGraphics(width, height,
                new DefaultFontMapper());

        //create rectangle
        java.awt.geom.Rectangle2D rectangle2d = new java.awt.geom.Rectangle2D.Double(
                0, 0, width, height);

        pieChart.draw(graphics2d, rectangle2d);

        graphics2d.dispose();
        pdfContentByte.addTemplate(pdfTemplate, 180, 450); //0, 0 will draw BAR chart on bottom left of page

        document.add(new com.itextpdf.text.Paragraph("MindFactory Projekt", new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 18, com.itextpdf.text.Font.BOLD)));
        document.add(new com.itextpdf.text.Paragraph(""));
        document.add(new com.itextpdf.text.Paragraph("Lukas er sej"));
        document.add(new com.itextpdf.text.Paragraph("Han bager den dej"));
        document.add(new com.itextpdf.text.Paragraph("former den som kaj"));
        document.add(new com.itextpdf.text.Paragraph("lige i munden på dig"));



        document.close();
        System.out.println("PDF created in >> " + pdfFilePath);


    }


    public static JFreeChart createPieChart() throws IOException {

        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Lukas", new Double(85));
        dataset.setValue("Sune", new Double(14));
        dataset.setValue("Alexander", new Double(1));

        JFreeChart chart = ChartFactory.createPieChart("Amount of work put into project",   // chart title
                dataset,          // data
                true,             // include legend
                true, false);

        int width = 640;   /* Width of the image */
        int height = 480;  /* Height of the image */
        File pieChart = new File("PieChart.jpeg");
        ChartUtils.saveChartAsJPEG(pieChart, chart, width, height);

        return chart;

    }

}