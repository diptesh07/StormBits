package com.bits.trident;

import org.apache.storm.Config;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.DRPCExecutionException;
import org.apache.storm.thrift.TException;
import org.apache.storm.trident.TridentState;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.operation.builtin.MapGet;
import org.apache.storm.trident.testing.FixedBatchSpout;
import org.apache.storm.trident.testing.MemoryMapState;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.DRPCClient;

public class TridentTrial {
	public static void main(String args[]) throws DRPCExecutionException, AuthorizationException, TException {

		FixedBatchSpout spout = new FixedBatchSpout(new Fields("sentence"), 3,
				new Values("the cow jumped over the moon"),
				new Values("the man went to the store and bought some candy"),
				new Values("four score and seven years ago"), new Values("how many apples can you eat"));
		spout.setCycle(true);

		TridentTopology topology = new TridentTopology();
		TridentState wordCounts = topology.newStream("spout1", spout)
				.each(new Fields("sentence"), new Split(), new Fields("word")).groupBy(new Fields("word"))
				// .aggregate(new Count(), new Fields("wordCount"))
				.persistentAggregate(new MemoryMapState.Factory(), new Count(), new Fields("count")).parallelismHint(6);

		topology.newDRPCStream("count-words").each(new Fields("args"), new SplitOnDelimiter(","), new Fields("tag"))
				.stateQuery(wordCounts, new MapGet(), new Fields("count"));

		Config conf = new Config();
		conf.setDebug(false);
		conf.put("storm.thrift.transport", "org.apache.storm.security.auth.SimpleTransportPlugin");
		conf.put(Config.STORM_NIMBUS_RETRY_TIMES, 3);
		conf.put(Config.STORM_NIMBUS_RETRY_INTERVAL, 10);
		conf.put(Config.STORM_NIMBUS_RETRY_INTERVAL_CEILING, 20);
		conf.put(Config.DRPC_MAX_BUFFER_SIZE, 1048576);

		// LocalDRPC drpc = new LocalDRPC();
		// LocalCluster cluster = new LocalCluster();
		//
		// cluster.submitTopology("drpc-demo", conf, topology.build());

		DRPCClient drpc = new DRPCClient(conf, "10.0.2.15", 3772);

		System.out.println("Results for 'hello':" + drpc.execute("count-words", "the"));

		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// cluster.shutdown();
		// drpc.shutdown();

	}

}
