package com.tencent.dao;

import com.tencent.domain.Tongji2Info;
import com.tencent.domain.Tongji3Info;

import java.sql.*;

public class MysqlDao {
    public MysqlDao() {

    }

    public static void toMysqlTongji2(Tongji2Info t2Info) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.88.81:3306/fluxdb", "root", "root");
            conn.setAutoCommit(false);

            ps = conn.prepareStatement("select * from tongji2 where reportTime = ?");
            ps.setDate(1, t2Info.getDate());

            rs = ps.executeQuery();

            if (rs.next()) {
                ps = conn.prepareStatement("update tongji2 set pv=pv+? , uv = uv+? ,vv =vv+?,newip=newip+?,newcust=+newcust+? where reportTime = ?");
                ps.setInt(1, t2Info.getPv());
                ps.setInt(2, t2Info.getUv());
                ps.setInt(3, t2Info.getVv());
                ps.setInt(4, t2Info.getNewip());
                ps.setInt(5, t2Info.getNewcust());
                ps.setDate(6, t2Info.getDate());
                ps.executeUpdate();
            } else {
                ps = conn.prepareStatement("insert into tongji2 values (?,?,?,?,?,?)");
                ps.setDate(1, t2Info.getDate());
                ps.setInt(2, t2Info.getPv());
                ps.setInt(3, t2Info.getUv());
                ps.setInt(4, t2Info.getVv());
                ps.setInt(5, t2Info.getNewip());
                ps.setInt(6, t2Info.getNewcust());
                ps.executeUpdate();
            }
            conn.commit();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
                throw new RuntimeException(e);
            }
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    rs = null;
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    ps = null;
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    conn = null;
                }
            }
        }
    }

    public static void toMysqlTongji3(Tongji3Info t3Info) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.88.81:3306/fluxdb", "root", "root");
            conn.setAutoCommit(false);

            ps = conn.prepareStatement("insert into tongji3 values (?,?,?,?)");
            ps.setTimestamp(1,t3Info.getTime());
            ps.setDouble(2,t3Info.getBr());
            ps.setDouble(3,t3Info.getAvgtime());
            ps.setDouble(4,t3Info.getAvgdeep());
            ps.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
                throw new RuntimeException(e);
            }
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    ps = null;
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    conn = null;
                }
            }
        }
    }
}
