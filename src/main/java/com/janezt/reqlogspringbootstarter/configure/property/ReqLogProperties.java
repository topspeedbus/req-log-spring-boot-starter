package com.janezt.reqlogspringbootstarter.configure.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author chan
 * @version 0.0.1
 * @time 2021/7/26 - 19:49
 */
@ConfigurationProperties("component.req.logs")
public class ReqLogProperties {
    private boolean enabled = true;
    private int slowReqElapsed = 3000;

    public ReqLogProperties() {
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public int getSlowReqElapsed() {
        return this.slowReqElapsed;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setSlowReqElapsed(int slowReqElapsed) {
        this.slowReqElapsed = slowReqElapsed;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ReqLogProperties)) {
            return false;
        } else {
            ReqLogProperties other = (ReqLogProperties)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.isEnabled() != other.isEnabled()) {
                return false;
            } else {
                return this.getSlowReqElapsed() == other.getSlowReqElapsed();
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof ReqLogProperties;
    }

//    public int hashCode() {
//        int PRIME = true;
//        int result = 1;
//        int result = result * 59 + (this.isEnabled() ? 79 : 97);
//        result = result * 59 + this.getSlowReqElapsed();
//        return result;
//    }

    public String toString() {
        return "ReqLogProperties(enabled=" + this.isEnabled() + ", slowReqElapsed=" + this.getSlowReqElapsed() + ")";
    }
}