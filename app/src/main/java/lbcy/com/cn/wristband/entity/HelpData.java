package lbcy.com.cn.wristband.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenjie on 2017/10/5.
 */

@Entity
public class HelpData {
    /**
     * id : 123
     * question :
     * answer :
     */

    @Id
    private Long mId;
    private int id;
    private String question;
    private String answer;
    @Generated(hash = 524029411)
    public HelpData(Long mId, int id, String question, String answer) {
        this.mId = mId;
        this.id = id;
        this.question = question;
        this.answer = answer;
    }
    @Generated(hash = 1428261047)
    public HelpData() {
    }
    public Long getMId() {
        return this.mId;
    }
    public void setMId(Long mId) {
        this.mId = mId;
    }
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getQuestion() {
        return this.question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public String getAnswer() {
        return this.answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
