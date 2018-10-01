package com.bits.cabs;

import java.util.HashMap;
import java.util.Map;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

public class UserCount extends BaseBasicBolt {

	private Map<String, Integer> userCounter = new HashMap<String, Integer>();

	public void execute(Tuple tuple, BasicOutputCollector outputCollector) {
		String userContact = tuple.getStringByField("userContact");

		//If the list already contains the present user contact, increase his number of trips by 1, else add him to the list
		if (userCounter.containsKey(userContact))
			userCounter.replace(userContact, userCounter.get(userContact) + 1);
		else
			userCounter.put(userContact, 1);
	}

	//This method is called at shutdown of cluster
	public void cleanup() {
		int max= 0;String key = "";
		for (Map.Entry<String, Integer> entry : userCounter.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
			if(entry.getValue()>max) {
				key = entry.getKey();
				max=entry.getValue();
			}
		}
		System.out.println("The most popular user is with contact : "+key+" having taken "+userCounter.get(key)+" trips.");
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		//This bolt does not return anything, so empty output field
	}

}
