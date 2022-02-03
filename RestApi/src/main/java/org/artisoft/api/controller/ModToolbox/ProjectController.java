package org.artisoft.api.controller.ModToolbox;

import org.artisoft.api.common.Common;
import org.artisoft.api.common.Response;
import org.artisoft.dal.dao.ModTask.CustomerDao;
import org.artisoft.dal.dao.ModToolbox.ProjectListDao;
import org.artisoft.dal.dao.ModToolbox.ToolboxUserDao;
import org.artisoft.dal.repository.ModToolbox.ProjectRepository;
import org.artisoft.domain.ModToolbox.*;
import org.artisoft.domain.ModToolbox.project.MainProjectList;
import org.artisoft.domain.ModToolbox.project.ToolboxFormSearcInfo;
import org.artisoft.domain.ModToolbox.template.TemplateDetails;
import org.artisoft.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "api/tb/project")
@CrossOrigin
public class ProjectController {
    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private ToolboxUserDao toolboxUserDao;

    @Autowired
    private ProjectListDao projectListDao;

    @RequestMapping(value = "/createProject", method = RequestMethod.POST)
    public ResponseEntity<Response<Object>> CreateProject(@RequestBody CreateProject project) {
        try {
            Response<Object> response = new Response<>();
            Object result = projectRepo.createProject(project);
            HttpStatus httpStatus = HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, result), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new Response<Object>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/getTmplCats", method = RequestMethod.GET)
    public ResponseEntity<Response<List<TemplateCategory>>> GetTemplateCategories() {
        List<TemplateCategory> resultList = projectRepo.getTemplateCategories();

        try {
            Response<List<TemplateCategory>> response = new Response<>();
            HttpStatus httpStatus = (resultList == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, resultList), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(new Response<List<TemplateCategory>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/getCatTmpls/{catId}", method = RequestMethod.GET)
    public ResponseEntity<Response<List<ProjectTemplate>>> GetCategoryTemplates(@PathVariable("catId") long catId) {
        List<ProjectTemplate> resultList = projectRepo.getCategoryTemplates(catId);

        try {
            Response<List<ProjectTemplate>> response = new Response<>();
            HttpStatus httpStatus = (resultList == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, resultList), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(new Response<List<ProjectTemplate>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/getCreateProjectInitials", method = RequestMethod.GET)
    public ResponseEntity<Response<CreateProjectInitials>> GetCreateProjectInitialData() {

        User userInfo = Common.GetLoggedUser();
        long userId = userInfo == null ? 0 : userInfo.getUserId();
        CreateProjectInitials initials = new CreateProjectInitials();
        initials.setTemplateCategories(projectRepo.getTemplateCategories());
        initials.setCustomerList(customerDao.filterDataByModId(new HashMap<String, String>(), 2l));
        initials.setProjectNo(projectRepo.getNextPojectNo());
        initials.setProjectList(projectRepo.getMainProjectList(userId));
        initials.setUserList(toolboxUserDao.getUserList());
        initials.setTemplateList(projectRepo.getTemplateList());
        try {
            Response<CreateProjectInitials> response = new Response<>();
            HttpStatus httpStatus = (initials == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, initials), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(new Response<CreateProjectInitials>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/getTemplateInfo/{templateId}", method = RequestMethod.GET)
    public ResponseEntity<Response<TemplateDetails>> GetTemplateInfo(@PathVariable("templateId") long templateId) {
        TemplateDetails templateDetails = new TemplateDetails();
        templateDetails.setCheckList(projectRepo.getTemplateCategories(templateId));
        templateDetails.setCmplQuestions(projectRepo.getTemplateQuestions(templateId));
        templateDetails.setCheckListOptions(projectRepo.getTemplateCheckListOptions(templateId));
        templateDetails.setTasks(projectRepo.getTemplateTasks(templateId));
        templateDetails.setDescription(projectRepo.getByTempDescription(templateId));

        try {
            Response<TemplateDetails> response = new Response<>();
            HttpStatus httpStatus = (templateDetails == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, templateDetails), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(new Response<TemplateDetails>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/getProjectList", method = RequestMethod.POST)
    public ResponseEntity<Response<List<ProjectList>>> getProjectList(@RequestBody HashMap<String, String > filterData){

        try {
            User userInfo = Common.GetLoggedUser();
            long userId = userInfo == null ? 0 : userInfo.getUserId();

            List<ProjectList> projectLists =  projectListDao.getProjectList(filterData,userId);
            Response<List<ProjectList>> response = new Response<>();
            HttpStatus httpStatus = (projectLists == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, projectLists), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<List<ProjectList>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/deleteProject/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Response<Object>> deleteProject(@PathVariable long id){
        try {
            Response<Object> response = new Response<>();
            HttpStatus httpStatus = HttpStatus.OK;
            User userInfo = Common.GetLoggedUser();
            long userId = userInfo == null ? 0 : userInfo.getUserId();
            Object result = projectRepo.deleteProject(id, userId);
            return new ResponseEntity<>(response.generateResponse(httpStatus, null), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<Object>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/getCreateFormProjects", method = RequestMethod.GET)
    public ResponseEntity<Response<List<MainProjectList>>> GetCreateFormProjects() {

        User userInfo = Common.GetLoggedUser();
        long userId = userInfo == null ? 0 : userInfo.getUserId();
        List<MainProjectList> resultList = projectRepo.getMainProjectList(userId);

        try {
            Response<List<MainProjectList>> response = new Response<>();
            HttpStatus httpStatus = (resultList == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            return new ResponseEntity<>(response.generateResponse(httpStatus, resultList), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(new Response<List<MainProjectList>>().generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
        }
    }

}
