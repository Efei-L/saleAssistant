package com.ltx.saleassistant.enums;

/**
 * 状态码
 * @author lishuai
 */
public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200,"操作成功"),
    // 登录
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIST(501,"用户名已存在"),
     PHONENUMBER_EXIST(502,"手机号已存在"), EMAIL_EXIST(503, "邮箱已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    LOGIN_ERROR(505,"用户名或密码错误"),
    CONTENT_NOT_NULL(506,"内容不能为空"),
    FILE_TYPE_ERROR(507,"文件类型错误，请上传Excel文件"),
    USERNAME_NOT_NULL(508,"用户名不能为空"),
    NICKNAME_NOT_NULL(509,"昵称不能为空"),
    PASSWORD_NOT_NULL(510,"密码不能为空"),
    EMAIL_NOT_NULL(511,"邮箱不能为空"),

    VERTEX_EXIST(521,"节点已存在"),
    RELATION_EXIST(522,"关系已存在"),
    FILE_NOT_NULL(550,"请上传文件"),
    FILE_PARSING_ERROR(551,"文件解析错误"),
    INTEGER_VALUE_ERROR(560,"数值错误，需要大于0的整数"),
    DATABASE_CONNECTION_FAILED(570,"数据库连接失败"),
    DATABASE_IMPORT_FAILED(571,"表格拉取出错，请检查表格是否存在"),
    ;
    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
