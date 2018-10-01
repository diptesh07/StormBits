package com.bits.checkin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.storm.Config;
import org.apache.storm.Constants;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import com.google.code.geocoder.model.LatLng;

public class HeatMapBuilder extends BaseBasicBolt {

	private Map<Long, List<LatLng>> heatMaps;

	public void declareOutputFields(OutputFieldsDeclarer fieldsDeclarer) {
		fieldsDeclarer.declare(new Fields("time", "heatmap"));
	}

	public void prepare(Map config, TopologyContext context) {
		heatMaps = new HashMap<Long, List<LatLng>>();
	}

	public void execute(Tuple tuple, BasicOutputCollector outputCollector) {
		if (isTickTuple(tuple) == false) {
//			Long time = tuple.getLongByField("time");
			Long timeInterval = tuple.getLongByField("time-interval");
			LatLng geoCode = (LatLng) tuple.getValueByField("geocode");

//			Long timeInterval = selectTimeInterval(time);
			List<LatLng> checkins = getCheckingForInterval(timeInterval);
			checkins.add(geoCode);
		}
		else {
			emitHeatMap(outputCollector);
		}
			
	}

	private void emitHeatMap(BasicOutputCollector outputCollector) {
		Long now = Long.valueOf("1531923143000");
		Long emitUpToTimeInterval = selectTimeInterval(now);
		Set<Long> timeIntervalsAvailable = heatMaps.keySet();
		
		for(Long timeInterval : timeIntervalsAvailable) {
//			if(timeInterval <= emitUpToTimeInterval) {
				List<LatLng> hotZones = heatMaps.remove(timeInterval);
				outputCollector.emit(new Values(timeInterval,hotZones));
//			}
		}
	}

	private boolean isTickTuple(Tuple tuple) {
		String sourceComponent = tuple.getSourceComponent();
		String sourceStreamId = tuple.getSourceStreamId();
		return sourceComponent.equals(Constants.SYSTEM_COMPONENT_ID) && sourceStreamId.equals(Constants.SYSTEM_TICK_STREAM_ID);
	}

	public Map<String, Object> getComponentConfiguration() {
		Config conf = new Config();
		conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 10);
		return conf;
	}

	private List<LatLng> getCheckingForInterval(Long timeInterval) {
		List<LatLng> hotZones = heatMaps.get(timeInterval);
		if (hotZones == null) {
			hotZones = new ArrayList<LatLng>();
			heatMaps.put(timeInterval, hotZones);
		}
		return hotZones;
	}

	private Long selectTimeInterval(Long time) {
		return time / (15 * 1000);
	}
}
