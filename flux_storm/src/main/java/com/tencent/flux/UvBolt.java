package com.tencent.flux;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import com.tencent.dao.HBaseDao;
import com.tencent.domain.FluxInfo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

public class UvBolt extends BaseRichBolt {

    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        try {
            String uvid = input.getStringByField("uvid");

            // 找今天零时之后的数据，没有的话，今天就是第一次来
            LocalDateTime now = LocalDateTime.now();
            long beginTime = now.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
            long stopTime = now.toInstant(ZoneOffset.of("+8")).toEpochMilli();

            List<FluxInfo> list = HBaseDao.queryHbaseFluxTab((beginTime+"").getBytes(),(stopTime+"").getBytes(),"^\\d+_"+uvid+"_.*$");

            String uv = list.size() == 0 ? "1" : "0";
            List<Object> values = input.getValues();
            values.add(uv);
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
        declarer.declare(new Fields("url","urlname","ref","uagent","uvid","ssid","sscount","sstime","cip","pv","uv"));
    }
}
