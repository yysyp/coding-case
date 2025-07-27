/**Copyright CopyLeft it's Fri Feb 28 23:46:35 CST 2020 Good luck~~~*/
package ps.demo.commonlibx.common;

import java.math.BigDecimal;

public enum MyBitOpEnum {

    FLAG1(1, "FLAG1"),
    FLAG2(2, "FLAG2"),
    FLAG3(3, "FLAG3"),
    FLAG4(4, "FLAG4");

    private int bit;
    private String status;

    MyBitOpEnum(int bit, String status) {
        this.bit = bit;
        this.status = status;
    }

    public int getBit() {
        return bit;
    }

    public void setBit(int bit) {
        this.bit = bit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //
    public long getBit2Number() {
        BigDecimal posValue = new BigDecimal(2);
        posValue = posValue.pow(bit);
        return posValue.longValue();
    }

    public boolean isBitOn(Long flag) {
        if (flag == null || flag == 0L) {
            return false;
        }
        if ((flag & getBit2Number()) == getBit2Number()) {
            return true;
        }
        return false;
    }

    public static MyBitOpEnum findByStatus(String status) {
        for (MyBitOpEnum e : MyBitOpEnum.values()) {
            if (e.getStatus().equals(status)) {
                return e;
            }
        }
        return null;
    }

}
