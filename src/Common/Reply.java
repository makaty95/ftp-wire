package Common;

public class Reply {
    private String code;
    private String content;

    public Reply(String code) {this.code = code;}

    public Reply(String code, String content) {
        this.code = code;
        this.content = content;
    }

    public void setCode(String code){this.code = code;}
    public void setContent(String content){this.content = content;}

    public String getCode(){return this.code;}
    public String getContent(){return this.content;}

}
