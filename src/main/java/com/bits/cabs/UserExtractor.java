package com.bits.cabs;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

public class UserExtractor extends BaseBasicBolt{

	public void execute(Tuple tuple, BasicOutputCollector outputCollector) {
		String tripDetails = tuple.getStringByField("trip");
		String parts[] = tripDetails.split(" ");
		outputCollector.emit(new Values(parts[2]));
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("userContact"));
	}
}
