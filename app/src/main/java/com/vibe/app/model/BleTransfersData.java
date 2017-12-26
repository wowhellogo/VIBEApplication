package com.vibe.app.model;


import com.vibe.app.utils.ByteUtil;

/**
 * @Package com.mk.lock.model
 * @作 用:蓝牙传输协议指令封装,使用构建者模式
 * @创 建 人: linguoding 邮箱：linggoudingg@gmail.com
 * @日 期: 2017年11月24日  10:58
 */


public class BleTransfersData {
    public final static int CMD = 0xAA;
    public final static int DATA = 0xBB;
    private final static int MAX_LENGTH = 28;//最大长度28
    public int head = CMD;//包头,默认为指令包
    public Cmd mCmd;
    private int length;
    private byte[] content;//转输内容
    private byte check;//包的逐位相加

    public int getHead() {
        return head;
    }

    public Cmd getCmd() {
        return mCmd;
    }

    public int getLength() {
        return length;
    }

    public byte[] getContent() {
        return content;
    }

    public byte getCheck() {
        return check;
    }

    private BleTransfersData(int head, int length, Cmd cmd, byte[] content, byte check) {
        this.head = head;
        this.length = length;
        mCmd = cmd;
        this.content = content;
        this.check = check;
    }


    public static class Builder {
        public int head = CMD;//包头
        public Cmd mCmd;
        private int length;
        private byte[] content;//转输内容
        private byte check;//包的逐位相加

        public BleTransfersData builder() {
            return new BleTransfersData(head, length, mCmd, content, check);
        }


        public Builder setHead(int head) {
            this.head = head;
            return this;
        }

        public Builder setCmd(Cmd cmd) {
            mCmd = cmd;
            return this;
        }

        public Builder setContent(byte[] content) {
            this.content = content;
            this.length = this.content.length + 4;
            return this;
        }

        public Builder setContent(String content) {
            setContent(conversionAsciiBytesByString(content));
            return this;
        }

        public Builder setLength(int length) {
            this.length = length;
            return this;
        }

        public Builder setCheck(byte check) {
            this.check = check;
            return this;
        }
    }


    /**
     * 封包
     *
     * @return
     */
    public byte[] dataPackage() {
        byte[] bts = new byte[length];
        bts[0] = (byte) this.head;
        bts[1] = (byte) this.length;
        bts[2] = (byte) mCmd.cmd;
        this.check = (byte) (bts[0] + bts[1] + bts[2]);
        for (int i = 0; i < content.length; i++) {
            bts[i + 3] = content[i];
            this.check += content[i];
        }
        bts[length - 1] = this.check;
        return bts;
    }

    /**
     * 解包
     *
     * @return
     */
    public static BleTransfersData parsePackage(byte[] bts) {
        byte content[] = new byte[bts.length - 4];
        System.arraycopy(bts, 3, content, 0, content.length);
        return new Builder()
                .setHead(bts[0])
                .setLength(bts[1])
                .setCmd(Cmd.valueCmd(bts[2]))
                .setContent(content)
                .setCheck(bts[bts.length - 1])
                .builder();
    }

    /**
     * 将字符串转换为ascii码字节数组
     *
     * @param str
     * @return
     */
    private static byte[] conversionAsciiBytesByString(String str) {
        byte[] bts = new byte[str.length()];
        for (int i = 0; i < bts.length; i++) {
            bts[i] = (byte) str.charAt(i);
        }
        return bts;
    }

    //指令
    public enum Cmd {
        OTA(0XFF),//OTA升级
        RE_TRANSFER(0XFB),//数据重传
        RE_FACTORY(0XF0),//恢复出厂
        RE_STATE(0XF1),//恢复状态
        SET_DATE(0XF2),//设置时间
        OPEN_LOCK(0x01),//开锁
        CLOSE_LOCK(0X02),//关锁
        SYN_PASSWORD(0X11),//同步密码
        SYN_TEMP_PASSWORD(0X12),//同步一次性密码
        SET_ADMIN_PASSWORD(0X13),//设置管理密码
        TRANSFER_PASSWORD(0XA1),//传输密码
        HAS_CMD(0XB1);//哈希指令
        private byte cmd;

        Cmd(int cmd) {
            this.cmd = (byte) cmd;
        }

        public int getCmd() {
            return cmd;
        }

        public static Cmd valueCmd(byte cmd) {
            for (Cmd item : Cmd.values()) {
                if (item.cmd == cmd) {
                    return item;
                }
            }
            return null;
        }
    }

    public static byte[] getTransferPasswordContent(int num, int index, String password) {
        byte[] bytes = new byte[password.length() + 2];
        bytes[0] = (byte) num;
        bytes[1] = (byte) index;
        for (int i = 0; i < password.length(); i++) {
            bytes[i + 2] = (byte) password.charAt(i);
        }
        return bytes;
    }

    @Override
    public String toString() {
        return "BleTransfersData{" +
                "head=" + head +
                ", mCmd=" + mCmd +
                ", length=" + length +
                ", content=" + ByteUtil.bytesToHexString(content) +
                ", check=" + check +
                '}';
    }
}
