/*
 * FrontController.java 4.01.2017
 */
package ua.kpi.atep.controller;

import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import ua.kpi.atep.services.*;

import static ua.kpi.atep.controller.WebSocketMediator.interactWithHttp;

//model states
import static ua.kpi.atep.services.AppModelState.LOGIN_FAILURE;
import static ua.kpi.atep.services.AppModelState.LOGIN_SUCCESS;
import static ua.kpi.atep.services.AppModelState.REGISTER_SUCCESS;
import static ua.kpi.atep.services.AppModelState.SIMULATION_NO_ASSIGNMENT;
import static ua.kpi.atep.services.AppModelState.SIMULATION_START;
import static ua.kpi.atep.services.AppModelState.UNATORIZED_ACCESS;

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

    @Value("${web.signup.message.userexists}")
    private String userExistsMessage;

    @Value("${web.login.message.invalidinfo}")
    private String invalidInfoMessage;

    @Value("${web.home.message.timedout}")
    private String timeOutMessage;

    @Value("${web.home.message.unautorized}")
    private String unatorizedMessage;

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
    private void initViewResolver() {
        //is initiazed on container initialization , no synchronization needed
        viewResolver = new EnumMap<>(AppModelState.class);
        
        viewResolver.put(SIMULATION_START, redirectTo(simulationPage));
        viewResolver.put(LOGIN_SUCCESS, redirectTo(homePage));
        viewResolver.put(LOGIN_FAILURE, redirectTo(loginPage));
        viewResolver.put(REGISTER_SUCCESS, redirectTo(loginPage));
        viewResolver.put(SIMULATION_START, redirectTo(simulationPage));
        viewResolver.put(UNATORIZED_ACCESS, redirectTo(homePage));
        viewResolver.put(SIMULATION_NO_ASSIGNMENT, redirectTo(errorPage));
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
                registerService.register(
                        userSession,
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
    
    
    @RequestMapping(value= "${web.action.history}")
    @ResponseBody
    public String getStory(HttpServletResponse response) {
        response.setHeader("Content-Disposition", "attachment;filename=values.csv");
        response.setContentType(simulationService.getUserActivityContentType());
        return simulationService.getUserActivity(userSession.getUser().getId());
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
