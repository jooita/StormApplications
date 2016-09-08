package keti.storm.kafka.topology;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import keti.storm.kafka.bolt.PacketCountBolt;
import keti.storm.kafka.bolt.WordCountBolt;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.apache.storm.LocalCluster;


public class WordCountTopologyLocal {


	public static void main(String[] args) throws Exception {

		
		String zkUrl = "kafka:2181"; // zookeeper url
		String brokerUrl = "kafka:9092";
		int nMsg = 100;
		boolean debug = false;

		if (args.length == 1 && args[0].matches("^-h|--help$")) {
			System.out.println("Usage: ENOW [kafka zookeeper url] [kafka broker url]");
			System.out.println("   E.g ENOW [" + zkUrl + "]" + " [" + brokerUrl + "]");
			System.exit(1);
		} else if (args.length == 1) {
			nMsg = Integer.parseInt(args[0]);
		} else if (args.length == 2) {

			if (args[1].matches("^-d|--debug$")) {
				debug = true;
			}
		}

		System.out.println("Using Kafka zookeeper url: " + zkUrl + " broker url: " + brokerUrl);

		ZkHosts hosts = new ZkHosts(zkUrl);
		SpoutConfig spoutConfig = new SpoutConfig(hosts, "packet", "/packet", UUID.randomUUID().toString());
		spoutConfig.startOffsetTime = kafka.api.OffsetRequest.EarliestTime();
		spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
		KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("kafkaspout", kafkaSpout, 1);
		builder.setBolt("packetcount", new PacketCountBolt(nMsg), 1).shuffleGrouping("kafkaspout");
		builder.setBolt("wordcount", new WordCountBolt(), 1).fieldsGrouping("packetcount", new Fields("words"));

		Config conf = new Config();
		conf.setDebug(debug);
		conf.setNumWorkers(1);

		List<String> nimbus_seeds = new ArrayList<String>();
		// nimbus url
		nimbus_seeds.add("localhost");
		
		List<String> zookeeper_servers = new ArrayList<String>();
		zookeeper_servers.add("localhost");

			// =============================
			// local mode
			// =============================
			 LocalCluster cluster = new LocalCluster();
			 cluster.submitTopology("log-stat", conf, builder.createTopology());
			 Thread.sleep(1000000000);
			 cluster.shutdown();
	}
}
