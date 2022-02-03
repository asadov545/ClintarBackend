package org.artisoft.api.controller.ModToolbox;

import org.artisoft.api.common.Common;
import org.artisoft.api.common.Response;
import org.artisoft.dal.repository.ModToolbox.AdminPanelRepository;
import org.artisoft.dal.repository.ModToolbox.ProjectTemplateListRepository;
import org.artisoft.domain.ModToolbox.ProjectTemplateList;
import org.artisoft.domain.ModToolbox.cpanel.CreateTemplate;
import org.artisoft.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "api/tb/cpanel")
@CrossOrigin
public class AdminPanelController {
    @Autowired
    private AdminPanelRepository adminRepo;
    @Autowired
    private ProjectTemplateListRepository projectTemplateListRepository;

    @RequestMapping(value = "/createTemplate", method = RequestMethod.POST)
    public ResponseEntity<Response<Object>> CreateTemplate(@RequestBody CreateTemplate template) {
        try {
            Response<Object> response = new Response<>();
            Object result = adminRepo.createTemplate(template);
            HttpStatus httpStatus = HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, result), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new Response<Object>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/deleteTemplate/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Response<Object>> deleteTemplate(@PathVariable long id) {
        try {
            Response<Object> response = new Response<>();
            HttpStatus httpStatus = HttpStatus.OK;
            User userInfo = Common.GetLoggedUser();
            long userId = userInfo == null ? 0 : userInfo.getUserId();
           // Object result = adminRepo.deleteTemplate(id, userId);
            boolean result = adminRepo.deleteTemplate2(id);
            return new ResponseEntity<>(response.generateResponse(httpStatus, null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<Object>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/getProjectTemplateList", method = RequestMethod.POST)
    public ResponseEntity<Response<List<ProjectTemplateList>>> getProjectTemplateList(@RequestBody HashMap<String, String> filterData) {

        try {
            User userInfo = Common.GetLoggedUser();
            long userId = userInfo == null ? 0 : userInfo.getUserId();
            List<ProjectTemplateList> projectTemplateLists = projectTemplateListRepository.getProjectTemplateList(userId);
            Response<List<ProjectTemplateList>> response = new Response<>();
            HttpStatus httpStatus = (projectTemplateLists == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, projectTemplateLists), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<List<ProjectTemplateList>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }
}
