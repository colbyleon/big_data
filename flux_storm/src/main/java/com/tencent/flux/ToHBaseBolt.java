package com.tencent.flux;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import com.tencent.dao.HBaseDao;
import com.tencent.domain.FluxInfo;

import java.util.Map;

public class ToHBaseBolt extends BaseRichBolt {

    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;

    }

    @Override
    public void execute(Tuple input) {
        try {
            String url = input.getStringByField("url");
            String urlname = input.getStringByField("urlname");
            String ref = input.getStringByField("ref");
            String uvid = input.getStringByField("uvid");
            String ssid = input.getStringByField("ssid");
            String sscount = input.getStringByField("sscount");
            String sstime = input.getStringByField("sstime");
            String cip = input.getStringByField("cip");

            FluxInfo fluxInfo = new FluxInfo(url, urlname,ref, uvid, ssid, sscount, sstime, cip);
            HBaseDao.toHbaseFluxTab(fluxInfo);
            collector.ack(input);
        } catch (Exception e) {
            e.printStackTrace();
            collector.fail(input);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }
}
