package keti.storm.kafka.bolt;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.text.SimpleDateFormat;

public class PacketCountBolt extends BaseBasicBolt{
	int nMsg = 100;
	int np = 0;
        long nb = 0L;
        long startMs = 0L;
        long endMs = 0L;
        SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm:ss:SSS");

	String packet;
    	String[] words;

	public PacketCountBolt(Integer n){
		nMsg = n;	
	}

	public void execute(Tuple input, BasicOutputCollector collector) {
		if(input!=null){

			packet = input.getString(0);
			np++;
			nb += packet.getBytes().length;
			if (np == 1){
				System.out.println("consuming start....");
                		startMs = System.currentTimeMillis();
			}
			if (np == nMsg){
				endMs = System.currentTimeMillis();
                		double elapsedSecs = (endMs - startMs) / 1000.0;
                		System.out.println("-----------------------/ ------------------ / ---------------- / --------------- /-----------------/ -------------- / ----------- ");
                		//System.out.println("      start.time      /      end.time      / consumed.in.nMsg /  Msg_count.sec  / performancetime / consumed.in.MB /    MB.sec ");
                		//System.out.println("---------------------/ ------------------ / ---------------- / --------------- /-----------------/ -------------- / ----------- ");
                		//System.out.printf(("%s / %s / %16d / %15.2f / %15.4f / %14d / %.4f /").format(df.format(startMs), df.format(endMs), np, np / elapsedSecs, elapsedSecs, nb, nb / elapsedSecs));
				System.out.print("\u001B[31m");
                		System.out.printf("start time : %s , end time : %s\n", df.format(startMs), df.format(endMs) );
                		System.out.printf("Message count: %d\n", np);
                		System.out.printf("Mbytes : %d , msg per Sec : %f\n", nb, np / elapsedSecs);
                		System.out.printf("perf time : %.4f , MB per Sec : %.4f\n", elapsedSecs, nb / elapsedSecs);
                		System.out.println(elapsedSecs);
                		System.out.println(nb / elapsedSecs);
				System.out.print("\u001B[0m");

                		System.out.println("-------------------/ ------------------ / ---------------- / --------------- /-----------------/ -------------- / ----------- ");
                		System.exit(0);
                		}	

			if (packet != null) {
				words = packet.split("\\s+");
		        	for (String word: words) {
                    			collector.emit(new Values(word));
                		}
			}

			}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("words"));
	}
}
