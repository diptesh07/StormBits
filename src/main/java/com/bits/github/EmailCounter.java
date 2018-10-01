package com.bits.github;

import java.util.HashMap;
import java.util.Map;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

public class EmailCounter extends BaseBasicBolt {

	private Map<String, Integer> counts;

	public void execute(Tuple tuple, BasicOutputCollector outputCollector) {
		// TODO Auto-generated method stub
		String email = tuple.getStringByField("email");
		counts.put(email, countFor(email) + 1);
		printCounts();
	}

	private void printCounts() {
		for (String email : counts.keySet()) {
			System.out.println(String.format("%s has count of %s", email, counts.get(email)));
		}
	}

	private int countFor(String email) {
		Integer count = counts.get(email);
		return count == null ? 0 : count;
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {

	}

	public void prepare(Map config, TopologyContext context) {
		counts = new HashMap<String, Integer>();
	}
	
//	public void cleanUp() {
////		printCounts();
//		for (String email : counts.keySet()) {
//			System.out.println(String.format("%s has count of %s", email, counts.get(email)));
//		}
//	}

}
