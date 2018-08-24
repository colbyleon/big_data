package com.tencent.flux2;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import com.tencent.dao.HBaseDao;
import com.tencent.domain.FluxInfo;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrBolt extends BaseRichBolt {

    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        // 1.获取时间
        Long time = input.getLongByField("time");

        // 2.获取这之前半个小时的数据
        List<FluxInfo> fluxInfos = HBaseDao.queryHbaseFluxTab((time - 30 * 60 * 1000 + "").getBytes(), (time + "").getBytes(), null);

        // 3.基于数据计算br
        Map<String, Integer> map = new HashMap<>();
        fluxInfos.forEach(fi -> {
            map.put(fi.getSsid(), map.getOrDefault(fi.getSsid(), 0) + 1);
        });
        int scount = map.size();
        int brcount = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 0) brcount++;
        }
        Double br = 0.0;
        if (scount != 0) {
            br = new BigDecimal(brcount).divide(new BigDecimal(scount), 4, BigDecimal.ROUND_HALF_UP).doubleValue();
        }

        // 4.将br信息发送
        List<Object> values = input.getValues();
        values.add(br);
        collector.emit(values);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("time", "br"));
    }
}
