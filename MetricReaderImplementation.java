package oracle.interview.implementation;

import oracle.interview.metrics.MetricReader;
import oracle.interview.metrics.TargetMetricsContainer;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MetricReaderImplementation implements MetricReader {
    
	@Override
    public List<TargetMetricsContainer> readMetrics(InputStream metricInputStream) {
        // TODO: implement this, reading data from the input stream, returning a list of containers read from the stream
        //JCMS V1
        
        List<TargetMetricsContainer> tmContainerList = new ArrayList<>();
        TargetMetricsContainer tmContainer = null;
        Locale.setDefault(Locale.US); //my local has es_MX
        SimpleDateFormat sdFormat = new SimpleDateFormat("d-MMM-yyyy");
        Date date;
        Instant instant;
        
        try {
		
        	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        	Document document = dBuilder.parse(metricInputStream);
        	document.getDocumentElement().normalize();
        	
        	NodeList nlTarget = document.getElementsByTagName("target");
        	//Get the list of targets, TargetMetricsContainer must has List<TargetObjects> -> String name, String type 
        	
        	for(int j=0;j<nlTarget.getLength();j++) {
        		Node nodeTarget = nlTarget.item(j);
        		if(nodeTarget.getNodeType()==Node.ELEMENT_NODE) {
        			Element elTarget = (Element) nodeTarget;
        			
        			tmContainer = new TargetMetricsContainer(elTarget.getAttribute("name"),elTarget.getAttribute("type"));
        			
        		}
        	}
        	
        	NodeList nlMetric = document.getElementsByTagName("metric");
        	//Get the full list of metrics
        	
        	for(int i=0;i<nlMetric.getLength();i++) { 
        		Node nodeMetric = nlMetric.item(i);
        		if(nodeMetric.getNodeType()==Node.ELEMENT_NODE) {
        			Element elMetric = (Element) nodeMetric;        		
        			String timestamp = elMetric.getAttribute("timestamp");
        			date = sdFormat.parse(timestamp);
        			instant = date.toInstant();
        			
        			tmContainer.addMetric(elMetric.getAttribute("type"), instant, Integer.parseInt(elMetric.getAttribute("value")));
        		}
        	}
        	
        	tmContainerList.add(tmContainer);
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
        
    	return tmContainerList;
    }

}
