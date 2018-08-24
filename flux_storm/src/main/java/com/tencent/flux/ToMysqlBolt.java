package com.tencent.flux;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import com.tencent.dao.MysqlDao;
import com.tencent.domain.Tongji2Info;

import java.sql.Date;
import java.util.Map;

public class ToMysqlBolt extends BaseRichBolt {

    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        try {
            int pv = Integer.parseInt(input.getStringByField("pv"));
            int uv = Integer.parseInt(input.getStringByField("uv"));
            int vv = Integer.parseInt(input.getStringByField("vv"));
            int newip = Integer.parseInt(input.getStringByField("newip"));
            int newcust = Integer.parseInt(input.getStringByField("newcust"));
            long sstime = Long.parseLong(input.getStringByField("sstime"));

            Tongji2Info tongji2Info = new Tongji2Info(new Date(sstime), pv, uv, vv, newip, newcust);
            MysqlDao.toMysqlTongji2(tongji2Info);
            collector.ack(input);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            collector.fail(input);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
