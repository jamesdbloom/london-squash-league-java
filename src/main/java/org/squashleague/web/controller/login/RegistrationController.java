package org.squashleague.web.controller.login;

import com.eaio.uuid.UUID;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.account.Role;
import org.squashleague.domain.account.User;
import org.squashleague.service.security.SpringSecurityUserContext;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Controller
public class RegistrationController {

    @Resource
    private UserDAO userDAO;
//    @Resource
//    private UserDetailsManager userDetailsManager;
    @Resource
    private SpringSecurityUserContext userContext;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerForm(Model uiModel) {
        uiModel.addAttribute("user", new User());
        uiModel.addAttribute("mobilePrivacyOptions", MobilePrivacy.enumToFormOptionMap());
        return "/page/user/register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@Valid User user, BindingResult bindingResult, Model uiModel) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("bindingResult", bindingResult);
            uiModel.addAttribute("user", user);
            uiModel.addAttribute("mobilePrivacyOptions", MobilePrivacy.enumToFormOptionMap());
            return "/page/user/register";
        }
        // add to DB
        userDAO.save(user.withRole(Role.ROLE_USER).withOneTimeToken(new UUID().toString()));
//        // add to Spring Security
//        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(Role.ROLE_USER.name());
//        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getEmail(), "password", authorities);
//        userDetailsManager.createUser(userDetails);
        // mark user as logged in
        userContext.setCurrentUser(user);
        return "redirect:/";
    }
}
