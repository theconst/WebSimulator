/*
 * FrontController.java 4.01.2017
 */
package ua.kpi.atep.controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import ua.kpi.atep.services.*;

import static ua.kpi.atep.controller.WebSocketMediator.interactWithHttp;
import ua.kpi.atep.model.entity.Assignment;
import ua.kpi.atep.model.entity.Student;
import static ua.kpi.atep.services.AppModelState.ADMIN_LOGIN;
import static ua.kpi.atep.services.AppModelState.ASSIGNMENT_CREATION_SUCCESS;

//model states
import static ua.kpi.atep.services.AppModelState.LOGIN_FAILURE;
import static ua.kpi.atep.services.AppModelState.LOGIN_SUCCESS;
import static ua.kpi.atep.services.AppModelState.REGISTER_SUCCESS;
import static ua.kpi.atep.services.AppModelState.SIMULATION_NO_ASSIGNMENT;
import static ua.kpi.atep.services.AppModelState.SIMULATION_START;
import static ua.kpi.atep.services.AppModelState.UNATORIZED_ACCESS;
import ua.kpi.atep.services.serialization.SerializationException;
import ua.kpi.atep.services.serialization.Serializer;

/**
 * Front servlet that dispatches all http requests from the user
 *
 * @author Konstantin Kovalchuk
 */
@Controller
public class FrontController {

    /**
     * string used to redirect to static page
     */
    private static final String SPRING_REDIR = "redirect:";

    private static final String ATTACHMENT = "attachment;filename=values.csv";

    private static final String CONTENT_DISPOSITION_HEADER
            = "Content-Disposition";

    /*
     * URL's for the applicaton
     */
    @Value("${web.login}")
    private String loginPage;

    @Value("${web.signup}")
    private String signupPage;

    @Value("${web.simulation}")
    private String simulationPage;

    @Value("${web.error}")
    private String errorPage;

    @Value("${web.adminpanel}")
    private String adminpanelPage;

    @Value("${web.home}")
    private String homePage;

    @Value("${web.attr.message}")
    private String messageAttr;

    @Value("${web.signup.message.closemessage}")
    private String closeWebSocketOrDoSimulation;

    @Value("${web.admin.account}")
    private String adminLogin;

    @Value("${web.admin.password}")
    private String adminPassword;

    @Value("${web.param.login}")
    private String loginParam;

    @Value("${web.param.username}")
    private String usernameParam;

    @Value("${web.param.password}")
    private String passwordParam;

    @Value("${web.param.group}")
    private String groupParam;

    /**
     * Register implements registration logic
     */
    @Autowired
    private RegisterService registerService;

    /**
     * Implements login logic
     */
    @Autowired
    private LoginService loginService;

    @Autowired
    private SimulationService simulationService;

    @Autowired
    private AdministrationService administrationService;

    @Autowired
    private Serializer<List<Student>, String, String> studentListSerializer;

    @Autowired
    private Serializer<Assignment, Reader, Writer> assignmentSerializer;

    /*
     * Controller is sinleton
     * Sesssion scoped bean is proxied
     *
     * Responsible of a proxy to find the corresponding proxy
     *
     */
    /**
     * Stores user properties
     */
    @Autowired
    private UserSession userSession;

    private Map<AppModelState, String> viewResolver;

    /**
     * Initialize view resolver
     */
    @PostConstruct
    private void init() {
        //is initiazed on container initialization , no synchronization needed
        viewResolver = new EnumMap<>(AppModelState.class);

        viewResolver.put(SIMULATION_START, redirectTo(simulationPage));
        viewResolver.put(LOGIN_SUCCESS, redirectTo(homePage));
        viewResolver.put(ADMIN_LOGIN, redirectTo(adminpanelPage));
        viewResolver.put(LOGIN_FAILURE, redirectTo(loginPage));
        viewResolver.put(REGISTER_SUCCESS, redirectTo(loginPage));
        viewResolver.put(SIMULATION_START, redirectTo(simulationPage));
        viewResolver.put(UNATORIZED_ACCESS, redirectTo(homePage));
        viewResolver.put(SIMULATION_NO_ASSIGNMENT, redirectTo(errorPage));

        viewResolver.put(
                ASSIGNMENT_CREATION_SUCCESS, redirectTo(adminpanelPage));
        
        //initialize administration
        administrationService.initAdministration(adminLogin, adminPassword);     
    }

    /**
     * Redirects to the welcome page
     *
     * @return URL of the home page
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String welcome() {
        return redirectTo(homePage);
    }

    /**
     * Redirects to the signup page
     *
     * @return URL of the signup page
     */
    @RequestMapping(value = "${web.action.signup}")
    public String signup() {
        return redirectTo(signupPage);
    }

    /**
     * Redirects to the welcome page
     *
     * @return URL of the home page
     */
    @RequestMapping(value = "${web.action.login}", method = RequestMethod.GET)
    public String login() {
        return redirectTo(loginPage);
    }

    /**
     * Sign up the user
     *
     * @param request user request
     * @return login page if success, registration form otherwise
     */
    @RequestMapping(value = "${web.action.signup.submit}",
            method = RequestMethod.POST)
    public String signup(HttpServletRequest request) {
        return resolveView(
                registerService.register(userSession,
                        request.getParameter(usernameParam),
                        request.getParameter(loginParam),
                        request.getParameter(passwordParam),
                        request.getParameter(groupParam))
        );
    }

    /**
     * Login to WebSimulator
     *
     * @param request user request
     * @return home page in case of success, login page otherwise
     */
    @RequestMapping(value = "${web.action.login.submit}",
            method = RequestMethod.POST)
    public String login(HttpServletRequest request) {
        return resolveView(
                loginService.login(
                        userSession,
                        request.getParameter(loginParam),
                        request.getParameter(passwordParam)
                )
        );
    }

    /**
     * Starts simulation
     *
     * @param request user request
     * @return simulation page if user is properly logged in
     */
    @RequestMapping(value = "${web.action.simulate}")
    public String startSimulation(HttpServletRequest request) {
        AppModelState simulationStartResult
                = simulationService.initSimulation(userSession);

        /* 
         * the simplest workaround to pass attr's 
         * is to put attributes on session 
         */
        if (simulationStartResult == SIMULATION_START) {
            interactWithHttp(request, userSession);
        }

        return resolveView(simulationStartResult);
    }

    
    //this method behaves awkwardly when user presses the save button too
    //quickly
    @RequestMapping(value = "${web.action.history}")
    @ResponseBody
    public String getStory(HttpServletResponse response) {
        String userActivity = simulationService.getUserActivity(userSession.getUser().getId());

        //if hypothetically user does simulation for the first time and closes
        //early
        if (userActivity == null) {
            
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            return closeWebSocketOrDoSimulation;
        }
        response.setHeader(CONTENT_DISPOSITION_HEADER, ATTACHMENT);
        response.setContentType(simulationService.getUserActivityContentType());
        return userActivity;
    }

    @RequestMapping(value = "${web.action.uploadmodel}")
    public String uploadModel(
            @RequestParam("${web.param.modelfile}") MultipartFile file,
            HttpServletResponse response)
            throws IOException, SerializationException {
        Assignment assignment = assignmentSerializer.deserialize(
                new InputStreamReader(file.getInputStream(), "UTF-8"));
        AppModelState result = administrationService.createAssignment(
                userSession, assignment);

        return resolveView(result);
    }

    @RequestMapping(value = "${web.action.assignvariant}")
    @ResponseBody
    public void assignVariant(
            @RequestParam("${web.param.variant}") int variant,
            @RequestParam("${web.param.username}") String login,
            HttpServletResponse response) {
        if (administrationService.setAssigmnent(userSession, variant, login)
                == ASSIGNMENT_CREATION_SUCCESS) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "${web.action.liststudents}",
            method = RequestMethod.GET,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public String getStudentList() throws SerializationException {
        List<Student> students
                = administrationService.getStudentList(userSession);
        return studentListSerializer.serialize(students);
    }

    /**
     * Primitive exception handling
     *
     * @param ex exception
     * @return error page
     */
    @ExceptionHandler(Exception.class)
    public String handleError(Exception ex) {
        Logger.getLogger(FrontController.class.getName(), ex.getMessage());

        return redirectTo(errorPage);
    }

    /**
     * Forms redirect command from URL
     *
     * @param url URL of the page
     * @return redirection command
     */
    private String redirectTo(String url) {
        return SPRING_REDIR + url;
    }

    /**
     * Resolve view
     *
     * @param result
     * @return
     */
    private String resolveView(AppModelState result) {
        return resolveView(result, homePage);
    }

    /**
     * Resolve view
     *
     * @param result result of the app model operation
     * @param def default page to return
     * @return
     */
    private String resolveView(AppModelState result, String def) {
        return viewResolver.getOrDefault(result, redirectTo(def));
    }
    
}
