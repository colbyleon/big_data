package com.tencent.dao;

import com.tencent.domain.FluxInfo;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HBaseDao {

    public static void toHbaseFluxTab(FluxInfo fi) {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "192.168.88.81:2181,192.168.88.82:2181,192.168.88.83:2181");
        try (
                HTable tab = new HTable(conf, "flux".getBytes())
        ) {
            Put put = new Put(fi.getRK().getBytes());
            put.add("cf1".getBytes(), "url".getBytes(), fi.getUrl().getBytes());
            put.add("cf1".getBytes(), "urlname".getBytes(), fi.getUrlname().getBytes());
            put.add("cf1".getBytes(), "ref".getBytes(), fi.getUrlname().getBytes());
            put.add("cf1".getBytes(), "uvid".getBytes(), fi.getUvid().getBytes());
            put.add("cf1".getBytes(), "ssid".getBytes(), fi.getSsid().getBytes());
            put.add("cf1".getBytes(), "sscount".getBytes(), fi.getScount().getBytes());
            put.add("cf1".getBytes(), "sstime".getBytes(), fi.getStime().getBytes());
            put.add("cf1".getBytes(), "cip".getBytes(), fi.getCip().getBytes());
            tab.put(put);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static List<FluxInfo> queryHbaseFluxTab(byte[] startRow, byte[] stopRow, String rkRegex) {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "192.168.88.81:2181,192.168.88.82:2181,192.168.88.83:2181");
        try (
                HTable tab = new HTable(conf, "flux".getBytes())
        ) {
            // 1. 创建扫描器
            Scan scan = new Scan();

            // 2. 设置扫描范围
            scan.setStartRow(startRow == null ? scan.getStartRow() : startRow);
            scan.setStopRow(stopRow == null ? scan.getStopRow() : stopRow);
            if (rkRegex != null) {
                Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(rkRegex));
                scan.setFilter(filter);
            }

            // 3. 扫描数据
            ResultScanner rs = tab.getScanner(scan);
            List<FluxInfo> list = new ArrayList<>();
            rs.forEach(result -> {
                String url = new String(result.getValue("cf1".getBytes(), "url".getBytes()));
                String urlname = new String(result.getValue("cf1".getBytes(), "urlname".getBytes()));
                String ref = new String(result.getValue("cf1".getBytes(), "ref".getBytes()));
                String uvid = new String(result.getValue("cf1".getBytes(), "uvid".getBytes()));
                String ssid = new String(result.getValue("cf1".getBytes(), "ssid".getBytes()));
                String scount = new String(result.getValue("cf1".getBytes(), "sscount".getBytes()));
                String stime = new String(result.getValue("cf1".getBytes(), "sstime".getBytes()));
                String cip = new String(result.getValue("cf1".getBytes(), "cip".getBytes()));
                list.add(new FluxInfo(url,urlname,ref,uvid,ssid,scount,stime,cip));
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
