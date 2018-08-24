package com.tencent.flux;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import com.tencent.dao.HBaseDao;
import com.tencent.domain.FluxInfo;

import java.util.List;
import java.util.Map;

public class NewCustBolt extends BaseRichBolt {

    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        try {
            String uvid = input.getStringByField("uvid");

            List<FluxInfo> fluxInfos = HBaseDao.queryHbaseFluxTab(null,input.getStringByField("sstime").getBytes(),"^\\d+_"+uvid+"_.*$");
            String newCust = fluxInfos.size() == 0 ? "1" : "0";

            List<Object> values = input.getValues();
            values.add(newCust);

            collector.emit(input, values);
            collector.ack(input);
        } catch (Exception e) {
            e.printStackTrace();
            collector.fail(input);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("url", "urlname", "ref", "uagent", "uvid", "ssid", "sscount", "sstime", "cip", "pv", "uv", "vv","newip","newcust"));

    }
}
