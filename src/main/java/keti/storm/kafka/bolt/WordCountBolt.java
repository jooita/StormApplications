package keti.storm.kafka.bolt;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class WordCountBolt extends BaseBasicBolt{

	HashMap<String,Integer> wordCount = new HashMap<String,Integer>();

	public void execute(Tuple input, BasicOutputCollector collector) {
		if(input!=null){

			String word = input.getString(0);
			Integer count = wordCount.get(word);
            		if (count == null)
                		count = 0;
            		count++;
            		wordCount.put(word, count);
			/*
            		if(wordCount!=null && wordCount.size() > 0){
                		List<String> keys = new ArrayList<String>(wordCount.keySet());
                		for (String key:keys) {
                    			System.out.println("Word = "+key+"\t \t \t Count = "+wordCount.get(key));
                		}
            		}
			*/

            		collector.emit(new Values(word, count));
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word","count"));
	}
}
