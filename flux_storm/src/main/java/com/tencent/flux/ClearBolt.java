package com.tencent.flux;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class ClearBolt extends BaseRichBolt {

    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        try {
            String str = input.getStringByField("str");
            String [] attrs = str.split("[|]");
            String url = attrs[0];
            String urlname = attrs[1];
            String ref = attrs[11];
            String uagent = attrs[12];
            String uvid = attrs[13];
            String ssid = attrs[14].split("_")[0];
            String sscount = attrs[14].split("_")[1];
            String sstime = attrs[14].split("_")[2];
            String cip = attrs[15];
            collector.emit(input,new Values(url,urlname,ref,uagent,uvid,ssid,sscount,sstime,cip));
            collector.ack(input);
        } catch (Exception e) {
            e.printStackTrace();
            collector.fail(input);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("url","urlname","ref","uagent","uvid","ssid","sscount","sstime","cip"));
    }
}
