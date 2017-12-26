package com.hao.common.net.result;

/**
 * @Package com.mk.lock.utils
 * @作 用:API错误信息码
 * @创 建 人: linguoding 邮箱：linggoudingg@gmail.com
 * @日 期: 2017年11月13日  16:05
 */


public enum ErrorCode {
    REGISTERED(1, "用户已注册"),
    REQUEST_PARAMETER_ERROR(2, "请求参数错误"),
    REGISTER_FAIL(3, "用户注册失败"),
    VERIFICATION_CODE_ERROR(4, "验证码错误"),
    INCORRECT_USERNAME_OR_PASSWORD(5, "用户名或密码错误"),
    UPDATE_THE_USER_PASSWORD_FAILED(6, "更新用户密码失败"),
    USER_AUTHENTICATION_FAILED(7, "用户验证失败"),
    SEND_AUTHENTICATION_CODE_FAILED(8, "发送验证码失败"),
    SENDING_ACTIVATION_DEVICE_SMS_FAILED(9, "发送激活设备短信失败"),
    DEVICE_IS_NOT_BOUND(32, "设备未绑定用户账号"),
    FAILED_TO_GENERATE_VERIFICATION_CODE(10, "生成验证码失败"),
    AILED_TO_GENERATE_VERIFICATION_CODE(10, "生成验证码失败"),
    Error_CODE_11(11, "车位设备检测异常"),
    Error_CODE_12(12, "实名认证失败"),
    Error_CODE_13(13, "文件创建失败"),
    Error_CODE_14(14, "文件类型错误"),
    Error_CODE_15(15, "获取用户信息失败"),
    Error_CODE_16(16, " 获取用户订单列表失败"),
    Error_CODE_17(17, "车位关锁异常"),
    Error_CODE_18(18, "车位开锁异常"),
    Error_CODE_19(19, "设备ID不正确"),
    Error_CODE_20(20, "添加车位信息失败"),
    Error_CODE_21(21, "删除车位信息失败"),
    Error_CODE_22(22, "更新车位经纬度失败"),
    Error_CODE_23(23, "获取用户车位信息失败"),
    Error_CODE_24(24, "获取停车场车位信息失败"),
    Error_CODE_25(25, "获取车位时间占用详情失败"),
    Error_CODE_26(26, "车位设备绑定信息异常"),
    Error_CODE_27(27, "预约下单失败"),
    Error_CODE_28(28, "取消订单状态失败"),
    Error_CODE_29(29, "获取订单详情失败"),
    Error_CODE_30(30, "更新订单支付状态失败"),
    Error_CODE_32(31, "更新订单延时信息失败"),
    Error_CODE_31(32, "获取订单二维码失败"),
    Error_CODE_33(33, "更新车位共享状态失败"),
    Error_CODE_34(34, "车位号绑定车牌失败"),
    Error_CODE_35(35, "更新订单延时信息失败"),
    Error_CODE_36(36, "获取选定位置周边的车位信息失败"),
    Error_CODE_37(37, "生成预付单失败"),
    Error_CODE_38(38, "获取用户微信OpenID失败"),
    Error_CODE_39(39, "请求的锁已经是打开状态"),
    Error_CODE_40(40, "请求的锁已经是关闭状态"),
    Error_CODE_41(41, "实际支付费用价格计算错误"),
    Error_CODE_42(42, "微信退款请求失败"),
    Error_CODE_43(43, "支付宝退款请求失败"),
    Error_CODE_44(44, "车位时间段已经占用"),
    Error_CODE_45(45, "没在预定时间内，不能开锁"),
    Error_CODE_46(46, "添加停车场失败"),
    Error_CODE_47(47, "没在预定时间内"),
    Error_CODE_48(48, "获取用户信息失败"),
    Error_CODE_49(49, "钱包的金额不够扣除此次费用"),
    Error_CODE_50(50, "更新钱包支付状态失败");

    int code;
    String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String getMsg(int code) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.code == code) {
                return errorCode.msg;
            }
        }
        return "";
    }

}
