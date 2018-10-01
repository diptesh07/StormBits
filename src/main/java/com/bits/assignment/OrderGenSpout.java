package com.bits.assignment;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.storm.shade.org.apache.commons.io.IOUtils;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

public class OrderGenSpout extends BaseRichSpout{
	
	private SpoutOutputCollector collector;
	private List<String> orders;

	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
		try {
			orders = IOUtils.readLines(ClassLoader.getSystemResourceAsStream("orders.txt"),
					Charset.defaultCharset().name());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void nextTuple() {
		for (String order : orders) {
			collector.emit(new Values(order));
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
			
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("order"));
	}

}
