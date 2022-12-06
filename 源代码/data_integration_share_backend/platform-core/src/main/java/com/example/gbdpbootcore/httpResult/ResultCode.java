package com.example.gbdpbootcore.httpResult;

/**
 * 响应码枚举，参考HTTP状态码的语义
 */
public enum ResultCode {

    /*** 接口调用成功*/
    SUCCESS(20000, "接口调用成功,调用结果请参考具体的业务返回参数"),
    /*** 服务不可用*/
    SERVICE_UNAVAILABLE(50300, "服务不可用"),
    /*** 授权权限不足（token相关）*/
    INSUFFICIENT_AUTHORIZATION(40100, "授权权限不足"),
    /*** 接口不存在 */
    NOT_FOUND(40002,"接口不存在"),
    /*
    50008: Illegal token; 50012: Other clients logged in; 50014: Token expired;
    * */
    ILLEGAL_TOKEN(50008, "非法token"),
    OTHER_CLIENTS_LOGGED_IN(50012, "其他客户端登陆"),
    TOKEN_EXPIRED(50014, "token过期"),
    /*** 失败 */
    FAIL(40000, "失败"),
    /*** 缺少必选参数*/
    MISSING_PARAMETERS(40001, "缺少必选参数"),
    /*** 非法的参数*/
    ILLEGAL_PARAMETER(40002, "非法的参数"),
    /*** 业务处理失败*/
    API_PROCESSING_FAILED(40004, "业务处理失败"),
    /*** 未认证（签名错误） */
    UNAUTHORIZED(40005, "签名错误"),
    /*** 权限不足*/
    INSUFFICIENT_PERMISSIONS(40006, "权限不足"),
    /*** 用户名不存在或者密码错误*/
    USERNAME_OR_PASSWORD_ERROR(40007, "用户名不存在或者密码错误"),
    /*** 重复数据*/
    DUPLICATE_KEY(40008, "重复数据"),
    /*** 有子节点，无法删除*/
    HAVE_CHILD_CANT_DELETE(40009, "该记录有子节点，无法删除！"),
    /*** 记录不存在*/
    RECORD_NOT_EXIST(40010, "记录不存在，请检查输入参数！"),
    /*** 简单密码*/
    SIMPLE_PASSWORD(40011, "密码过于简单!密码长度需大于8，至少1个字母，1个数字和1个特殊字符"),
    /*** 不能删除当前数据*/
    CAN_NOT_DELETE(40012, "不能删除当前数据！"),
    ;

    //SUCCESS(200),//成功
    //
    //INTERNAL_SERVER_ERROR(500);//服务器内部错误

    protected int code;
    private String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResultCode valueOf(int code) {
        for (ResultCode resultCode : ResultCode.values()) {
            if (resultCode.code() == code) {
                return resultCode;
            }
        }
        return API_PROCESSING_FAILED;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }
}
