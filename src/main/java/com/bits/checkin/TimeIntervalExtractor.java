package com.bits.checkin;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import com.google.code.geocoder.model.LatLng;

public class TimeIntervalExtractor extends BaseBasicBolt{
	
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("time-interval","geocode"));
	}
	
	public void execute(Tuple tuple, BasicOutputCollector outputCollector) {
		Long time = tuple.getLongByField("time");
		LatLng geocode = (LatLng) tuple.getValueByField("geocode");
		
		Long timeInterval = time/15000;
		outputCollector.emit(new Values(timeInterval,geocode));
	}

}
