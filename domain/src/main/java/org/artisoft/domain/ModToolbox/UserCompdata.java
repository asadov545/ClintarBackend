package org.artisoft.domain.ModToolbox;

import org.artisoft.domain.ValueLabel;

import java.util.List;

public class UserCompdata {
    private List<ValueLabel<Long, String>> mainProjectList;
    private List<ValueLabel<Long, String>> countryList;

    public List<ValueLabel<Long, String>> getMainProjectList() {
        return mainProjectList;
    }

    public void setMainProjectList(List<ValueLabel<Long, String>> mainProjectList) {
        this.mainProjectList = mainProjectList;
    }

    public List<ValueLabel<Long, String>> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<ValueLabel<Long, String>> countryList) {
        this.countryList = countryList;
    }
}
