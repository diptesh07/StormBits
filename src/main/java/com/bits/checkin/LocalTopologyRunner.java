package com.bits.checkin;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.generated.StormTopology;

public class LocalTopologyRunner {
	
	public static void main(String args[]) {
		Config config = new Config();
		config.setDebug(true); 
		
		StormTopology topology = HeatMapTopologyBuilder.build();
		
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("local-heatMap", config, topology);
		
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		cluster.killTopology(null);
		cluster.shutdown();
	}

}
