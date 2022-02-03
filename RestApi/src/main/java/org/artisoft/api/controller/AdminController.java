package org.artisoft.api.controller;


import org.artisoft.api.common.Common;
import org.artisoft.api.common.Response;
import org.artisoft.dal.dao.admin.PrivilegesDao;
import org.artisoft.dal.dao.admin.RoleDao;
import org.artisoft.dal.dao.admin.RolePrivsDao;
import org.artisoft.dal.dao.admin.UserRoleDao;
import org.artisoft.domain.ModTask.permissions.*;
import org.artisoft.domain.User;
import org.artisoft.domain.ValueLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.artisoft.api.common.ResponseUtil;

import java.util.List;

@RestController
@RequestMapping(value = "/api/admin")
@CrossOrigin
public class AdminController {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PrivilegesDao privilegesDao;

    @Autowired
    private RolePrivsDao rolePrivsDao;
    @Autowired
    private UserRoleDao userRolesDao;
    /////////////// roles

    @RequestMapping(value = "/role/{id}", method = RequestMethod.GET)
    public ResponseEntity<Response<Roles>> getRoleById(@PathVariable("id") int id) {
        try {
            Roles user = roleDao.getById(id);
            Response<Roles> response = new Response<>();
            return new ResponseEntity<>(response.generateResponse(HttpStatus.OK, user), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<Roles>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/roles/", method = RequestMethod.GET)
    public ResponseEntity<Response<List<Roles>>> getAllRoles() {
        try {
            List<Roles> rolesList = roleDao.getAll();
            Response<List<Roles>> response = new Response<>();
            return new ResponseEntity<>(response.generateResponse(HttpStatus.OK, rolesList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<List<Roles>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/role/insupd", method = RequestMethod.POST)
    public ResponseEntity<Response<Roles>> InsertUpdate(@RequestBody Roles role) {
        try {
            boolean isSuccess;
            if (role.getRoleId() == 0)
                isSuccess = roleDao.insert(role) > 0;
            else
                isSuccess = roleDao.update(role);
            if (isSuccess) {
                role = roleDao.getById(role.getRoleId());
            }
            Response<Roles> response = new Response<>();
            HttpStatus httpStatus = isSuccess ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus, role), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<Roles>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/role/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Response<Boolean>> delete(@PathVariable("id") int id) {
        return ResponseUtil.getResponseResponseEntity(roleDao.delete(id));
    }

    /////////////// PRIVILEGES
    @RequestMapping(value = "/priv/{id}", method = RequestMethod.GET)
    public ResponseEntity<Response<Privileges>> getPrivById(@PathVariable("id") int id) {
        try {
            Privileges privilege = privilegesDao.getById(id);
            Response<Privileges> response = new Response<>();
            return new ResponseEntity<>(response.generateResponse(HttpStatus.OK, privilege), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<Privileges>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/privs/", method = RequestMethod.GET)
    public ResponseEntity<Response<List<Privileges>>> getAllPrivileges() {
        try {
            List<Privileges> privilegesList = privilegesDao.getAll();
            Response<List<Privileges>> response = new Response<>();
            return new ResponseEntity<>(response.generateResponse(HttpStatus.OK, privilegesList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<List<Privileges>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/priv/insupd", method = RequestMethod.POST)
    public ResponseEntity<Response<Privileges>> InsertUpdatePrivilege(@RequestBody Privileges privilege) {
        try {
            boolean isSuccess;
            if (privilege.getPrivId() == 0)
                isSuccess = privilegesDao.insert(privilege) > 0;
            else
                isSuccess = privilegesDao.update(privilege);
            if (isSuccess) {
                privilege = privilegesDao.getById(privilege.getPrivId());
            }
            Response<Privileges> response = new Response<>();
            HttpStatus httpStatus = isSuccess ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus, privilege), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<Privileges>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/priv/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Response<Boolean>> deletePrivilege(@PathVariable("id") int id) {
        return ResponseUtil.getResponseResponseEntity(privilegesDao.delete(id));
    }

    /////////////////////////////////////// ROLE PRIVS ////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @RequestMapping(value = "/rolepriv/{id}", method = RequestMethod.GET)
    public ResponseEntity<Response<RolePrivs>> getRolePrivById(@PathVariable("id") int id) {
        try {
            RolePrivs rolePrivs = rolePrivsDao.getById(id);
            Response<RolePrivs> response = new Response<>();
            return new ResponseEntity<>(response.generateResponse(HttpStatus.OK, rolePrivs), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<RolePrivs>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/roleprivs/", method = RequestMethod.GET)
    public ResponseEntity<Response<List<RolePrivs>>> getAllRolePrivs() {
        try {
            List<RolePrivs> rolePrivsList = rolePrivsDao.getAll();
            Response<List<RolePrivs>> response = new Response<>();
            return new ResponseEntity<>(response.generateResponse(HttpStatus.OK, rolePrivsList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<List<RolePrivs>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/rolepriv/insupd", method = RequestMethod.POST)
    public ResponseEntity<Response<RolePrivs>> InsertUpdatePrivilege(@RequestBody RolePrivs rolePrivs) {
        try {
            boolean isSuccess;
            if (rolePrivs.getRolePrivId() == 0)
                isSuccess = rolePrivsDao.insert(rolePrivs) > 0;
            else
                isSuccess = rolePrivsDao.update(rolePrivs);
            if (isSuccess) {
                rolePrivs = rolePrivsDao.getById(rolePrivs.getRolePrivId());
            }
            Response<RolePrivs> response = new Response<>();
            HttpStatus httpStatus = isSuccess ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus, rolePrivs), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<RolePrivs>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/rolepriv/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Response<Boolean>> deleteRolePrivs(@PathVariable("id") int id) {
        return ResponseUtil.getResponseResponseEntity(rolePrivsDao.delete(id));
    }

    @RequestMapping(value = "/rolepriv/list/{val}", method = RequestMethod.GET)
    public ResponseEntity<Response<List<ValueLabel<Long, String>>>> getRolePrivList(@PathVariable String val, @RequestParam String label) {
        try {
            Response<List<ValueLabel<Long, String>>> response = new Response<>();
            if (!val.equalsIgnoreCase("role") && !val.equalsIgnoreCase("priv")) {
                return new ResponseEntity<>(response.generateResponse(HttpStatus.BAD_REQUEST, null), HttpStatus.OK);
            }
            List<ValueLabel<Long, String>> rolePrivsList = val.equalsIgnoreCase("role")
                    ? rolePrivsDao.getRoles(label)
                    : rolePrivsDao.getPrivs(label);


            return new ResponseEntity<>(response.generateResponse(HttpStatus.OK, rolePrivsList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<List<ValueLabel<Long, String>>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }


    /////////////////////////////////////// user ROLE  ////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @RequestMapping(value = "/userrole/{id}", method = RequestMethod.GET)
    public ResponseEntity<Response<UserRoles>> getUserRoleById(@PathVariable("id") int id) {
        try {
            UserRoles userRoles = userRolesDao.getById(id);
            Response<UserRoles> response = new Response<>();
            return new ResponseEntity<>(response.generateResponse(HttpStatus.OK, userRoles), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<UserRoles>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/userrole/", method = RequestMethod.GET)
    public ResponseEntity<Response<List<UserRoles>>> getAllUserRoles() {
        try {
            List<UserRoles> userRolesList = userRolesDao.getAll();
            Response<List<UserRoles>> response = new Response<>();
            return new ResponseEntity<>(response.generateResponse(HttpStatus.OK, userRolesList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<List<UserRoles>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/userrole/insupd", method = RequestMethod.POST)
    public ResponseEntity<Response<UserRoles>> InsertUpdateUserRoles(@RequestBody UserRoles userRoles) {
        try {
            boolean isSuccess;
            if (userRoles.getUserRoleId() == 0)
                isSuccess = userRolesDao.insert(userRoles) > 0;
            else
                isSuccess = userRolesDao.update(userRoles);
            if (isSuccess) {
                userRoles = userRolesDao.getById(userRoles.getUserRoleId());
            }
            Response<UserRoles> response = new Response<>();
            HttpStatus httpStatus = isSuccess ? HttpStatus.OK : HttpStatus.NOT_MODIFIED;
            return new ResponseEntity<>(response.generateResponse(httpStatus, userRoles), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<UserRoles>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/userrole/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Response<Boolean>> deleteUserRole(@PathVariable("id") int id) {
        return ResponseUtil.getResponseResponseEntity(userRolesDao.delete(id));

    }

    @RequestMapping(value = "/userrole/userlist", method = RequestMethod.GET)
    public ResponseEntity<Response<List<ValueLabel<Long, String>>>> getUserList(@RequestParam String label) {
        try {
            Response<List<ValueLabel<Long, String>>> response = new Response<>();

            List<ValueLabel<Long, String>>  userList = userRolesDao.getUsers(label);


            return new ResponseEntity<>(response.generateResponse(HttpStatus.OK, userList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<List<ValueLabel<Long, String>>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/priv/privlistbyuser", method = RequestMethod.GET)
    public ResponseEntity<Response<List<Long>>> getUserList() {
        try {

            User userInfo = Common.GetLoggedUser();
            long userId = userInfo == null ? 0 : userInfo.getUserId();
            Response<List<Long>> response = new Response<>();
            List<Long>  userList = privilegesDao.getPriviligiesList(userId);
            return new ResponseEntity<>(response.generateResponse(HttpStatus.OK, userList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<List<Long>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/rolepriv/compData", method = RequestMethod.GET)
    public ResponseEntity<Response<RolePrivsCompdata>> getCompData(){

        try {
            RolePrivsCompdata rolePrivsCompdata =  rolePrivsDao.getRolePrivsCompData();
            Response<RolePrivsCompdata> response = new Response<>();
            HttpStatus httpStatus = (rolePrivsCompdata == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, rolePrivsCompdata), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<RolePrivsCompdata>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/userrole/compData", method = RequestMethod.GET)
    public ResponseEntity<Response<UserRolesCompData>> getUserRoleCompData(){

        try {
            UserRolesCompData userRolesCompData =  userRolesDao.getUserRolesCompData();
            Response<UserRolesCompData> response = new Response<>();
            HttpStatus httpStatus = (userRolesCompData == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, userRolesCompData), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<UserRolesCompData>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }


}
