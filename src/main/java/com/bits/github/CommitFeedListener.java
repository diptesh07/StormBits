package com.bits.github;

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

public class CommitFeedListener extends BaseRichSpout {

	private SpoutOutputCollector outputCollector;
	private List<String> commits;

	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.outputCollector = collector;
		try {
			commits = IOUtils.readLines(ClassLoader.getSystemResourceAsStream("changeLog.txt"),
					Charset.defaultCharset().name());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void nextTuple() {
		for (String commit : commits) {
			System.out.println(commit);
			outputCollector.emit(new Values(commit));
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("commit"));
	}

}
