package com.tencent.flux2;

import backtype.storm.coordination.BatchOutputCollector;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBatchBolt;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import com.tencent.dao.HBaseDao;
import com.tencent.domain.FluxInfo;

import java.math.BigDecimal;
import java.util.*;

public class AvgDeepBolt extends BaseRichBolt {

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

        Map<String, Set<String>> map = new HashMap<>();
        fluxInfos.forEach(fi -> {
            if (map.containsKey(fi.getSsid())) {
                Set<String> set = map.get(fi.getSsid());
                set.add(fi.getUrlname());
            }else {
                HashSet<String> set = new HashSet<>();
                set.add(fi.getUrlname());
                map.put(fi.getSsid(), set);
            }
        });
        int scount = map.size();
        int alldeep = alldeep = map.entrySet().stream().mapToInt(e -> e.getValue().size()).sum();

        Double avgdeep = 0.0;
        if (scount != 0) {
            avgdeep = new BigDecimal(alldeep).divide(new BigDecimal(scount),4,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        List<Object> values = input.getValues();
        values.add(avgdeep);
        collector.emit(values);


    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("time","br","avgtime","avgdeep"));
    }
}
