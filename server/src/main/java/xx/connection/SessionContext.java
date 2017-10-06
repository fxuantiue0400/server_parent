package xx.connection;

/**
 * Created by Administrator on 2017/8/23/023.
 * 客户端上下文信息
 */
public class SessionContext {
    private String userId;  //用户ID
    private String version; //客户端版本
    private String type;    //客户端类型
    private String deviceId;//设备ID
    private String loginTime;//登录时间

    public SessionContext(String userId, String version, String type, String deviceId, String loginTime) {
        this.userId = userId;
        this.version = version;
        this.type = type;
        this.deviceId = deviceId;
        this.loginTime = loginTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    @Override
    public String toString() {
        return "SessionContext{" +
                "userId='" + userId + '\'' +
                ", version='" + version + '\'' +
                ", type='" + type + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", loginTime='" + loginTime + '\'' +
                '}';
    }
}
