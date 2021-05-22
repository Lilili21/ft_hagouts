package school21.gfoote.ft_hangouts.model;

public class SmsInfo {
    private int id;
    private String phone;
    private String message;
    private String who;

    public SmsInfo(int id, String phone, String msg, String who) {
        this.id = id;
        this.phone = phone;
        this.message = msg;
        this.who = who;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public static String SmsDbFormat(){
        return "Sms (phone, message, who)";
    }
}
