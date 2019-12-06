import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RefineryUtilities;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class Main {
    static String companyDataString;
    static List<Company> companyList;
    static String APIKey = "I6Z270UURCHPFXJK";
    static String timeUnit = "1";
    static String seriesUnit = "TIME_SERIES_INTRADAY";
    static boolean isIntraDay = true;
    static ChartPanel chartPanel;
    static JFrame frame;
    static Double trueMax, trueMin;
    static String targetSymbol;
    static JScrollPane scroll, graphScroll;
    static JPanel textPanel;
    static ChartPanel graphPanel, volumeGraphPanel;
    static Container chartContainer;
    static Dimension currentSize;


    public static void main(String[] args) {
        companyList = getCompanyInfo();

        companyList.remove(0);

        generateGUI();

    }

    static void generateGUI(){
        //Creating the Frame
        frame = new JFrame("FRANK Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(700,700);


        //Creating the MenuBar and adding components
        JMenuBar mb = new JMenuBar();
        JMenu m1 = new JMenu("Market");
        JMenu m2 = new JMenu("View");
        JMenu m3 = new JMenu("Data Type");
        mb.add(m1);
        mb.add(m2);
        mb.add(m3);
        JRadioButtonMenuItem m11 = new JRadioButtonMenuItem("Stock Market");
        JRadioButtonMenuItem m22 = new JRadioButtonMenuItem("Forex");
        JRadioButtonMenuItem viewSubMenuItem1 = new JRadioButtonMenuItem("JSON Text");
        JRadioButtonMenuItem viewSubMenuItem2 = new JRadioButtonMenuItem("Graph Data");
        JMenu viewSubMenuItem3 = new JMenu("Time Unit Interval");
        JMenu viewSubMenutItem4 = new JMenu("Stock Time Series Type");
        JRadioButtonMenuItem seriesSubMenuItem1 = new JRadioButtonMenuItem("InterDay");
        JRadioButtonMenuItem seriesSubMenuItem2 = new JRadioButtonMenuItem("Daily");
        JRadioButtonMenuItem seriesSubMenuItem3 = new JRadioButtonMenuItem("Weekly");
        JRadioButtonMenuItem seriesSubMenuItem4 = new JRadioButtonMenuItem("Monthly");
        JRadioButtonMenuItem seriesSubMenuItem5 = new JRadioButtonMenuItem("Quote Endpoint");
        m1.add(m11);
        m1.add(m22);
        m2.add(viewSubMenuItem1);
        m2.add(viewSubMenuItem2);
        m2.add(viewSubMenuItem3);
        m3.add(viewSubMenutItem4);
        viewSubMenutItem4.add(seriesSubMenuItem1);
        viewSubMenutItem4.add(seriesSubMenuItem2);
        viewSubMenutItem4.add(seriesSubMenuItem3);
        viewSubMenutItem4.add(seriesSubMenuItem4);
        viewSubMenutItem4.add(seriesSubMenuItem5);
        JRadioButtonMenuItem timeSubMenuItem1 = new JRadioButtonMenuItem("1 min");
        JRadioButtonMenuItem timeSubMenuItem2 = new JRadioButtonMenuItem("5 min");
        JRadioButtonMenuItem timeSubMenuItem3 = new JRadioButtonMenuItem("15 min");
        JRadioButtonMenuItem timeSubMenuItem4 = new JRadioButtonMenuItem("30 min");
        JRadioButtonMenuItem timeSubMenuItem5 = new JRadioButtonMenuItem("60 min");
        viewSubMenuItem3.add(timeSubMenuItem1);
        viewSubMenuItem3.add(timeSubMenuItem2);
        viewSubMenuItem3.add(timeSubMenuItem3);
        viewSubMenuItem3.add(timeSubMenuItem4);
        viewSubMenuItem3.add(timeSubMenuItem5);
        m11.setSelected(true);
        viewSubMenuItem2.setSelected(true);
        seriesSubMenuItem1.setSelected(true);
        timeSubMenuItem1.setSelected(true);

        m11.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m22.setSelected(false);
            }
        });
        m22.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m11.setSelected(false);
            }
        });
        viewSubMenuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewSubMenuItem2.setSelected(false);
            }
        });
        viewSubMenuItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewSubMenuItem1.setSelected(false);
            }
        });

        seriesSubMenuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seriesUnit = "TIME_SERIES_INTRADAY";
                isIntraDay = true;
                viewSubMenuItem3.setVisible(true);
                seriesSubMenuItem2.setSelected(false);
                seriesSubMenuItem3.setSelected(false);
                seriesSubMenuItem4.setSelected(false);
                seriesSubMenuItem5.setSelected(false);
            }
        });
        seriesSubMenuItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seriesUnit = "TIME_SERIES_DAILY";
                isIntraDay = false;
                viewSubMenuItem3.setVisible(false);
                seriesSubMenuItem1.setSelected(false);
                seriesSubMenuItem3.setSelected(false);
                seriesSubMenuItem4.setSelected(false);
                seriesSubMenuItem5.setSelected(false);
            }
        });
        seriesSubMenuItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seriesUnit = "TIME_SERIES_WEEKLY";
                isIntraDay = false;
                viewSubMenuItem3.setVisible(false);
                seriesSubMenuItem2.setSelected(false);
                seriesSubMenuItem1.setSelected(false);
                seriesSubMenuItem4.setSelected(false);
                seriesSubMenuItem5.setSelected(false);
            }
        });
        seriesSubMenuItem4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seriesUnit = "TIME_SERIES_MONTHLY";
                isIntraDay = false;
                viewSubMenuItem3.setVisible(false);
                seriesSubMenuItem2.setSelected(false);
                seriesSubMenuItem3.setSelected(false);
                seriesSubMenuItem1.setSelected(false);
                seriesSubMenuItem5.setSelected(false);
            }
        });
        seriesSubMenuItem5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seriesUnit = "GLOBAL_QUOTE";
                isIntraDay = false;
                viewSubMenuItem3.setVisible(false);
                seriesSubMenuItem2.setSelected(false);
                seriesSubMenuItem3.setSelected(false);
                seriesSubMenuItem4.setSelected(false);
                seriesSubMenuItem1.setSelected(false);
            }
        });

        timeSubMenuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeUnit = "1";
                timeSubMenuItem2.setSelected(false);
                timeSubMenuItem3.setSelected(false);
                timeSubMenuItem4.setSelected(false);
                timeSubMenuItem5.setSelected(false);
            }
        });
        timeSubMenuItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeUnit = "5";
                timeSubMenuItem1.setSelected(false);
                timeSubMenuItem3.setSelected(false);
                timeSubMenuItem4.setSelected(false);
                timeSubMenuItem5.setSelected(false);
            }
        });
        timeSubMenuItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeUnit = "15";
                timeSubMenuItem2.setSelected(false);
                timeSubMenuItem1.setSelected(false);
                timeSubMenuItem4.setSelected(false);
                timeSubMenuItem5.setSelected(false);
            }
        });
        timeSubMenuItem4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeUnit = "30";
                timeSubMenuItem2.setSelected(false);
                timeSubMenuItem3.setSelected(false);
                timeSubMenuItem1.setSelected(false);
                timeSubMenuItem5.setSelected(false);
            }
        });
        timeSubMenuItem5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeUnit = "60";
                timeSubMenuItem2.setSelected(false);
                timeSubMenuItem3.setSelected(false);
                timeSubMenuItem4.setSelected(false);
                timeSubMenuItem1.setSelected(false);
            }
        });


        //Creating the panel at bottom and adding components
        textPanel = new JPanel(); // the panel is not visible in output
        JLabel label = new JLabel("Enter Symbol");
        JTextField tf = new JTextField(10); // accepts upto 10 characters
        JButton symbolButton = new JButton("Get Info");
        JButton resetButton = new JButton("Reset");
        textPanel.add(label); // Components Added using Flow Layout
        textPanel.add(label); // Components Added using Flow Layout
        textPanel.add(tf);
        textPanel.add(symbolButton);
        textPanel.add(resetButton);


        // Text Area at the Center
        JTextArea ta = new JTextArea();
        scroll = new JScrollPane(ta);

        ta.setEditable(false);

        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        //Add the built company data String to text Area
        ta.setText(companyDataString);

        //Adding Components to the frame.


        frame.getContentPane().add(BorderLayout.NORTH, mb);
        frame.getContentPane().add(BorderLayout.CENTER, scroll);
        frame.getContentPane().add(BorderLayout.SOUTH, textPanel);
        frame.setVisible(true);

        initCompanyMenu(ta, companyList);

        symbolButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(viewSubMenuItem2.isSelected()){

                    currentSize = frame.getSize();

                    frame.remove(scroll);
                    if(graphScroll!=null){
                        chartContainer.removeAll();
                        graphScroll.remove(chartContainer);
                        frame.remove(graphScroll);
                        frame.revalidate();
                    }


                    targetSymbol = tf.getText();
                    jsonToDataset(fetchStockInfo(tf.getText()));
                }else{

                    currentSize = frame.getSize();

                    frame.add(BorderLayout.CENTER, scroll);
                    if(graphPanel!=null){
                        frame.remove(graphScroll);
                        frame.revalidate();
                        frame.setVisible(true);
                    }
                    //ta.setVisible(true);
                    ta.setText(fetchStockInfo(tf.getText()));
                    frame.setSize(currentSize);
                    ta.setCaretPosition(0);
                }

            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                currentSize = frame.getSize();

                if(graphPanel!=null){
                    frame.getContentPane().remove(graphScroll);
                    frame.revalidate();
                    frame.repaint();
                    frame.setVisible(true);
                }
                frame.add(BorderLayout.CENTER, scroll);
                initCompanyMenu(ta, companyList);
                ta.setCaretPosition(0);
            }
        });
    }


    static String getStockJSON(String link){
        String responseStr = "";
        try {
            URL url = new URL(link);
            HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
            urlconnection.setRequestMethod("GET");
            urlconnection.setInstanceFollowRedirects(true);
            urlconnection.connect();
            InputStream inputStream = urlconnection.getInputStream();
            if (inputStream == null) {
                return "Error in inputstream";
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            //System.out.println("createList here");
            StringBuilder builder = new StringBuilder();
            String temp;

            while ((temp = reader.readLine())!=null) {
                builder.append(temp);
                //builder.append(System.getProperty("line.separator"));
                //System.out.println(reader.readLine());
            }
            responseStr = builder.toString();

        } catch (IOException e) {
            System.out.println("IO EXCEPTION IN PAGE SHIT");
        }

        //System.out.println("stockJSON String is: " + responseStr);

        return responseStr;
    }

    static List<Company> getCompanyInfo(){
        List<Company> companyList = new ArrayList<>();
        List<String> dataList = new ArrayList<>();

        //get raw data from file
        try{
            FileInputStream stream = new FileInputStream("/Users/Jake/Downloads/companylist.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            String line;

            while ((line = reader.readLine()) != null)   {
                dataList.add(line);
            }

            stream.close();

        }catch (IOException io){
            System.out.println("IO EXCEPTION");
        }

        //compile raw data into organized list of company objects
        for(String data : dataList){
            List<String> sorted = Arrays.asList(data.split("\\s*,\\s*"));
            Company company = new Company();
            company.setName(sorted.get(0));
            company.setSymbol(sorted.get(1));
            company.setIndustry(sorted.get(2));
            companyList.add(company);
        }

        return companyList;
    }

    static void initCompanyMenu(JTextArea textArea, List<Company> companyList){

        StringBuilder builder = new StringBuilder();
        for(Company company: companyList){
            builder.append("Company Name: " + company.getName() + " Symbol: " + company.getSymbol() + " Industry: " + company.getIndustry() + System.getProperty("line.separator"));
        }

        companyDataString = builder.toString();
        textArea.setText(companyDataString);
        textArea.setCaretPosition(0);
    }

    static String fetchStockInfo(String symbol){

        StringBuilder builder = new StringBuilder();
        builder.append("https://www.alphavantage.co/query?function=");
        builder.append(seriesUnit);
        builder.append("&symbol=");
        builder.append(symbol);
        if(isIntraDay){
            builder.append("&interval=");
            builder.append(timeUnit);
            builder.append("min&apikey=");
            builder.append(APIKey);
        }else{
            builder.append("&apikey=");
            builder.append(APIKey);
        }

        String searchURL = builder.toString();
        System.out.println(searchURL);

        return getStockJSON(searchURL);
    }

    static void createGraph(String graphTitle, JFrame frame, List<IntraDayData> dataList){

        JFreeChart xylineChart = ChartFactory.createTimeSeriesChart(graphTitle, "Time", "Cost", createTimeSeriesDataSet(dataList).get(0));

        graphPanel = new ChartPanel( xylineChart );
        graphPanel.setPreferredSize( new java.awt.Dimension( 500 , 500 ) );
        final XYPlot plot = xylineChart.getXYPlot( );
        plot.setDomainPannable(false);
        DateAxis dateAxis = (DateAxis) plot.getDomainAxis();
        dateAxis.setDateFormatOverride(new SimpleDateFormat("hh:mm:ss"));
        graphPanel.setDisplayToolTips(true);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
        renderer.setSeriesPaint( 0 , Color.RED );
        renderer.setSeriesPaint( 1 , Color.GREEN );
        renderer.setSeriesPaint( 2 , Color.YELLOW );
        renderer.setSeriesPaint( 3 , Color.BLUE );
        renderer.setSeriesStroke( 0 , new BasicStroke( 1.0f ) );
        renderer.setSeriesStroke( 1 , new BasicStroke( 1.0f ) );
        renderer.setSeriesStroke( 2 , new BasicStroke( 1.0f ) );
        renderer.setSeriesStroke( 3 , new BasicStroke( 1.0f ) );
        renderer.setBaseToolTipGenerator(new XYToolTipGenerator() {
            @Override
            public String generateToolTip(XYDataset xyDataset, int i, int i1) {
                Double targetCost = xyDataset.getYValue(i, i1);

                return Double.toString(targetCost);
            }
        });
        plot.setRenderer( renderer );

        JFreeChart volumeChart = ChartFactory.createTimeSeriesChart(graphTitle, "Time", "Volume", createTimeSeriesDataSet(dataList).get(1));
        volumeGraphPanel = new ChartPanel( volumeChart );
        volumeGraphPanel.setPreferredSize( new java.awt.Dimension( 500 , 350 ) );
        final XYPlot volumePlot = volumeChart.getXYPlot( );
        volumePlot.setDomainPannable(false);
        DateAxis volumeDateAxis = (DateAxis) plot.getDomainAxis();
        volumeDateAxis.setDateFormatOverride(new SimpleDateFormat("hh:mm:ss"));

        XYLineAndShapeRenderer volumeRenderer = new XYLineAndShapeRenderer( );
        volumeRenderer.setSeriesPaint( 0 , Color.YELLOW );

        volumeRenderer.setSeriesStroke( 0 , new BasicStroke( 1.0f ) );
        volumeRenderer.setBaseToolTipGenerator(new XYToolTipGenerator() {
            @Override
            public String generateToolTip(XYDataset xyDataset, int i, int i1) {
                String dataInfo = Double.toString(xyDataset.getYValue(i, i1));

                return dataInfo;
            }
        });
        volumePlot.setRenderer( volumeRenderer );

        chartContainer = new Container();
        chartContainer.add(graphPanel);
        chartContainer.add(volumeGraphPanel);
        chartContainer.setLayout(new GridLayout(2, 1));
        graphScroll = new JScrollPane(chartContainer);

        graphScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        graphScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        frame.add(BorderLayout.CENTER, graphScroll);
        //RefineryUtilities.centerFrameOnScreen( frame );
        frame.pack();
        frame.revalidate();
        frame.setSize(currentSize);
        frame.setVisible(true);

    }


    static void jsonToDataset(String jsonString){
        JSONObject jobject = new JSONObject(jsonString);
        String targetKey = "";

        //Find the target json key which will yeild keys of times with values of json objects
        switch(seriesUnit){
            case "TIME_SERIES_INTRADAY":
                switch(timeUnit){
                    case "1":
                        targetKey = "Time Series (1min)";
                        break;
                    case "5":
                        targetKey = "Time Series (5min)";
                        break;
                    case "15":
                        targetKey = "Time Series (15min)";
                        break;
                    case "30":
                        targetKey = "Time Series (30min)";
                        break;
                    case "60":
                        targetKey = "Time Series (60min)";
                        break;
                }
                break;
            case "TIME_SERIES_DAILY":
                targetKey = "Time Series (Daily)";
                break;
            case "TIME_SERIES_WEEKLY":
                targetKey = "Time Series (Weekly)";
                break;
            case "TIME_SERIES_MONTHLY":
                targetKey = "Time Series (Monthly)";
                break;
            case "GLOBAL_QUOTE":
                targetKey = "Global Quote";
                break;
        }

        JSONObject targetJson = jobject.getJSONObject(targetKey);

        Iterator keys = targetJson.keys();

        List<String> keyList = new ArrayList<>();


        List<IntraDayData> intraDayDataList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        while(keys.hasNext()){
            Object keyToAdd = keys.next();
            keyList.add(keyToAdd.toString());
        }

        List<LocalDateTime> sortedKeyList = keyList.stream().map(ds -> LocalDateTime.parse(ds, formatter)).collect(Collectors.toCollection(ArrayList::new));

        sortedKeyList.sort(Comparator.naturalOrder());


        for(int i = 0; i < sortedKeyList.size(); i++){
            IntraDayData idata = new IntraDayData();
            JSONObject json = targetJson.getJSONObject(formatter.format(sortedKeyList.get(i)));
            idata.setDate(sortedKeyList.get(i));
            idata.setOpen(Double.parseDouble(json.getString("1. open")));
            idata.setHigh(Double.parseDouble(json.getString("2. high")));
            idata.setLow(Double.parseDouble(json.getString("3. low")));
            idata.setClose(Double.parseDouble(json.getString("4. close")));
            idata.setVolume(Double.parseDouble(json.getString("5. volume")));

            intraDayDataList.add(idata);
        }

        createGraph(targetSymbol + " Intra-Day Trading", frame, intraDayDataList);

    }

    static List<TimeSeriesCollection> createTimeSeriesDataSet(List<IntraDayData>  dataList) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Date date;
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString;
        RegularTimePeriod rtp;
        TimeSeriesDataItem dataItem;
        TimeSeriesCollection dataSet = new TimeSeriesCollection();
        TimeSeriesCollection volumeSet = new TimeSeriesCollection();
        List<TimeSeriesCollection> dataSetList = new ArrayList<>();
        TimeSeries series, volumeSeries;

        for(int j = 0; j < 5; j++){
            series = new TimeSeries( intraDayDataString(j) );
            volumeSeries = new TimeSeries( intraDayDataString(j) );

            for(int i = 0; i < dataList.size(); i++){
                dateString = formatter.format(dataList.get(i).getDate());
                try {
                    date = sdf2.parse(dateString);
                    rtp = new org.jfree.data.time.Minute(date);
                    switch(j){
                        case 0:
                            dataItem = new TimeSeriesDataItem(rtp , dataList.get(i).getHigh());
                            series.add(dataItem);
                            break;
                        case 1:
                            dataItem = new TimeSeriesDataItem(rtp, dataList.get(i).getOpen());
                            series.add(dataItem);
                            break;
                        case 2:
                            dataItem = new TimeSeriesDataItem(rtp, dataList.get(i).getClose());
                            series.add(dataItem);
                            break;
                        case 3:
                            dataItem = new TimeSeriesDataItem(rtp, dataList.get(i).getLow());
                            series.add(dataItem);
                            break;
                        case 4:
                            dataItem = new TimeSeriesDataItem(rtp, dataList.get(i).getVolume());
                            volumeSeries.add(dataItem);
                            break;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if(j<4){
                dataSet.addSeries(series);
            }else{
                volumeSet.addSeries(volumeSeries);
            }
        }

        dataSetList.add(dataSet);
        dataSetList.add(volumeSet);

        return dataSetList;
    }


    static String intraDayDataString(Integer marker){
        String value = "";
        switch (marker){
            case 0:
                value = "High";
                break;
            case 1:
                value = "Open";
                break;
            case 2:
                value = "Close";
                break;
            case 3:
                value = "Low";
                break;
            case 4:
                value = "Volume";
                break;
        }
        return value;
    }

}


