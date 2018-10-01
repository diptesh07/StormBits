package com.bits.trident;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.LocalDRPC;
import org.apache.storm.shade.com.google.common.collect.ImmutableList;
import org.apache.storm.trident.TridentState;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.operation.builtin.Count;
import org.apache.storm.trident.operation.builtin.MapGet;
import org.apache.storm.trident.testing.FeederBatchSpout;
import org.apache.storm.trident.testing.MemoryMapState;
import org.apache.storm.tuple.Fields;

public class LogAnalyserTrident {

	public static void main(String[] args) {

		System.out.println("Log Analyser Trident");
		TridentTopology topology = new TridentTopology();

		FeederBatchSpout testSpout = new FeederBatchSpout(
				ImmutableList.of("fromMobileNumber", "toMobileNumber", "duration"));

		TridentState callCounts = topology.newStream("fixed-batch-spout", testSpout)
				.each(new Fields("fromMobileNumber", "toMobileNumber"), new FormatCall(), new Fields("call"))
				.groupBy(new Fields("call"))
				.persistentAggregate(new MemoryMapState.Factory(), new Count(), new Fields("count"));

		LocalDRPC drpc = new LocalDRPC();
//
//		topology.newDRPCStream("call_count", drpc).stateQuery(callCounts, new Fields("args"), new MapGet(),
//				new Fields("count"));

		Config conf = new Config();
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("trident", conf, topology.build());

		System.out.println("DRPC : Query starts");
		System.out.println(drpc.execute("call_count", "1234123401 - 1234123402"));
//		System.out.println(drpc.execute("multiple_call_count", "1234123401 - 1234123402,1234123401 - 1234123403"));
		System.out.println("DRPC : Query ends");

		cluster.shutdown();
		drpc.shutdown();
	}

}
