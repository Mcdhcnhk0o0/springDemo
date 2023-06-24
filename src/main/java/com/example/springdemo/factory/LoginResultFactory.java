package com.example.springdemo.factory;

import com.example.springdemo.bean.LoginResult;
import com.example.springdemo.bean.Result;

public class LoginResultFactory {

    public static Result<LoginResult> loginFailed(int statusCode, String statusMessage) {
        Result<LoginResult> result = new Result<>();
        result.setData(new LoginResult());
        result.getData().setSuccess(false);
        result.getData().setStatusCode(statusCode);
        result.getData().setStatusMessage(statusMessage);
        return result;
    }

    public static Result<LoginResult> loginSuccess(int statusCode, String statusMessage) {
        Result<LoginResult> result = new Result<>();
        result.setData(new LoginResult());
        result.getData().setSuccess(true);
        result.getData().setStatusCode(statusCode);
        result.getData().setStatusMessage(statusMessage);
        return result;
    }

    static class DateTime {
        public int hour;
        public int minute;

        public int compareTo(DateTime another) {
            if (this.hour > another.hour) {
                return 1;
            } else if (this.hour == another.hour && this.minute > another.minute) {
                return 1;
            } else if (this.hour == another.hour && this.minute == another.minute) {
                return 0;
            } else {
                return -1;
            }
        }

    }

    private DateTime parseEventTime(String event) {
        DateTime time = new DateTime();
        if (event == null) {
            return null;
        }
        String[] eventTime = event.split(":");
        if (eventTime.length != 2) {
            return null;
        }
        time.hour = Integer.parseInt(eventTime[0]);
        time.minute = Integer.parseInt(eventTime[1]);
        return time;
    }

}
