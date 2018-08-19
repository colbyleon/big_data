package com.tencent.flux;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import com.tencent.flux2.*;
import storm.kafka.*;

import java.util.UUID;

public class FluxDriver {
    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException, InterruptedException {
        System.setProperty("hadoop.home.dir", "D:\\GreenSoft\\hadoopbin_for_hadoop2.7.1");


        // 1.创建组件
        BrokerHosts hosts = new ZkHosts("192.168.88.81:2181,192.168.88.82:2181,192.168.88.83:2181");
        SpoutConfig spoutConfig = new SpoutConfig(hosts, "park", "/park", UUID.randomUUID().toString());
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);

        // 2.创建构建者
        TopologyBuilder builder = new TopologyBuilder();

        // 3.向构建者声明拓扑结构
        builder.setSpout("KAFKA_SPOUT", kafkaSpout);
        builder.setBolt("CLEAR_BOLT", new PrintBolt()).shuffleGrouping("KAFKA_SPOUT");
        builder.setBolt("PV_BOLT", new ClearBolt()).shuffleGrouping("CLEAR_BOLT");
        builder.setBolt("UV_BOLT", new PvBolt()).shuffleGrouping("PV_BOLT");
        builder.setBolt("VV_BOLT", new UvBolt()).shuffleGrouping("UV_BOLT");
        builder.setBolt("NEW_IP_BOLT", new VvBolt()).shuffleGrouping("VV_BOLT");
        builder.setBolt("NEW_CUST_BOLT", new NewIpBolt()).shuffleGrouping("NEW_IP_BOLT");
        builder.setBolt("TO_MYSQL_BOLT", new NewCustBolt()).shuffleGrouping("NEW_CUST_BOLT");
        builder.setBolt("TO_HBASE_BOLT", new ToMysqlBolt()).shuffleGrouping("NEW_CUST_BOLT");
        builder.setBolt("PRINT_BOLT", new ToHBaseBolt()).shuffleGrouping("NEW_CUST_BOLT");

        // 4.创建拓扑
//        Config config = new Config();
//        StormSubmitter.submitTopology("FLUX_TOPOLOGY",config,builder.createTopology());

        /**
         * 伪实时计算拓扑
         */
        TopologyBuilder builder2 = new TopologyBuilder();
        builder2.setBolt("TIME_BOLT", new TimeBolt());
        builder2.setBolt("BR_BOLT", new BrBolt()).shuffleGrouping("TIME_BOLT");
        builder2.setBolt("AVG_TIME_BOLT", new AvgTimeBolt()).shuffleGrouping("BR_BOLT");
        builder2.setBolt("AVG_DEEP_BOLT", new AvgDeepBolt()).shuffleGrouping("AVG_TIME_BOLT");
        builder2.setBolt("PRINT_BOLT", new PrintBolt()).shuffleGrouping("AVG_DEEP_BOLT");
        builder2.setBolt("TO_MYSQL2_BOLT", new ToMysqlBolt2()).shuffleGrouping("AVG_DEEP_BOLT");

        // 5.提交拓扑到集群中运行
        LocalCluster cluster = new LocalCluster();
        Config config = new Config();
//        Config config2 = new Config();
//        config2.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 5);

        cluster.submitTopology("FLUX_TOPOLOGY", config, builder.createTopology());
        cluster.submitTopology("FLUX_TOPOLOGY2", config, builder2.createTopology());

        Thread.sleep(1000 * 1000);
        cluster.killTopology("FLUX_TOPOLOGY");
        cluster.killTopology("FLUX_TOPOLOGY2");
        cluster.shutdown();
    }
}
