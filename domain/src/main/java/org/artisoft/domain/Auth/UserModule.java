package org.artisoft.domain.Auth;

public class UserModule {
    private int modId;
    private String modTitle;
    private String modKey;
    private String modDescription;

    public int getModId() {
        return modId;
    }

    public void setModId(int modId) {
        this.modId = modId;
    }

    public String getModTitle() {
        return modTitle;
    }

    public void setModTitle(String modTitle) {
        this.modTitle = modTitle;
    }

    public String getModKey() {
        return modKey;
    }

    public void setModKey(String modKey) {
        this.modKey = modKey;
    }

    public String getModDescription() {
        return modDescription;
    }

    public void setModDescription(String modDescription) {
        this.modDescription = modDescription;
    }
}
