package org.artisoft.api.controller;

import org.artisoft.api.common.Response;
import org.artisoft.dal.dao.CountryDao;
import org.artisoft.domain.ValueLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/country")
@CrossOrigin
public class CountryController {
@Autowired
private CountryDao countryDao;

    @RequestMapping(value = "/ac", method = RequestMethod.GET)
    public ResponseEntity<Response<List<ValueLabel<Long, String>>>> getCountryList(@RequestParam("label") String label){
        try {
            List<ValueLabel<Long, String>> valueLabels =  countryDao.filterCountryList(label);
            Response<List<ValueLabel<Long, String>>> response = new Response<>();
            HttpStatus httpStatus = (valueLabels == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, valueLabels), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<List<ValueLabel<Long, String>>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

}
