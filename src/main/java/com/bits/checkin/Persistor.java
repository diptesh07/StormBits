package com.bits.checkin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.geocoder.model.LatLng;

public class Persistor extends BaseBasicBolt{
	
	private ObjectMapper objectMapper;
	
	public void prepare(Map stormConf, TopologyContext contect) {
		objectMapper = new ObjectMapper();
	}
	
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		Long timeInterval = tuple.getLongByField("time");
		List<LatLng> heatZones = (List<LatLng>) tuple.getValueByField("heatmap");
		List<String> hotZones = asListofStrings(heatZones);
		
		try {
			String key = "checkins - " + timeInterval;
			String value = objectMapper.writeValueAsString(hotZones);
			WriteToFile.writeToFile(key+" "+value+"\n");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
	}

	private List<String> asListofStrings(List<LatLng> heatZones) {
		List<String> hotZonesStandard = new ArrayList<String>(heatZones.size());
		for(LatLng coOrd : heatZones) {
			hotZonesStandard.add(coOrd.toUrlValue());
		}
		return hotZonesStandard;
	}

}
