package com.vibe.app.model;


import com.vibe.app.utils.ByteUtil;

/**
 * @Package com.mk.lock.model
 * @作 用:蓝牙传输协议指令封装,使用构建者模式
 * @创 建 人: linguoding 邮箱：linggoudingg@gmail.com
 * @日 期: 2017年11月24日  10:58
 */


public class BleTransfersData {
    public Cmd mCmd;
    private byte[] content;//转输内容
    private final static int length = 5;

    public Cmd getCmd() {
        return mCmd;
    }

    public byte[] getContent() {
        return content;
    }

    private BleTransfersData(Cmd mCmd, byte[] content) {
        this.mCmd = mCmd;
        this.content = content;
    }

    public static class Builder {
        public Cmd mCmd;
        private byte[] content;//转输内容

        public BleTransfersData builder() {
            return new BleTransfersData(mCmd, content);
        }


        public Builder setCmd(Cmd cmd) {
            mCmd = cmd;
            return this;
        }


        public Builder setContent(int...content){
            this.content=new byte[content.length];
            for(int i=0;i<content.length;i++){
                this.content[i]= (byte) content[i];
            }
            return this;
        }

        public Builder setContent(byte[] content){
            this.content=content;
            return this;
        }
    }


    /**
     * 封包
     *
     * @return
     */
    public byte[] dataPackage() {
        byte[] bts = {0x00, 0x00, 0x00, 0x00, 0x00};
        bts[0] = mCmd.cmd;
        for (int i = 0; i < content.length; i++) {
            bts[i + 1] = content[i];
        }
        return bts;

    }

    /**
     * 解包
     *
     * @return
     */
    public static BleTransfersData parsePackage(byte[] bts) {
        byte content[] = new byte[bts.length - 1];
        System.arraycopy(bts, 1, content, 0, content.length);
        return new Builder()
                .setCmd(Cmd.valueCmd(bts[0]))
                .setContent(content)
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
        SET_MODE(0X01),//设置模式或调节，           第二位  （1：加，2：减）          第三位：0~2（0：连续，1：间隔，2：波浪）
        SET_STRENGTH(0X02),//设置强度或调节         第二位：（1：加，2：减）          第三位：0~9
        SET_TIME_DURATION(0X04),//工作时长设置（0~25分钟）
        SET_ON_OFF(0X08),//开始停止设置（0：停止，1：开始）
        OTA(0X10),//请求升级（0：取消，1：开始）
        //设置工作状态,(工作时间：0~25分钟，设置时长：0~25分钟，当前强度：0~9，当前模式：0~2，开始停止状态：0：停止，1：开始，充电状态：0：正常，1：充电中)
        SET_JOB_STATE(0x20);
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

}
