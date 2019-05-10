package com.example.appointment;

public class DataApptClass {
    String apptDay,apptType,apptStartTime,apptEndTime,apptId,requesterDeviceToken,responderDeviceToken,requestedTime,
            requestedId,requesterName,responderId,responderName,messagebody,userType;


    public DataApptClass() {
    }

    public DataApptClass(String apptDay, String apptType, String apptStartTime, String apptEndTime,
                         String apptId, String requesterDeviceToken, String responderDeviceToken,
                         String requestedTime, String requestedId, String requesterName,
                         String responderId, String responderName, String messagebody,String userType) {
        this.apptDay = apptDay;
        this.apptType = apptType;
        this.apptStartTime = apptStartTime;
        this.apptEndTime = apptEndTime;
        this.apptId = apptId;
        this.requesterDeviceToken = requesterDeviceToken;
        this.responderDeviceToken = responderDeviceToken;
        this.requestedTime = requestedTime;
        this.requestedId = requestedId;
        this.requesterName = requesterName;
        this.responderId = responderId;
        this.responderName = responderName;
        this.messagebody = messagebody;
        this.userType= userType;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getMessagebody() {
        return messagebody;
    }

    public void setMessagebody(String messagebody) {
        this.messagebody = messagebody;
    }

    public String getResponderDeviceToken() {
        return responderDeviceToken;
    }

    public void setResponderDeviceToken(String responderDeviceToken) {
        this.responderDeviceToken = responderDeviceToken;
    }

    public String getApptDay() {
        return apptDay;
    }

    public void setApptDay(String apptDay) {
        this.apptDay = apptDay;
    }

    public String getApptType() {
        return apptType;
    }

    public void setApptType(String apptType) {
        this.apptType = apptType;
    }

    public String getApptStartTime() {
        return apptStartTime;
    }

    public void setApptStartTime(String apptStartTime) {
        this.apptStartTime = apptStartTime;
    }

    public String getApptEndTime() {
        return apptEndTime;
    }

    public void setApptEndTime(String apptEndTime) {
        this.apptEndTime = apptEndTime;
    }

    public String getApptId() {
        return apptId;
    }

    public void setApptId(String apptId) {
        this.apptId = apptId;
    }

    public String getRequesterDeviceToken() {
        return requesterDeviceToken;
    }

    public void setRequesterDeviceToken(String requesterDeviceToken) {
        this.requesterDeviceToken = requesterDeviceToken;
    }

    public String getRequestedTime() {
        return requestedTime;
    }

    public void setRequestedTime(String requestedTime) {
        this.requestedTime = requestedTime;
    }

    public String getRequestedId() {
        return requestedId;
    }

    public void setRequestedId(String requestedId) {
        this.requestedId = requestedId;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getResponderId() {
        return responderId;
    }

    public void setResponderId(String responderId) {
        this.responderId = responderId;
    }

    public String getResponderName() {
        return responderName;
    }

    public void setResponderName(String responderName) {
        this.responderName = responderName;
    }
}
