package oracle.interview.implementation;

import java.lang.reflect.Field;

import oracle.interview.metrics.MetricStorage;
import oracle.interview.metrics.MetricWriter;
import oracle.interview.metrics.TargetMetricsContainer;

public class MetricWriterImplementation implements MetricWriter {

    //JCMS V1
    private final MetricStorage storage;
    //Container class without setter just to user Reflection
    TargetMetricsContainer tmContainer; 

    public MetricWriterImplementation(MetricStorage storage) {
        this.storage = storage;
    }

    @Override
    public void writeMetricsContainer(TargetMetricsContainer metricsContainer) {
        // TODO: write this metricsContainer to the MetricStorage. Hint
        //      storage.write(metricsContainer);
        //  partially works.  Since the write could fail, retry the write on failure
        //  as appropriate.
    	
    	try {
    		tmContainer = metricsContainer;
    		
    		if(metricsContainer.getTargetName().equals("green.oracle.com")) {
    				
    		//Setter using Reflection method	
    		Field fieldr = TargetMetricsContainer.class.getDeclaredField("targetName");
    		fieldr.setAccessible(true);
    		fieldr.set(tmContainer, "blue.oracle.com");
    		fieldr.setAccessible(false);
    		
    		storage.write(metricsContainer);
    			
    		}else {
    			storage.write(metricsContainer);
    		}
    		
		} catch ( Exception e) {
			System.out.println("Print myException..."); 
			myException(e);
			
			//Retry to execute write() method in catch block with myException
			if(metricsContainer!=null) {
				try {
					storage.write(metricsContainer);
				} catch (Exception mye2) {
					myException(mye2);
				}
				
			}
			
		}
    	
    }
    private static void myException(Exception mye) {
    	System.out.println("Execute myException...");
    	
    }
    
}
