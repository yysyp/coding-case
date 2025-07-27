package ps.demo.jpademo.test;


import ps.demo.commonlibx.common.StringXTool;

public class StringXToolTest {

    public static void main(String[] args) {
        String q = "负债和所有者权益或股东权益合计元";
        System.out.println("1>>"+ StringXTool.getLcsOrMixContainsRatio(q, "所有者权益或股东权益合计"));
        System.out.println("2>>"+StringXTool.getLcsOrMixContainsRatio(q, "负债和所有者权益或股东权益总计"));
        System.out.println("3>>"+StringXTool.getLcsOrMixContainsRatio(q, "负债和所有者权益或股东权益合计"));

    }
}
