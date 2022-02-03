package org.artisoft.api.controller.ModTask;

import org.artisoft.api.common.Response;
import org.artisoft.dal.dao.ModTask.CustomerDao;
import org.artisoft.domain.ModTask.Customer;
import org.artisoft.domain.Users;
import org.artisoft.domain.ValueLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "api/task/customer")
@CrossOrigin
public class CustomerController {

    @Autowired
    private CustomerDao customerDao;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Response<Customer>> getCustomerById(@PathVariable("id") int id){
        try {
            Customer customer = customerDao.getById(id);
            Response<Customer> response = new Response<>();
            HttpStatus httpStatus = (customer == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;

            return new ResponseEntity<>(response.generateResponse(httpStatus, customer), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<Customer>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Response<Customer>> delete(@PathVariable int id){
        try {
            boolean isSuccess = customerDao.delete(id);
            Response<Customer> response = new Response<>();
            HttpStatus httpStatus = isSuccess ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus, null), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<Customer>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/insupd", method = RequestMethod.POST)
    public ResponseEntity<Response<Customer>> InsertUpdate(@RequestBody Customer customer){
        try {

            Users nusers=new Users();
            nusers=customer.getUser();
            nusers.setModId(1);
            customer.setUser(nusers);
            boolean isSuccess;
            if (customer.getCustomerId() == 0)
                isSuccess = customerDao.insertNew(customer) > 0;
            else
                isSuccess = customerDao.updateNew(customer);
            if(isSuccess){
                customer = customerDao.getById(customer.getCustomerId());
            }
            Response<Customer> response = new Response<>();
            HttpStatus httpStatus = isSuccess ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus, customer), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<Customer>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/filterData", method = RequestMethod.POST)
    public ResponseEntity<Response<List<Customer>>> filterData(@RequestBody HashMap<String, String > filterData){

        try {
            List<Customer> customerList =  customerDao.filterDataByModId(filterData,1L);
            Response<List<Customer>> response = new Response<>();
            HttpStatus httpStatus = (customerList == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, customerList), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<List<Customer>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/ac", method = RequestMethod.GET)
    public ResponseEntity<Response<List<ValueLabel<Long, String>>>> getUserList() {
        try {
            List<ValueLabel<Long, String>> valueLabels = customerDao.filterCustomerList(1L);
            Response<List<ValueLabel<Long, String>>> response = new Response<>();
            HttpStatus httpStatus = (valueLabels == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, valueLabels), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<List<ValueLabel<Long, String>>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }



}
