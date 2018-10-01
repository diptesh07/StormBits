package com.bits.checkin;

import org.apache.storm.generated.StormTopology;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

public class HeatMapTopologyBuilder {

	public static StormTopology build() {
		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("checkins", new Checkins(),4).setNumTasks(4);

		builder.setBolt("geocode-lookup", new GeoCodeLookup(),8).setNumTasks(64).shuffleGrouping("checkins");
		
		builder.setBolt("timeIntervalExtractor", new TimeIntervalExtractor(),4).shuffleGrouping("geocode-lookup");

		builder.setBolt("heatMap-builder", new HeatMapBuilder(),4).fieldsGrouping("timeIntervalExtractor", new Fields("time-interval"));

		builder.setBolt("persistor", new Persistor(),1).setNumTasks(4).shuffleGrouping("heatMap-builder");

		return builder.createTopology();
	}

}
