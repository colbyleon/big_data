package com.tencent.flux2;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import com.tencent.dao.MysqlDao;
import com.tencent.domain.Tongji3Info;

import java.sql.Timestamp;
import java.util.Map;

public class ToMysqlBolt2 extends BaseRichBolt {

    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        Long time = input.getLongByField("time");
        double br = input.getDoubleByField("br");
        double avgtime = input.getDoubleByField("avgtime");
        double avgdeep = input.getDoubleByField("avgdeep");

        Tongji3Info tongji3Info = new Tongji3Info(new Timestamp(time), br, avgtime, avgdeep);
        MysqlDao.toMysqlTongji3(tongji3Info);

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
