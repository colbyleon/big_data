package com.tencent.flux;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

import java.util.List;
import java.util.Map;

public class VvBolt extends BaseRichBolt {

    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        try {
            String sscount = input.getStringByField("sscount");
            String vv = sscount.equals("0") ? "1" : "0";

            List<Object> values = input.getValues();
            values.add(vv);
            collector.emit(input, values);
            collector.ack(input);
        } catch (Exception e) {
            collector.fail(input);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("url", "urlname", "ref", "uagent", "uvid", "ssid", "sscount", "sstime", "cip", "pv", "uv", "vv"));

    }
}
