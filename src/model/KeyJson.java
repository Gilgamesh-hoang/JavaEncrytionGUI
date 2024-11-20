package model;

import java.io.Serializable;

public class KeyJson implements Serializable {
    private String algorithm;
    private String key;
    private String mode;
    private String padding;

    public KeyJson(String algorithm, String key, String mode, String padding) {
        this.algorithm = algorithm;
        this.key = key;
        this.mode = mode;
        this.padding = padding;
    }

    public KeyJson() {
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getPadding() {
        return padding;
    }

    public void setPadding(String padding) {
        this.padding = padding;
    }
}
