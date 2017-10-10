package lbcy.com.cn.wristband.entity;

/**
 * Created by chenjie on 2017/10/4.
 */

public class BasicBodyBean {

    /**
     * code : 200
     * message : null
     * data : {"height":175,"weight":75}
     */

    private int code;
    private Object message;
    private BasicBodyData data;

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

    public BasicBodyData getData() {
        return data;
    }

    public void setData(BasicBodyData data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * height : 175
         * weight : 75
         */

        private int height;
        private int weight;

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }
    }
}
