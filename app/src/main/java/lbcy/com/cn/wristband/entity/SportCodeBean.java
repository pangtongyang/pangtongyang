package lbcy.com.cn.wristband.entity;

/**
 * Created by chenjie on 2017/10/27.
 */

public class SportCodeBean {

    /**
     * code : 200
     * message : null
     * data : {"sport_code":2017110022}
     */

    private int code;
    private Object message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * sport_code : 2017110022
         */

        private int sport_code;

        public int getSport_code() {
            return sport_code;
        }

        public void setSport_code(int sport_code) {
            this.sport_code = sport_code;
        }
    }
}
