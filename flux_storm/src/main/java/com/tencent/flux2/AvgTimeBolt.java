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
import java.util.*;

public class AvgTimeBolt extends BaseRichBolt {

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
        Map<String, List<FluxInfo>> map = new HashMap<>();
        fluxInfos.forEach(fi -> {
           if (map.containsKey(fi.getSsid())){
               List<FluxInfo> fiList = map.get(fi.getSsid());
               fiList.add(fi);
           }else {
               List<FluxInfo> fiList = new ArrayList<>();
               fiList.add(fi);
               map.put(fi.getSsid(), fiList);
           }
        });

        int scount = map.size();
        long useTime = 0;
        for (Map.Entry<String, List<FluxInfo>> entry : map.entrySet()) {
            LongSummaryStatistics statistics = entry.getValue().stream()
                    .mapToLong(f -> Long.parseLong(f.getStime()))
                    .summaryStatistics();
            useTime += statistics.getMax()-statistics.getMin();
        }

        Double avgtime = 0.0;
        if (scount != 0) {
            avgtime = new BigDecimal(useTime).divide(new BigDecimal(scount),4,BigDecimal.ROUND_HALF_UP).doubleValue();
        }

        List<Object> values = input.getValues();
        values.add(avgtime);
        collector.emit(values);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("time","br","avgtime"));
    }
}
