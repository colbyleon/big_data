package com.tencent.logservlet;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {
    @Test
    public void test() {
        String str = "http://localhost:8080/A/自动跳转|自动跳转|晚上吃啥|UTF-8|1920x1080|24-bit|zh-cn|0|1||0.6096680562564598|http://localhost:8080/B/自动跳转|Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.75 Safari/537.36|14588146250228543436|1798820041_24_1534036027523|0:0:0:0:0:0:0:1";
        //|1798820041_24_1534036027523|0:
        Pattern p = Pattern.compile("^(?:[^|]*\\|){14}\\d+_\\d+_(\\d+)\\|[^|]*$");

        Matcher matcher = p.matcher(str);
        boolean matches = matcher.matches();

        System.out.println(matcher.groupCount());
        for (int i = 0; i <= matcher.groupCount(); i++) {
            System.out.println(matcher.group(i));
        }

        System.out.println(matcher.group(1));

    }

    @Test
    public void compute() throws IOException {
        String raw = "0.0\n" +
                "0.0\n" +
                "0.0\n" +
                "1471172.0\n" +
                "1020444.0\n" +
                "0.0\n" +
                "1399.0";

        StringReader sr = new StringReader(raw);
        BufferedReader br = new BufferedReader(sr);

        String num = "";
        Double sum = 0.0;
        Integer count = 0;
        while ((num = br.readLine()) != null) {
            sum += Double.parseDouble(num);
            count ++;
        }

        System.out.println(sum/count);
    }
}
