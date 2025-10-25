package com.spenditure.object;

public class SecurityQuestion {

    private int sid;
    private String securityQuestion;

    public SecurityQuestion(int sid, String securityQuestion) {
        this.sid = sid;
        this.securityQuestion = securityQuestion;
    }

    public int getSid() {
        return sid;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }


    public String toString() {
        return "sid: " + sid + ", SecurityQuestion: " + securityQuestion;
    }
}
