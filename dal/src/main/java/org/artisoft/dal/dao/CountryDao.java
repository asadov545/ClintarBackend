package org.artisoft.dal.dao;

import org.artisoft.domain.ValueLabel;

import java.util.List;

public interface CountryDao {

    List<ValueLabel<Long, String>> filterCountryList(String label);
}
