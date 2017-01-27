/*
 * LoginService.java 3.01.2017
 */
package ua.kpi.atep.services;

/**
 *
 * @author Home
 */
public interface LoginService {
    AppModelState login(UserSession session, String email, String password);
}
