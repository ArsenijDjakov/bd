package it.academy.app.controllers;

import it.academy.app.exception.IncorrectDataException;
import it.academy.app.exception.ValidationException;
import it.academy.app.models.user.User;
import it.academy.app.services.UserService;
import it.academy.app.services.scraping.ScrapingService;
import it.academy.app.shared.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
public class ScrapingController {

    @Autowired
    UserService userService;

    @Autowired
    ScrapingService scrapingService;

    @PostMapping(value = "/scrape")
    @ResponseBody
    public String scrapeShops(Authentication authentication, @RequestParam @NotNull String categoryId) throws IncorrectDataException, IOException {
        User user = userService.getByUsername(authentication.getName());
        if (user.hasAdminRights()) {
            scrapingService.scrape(Integer.parseInt(categoryId));
            return "Category with id " + categoryId + " scraped successfully";
        }
        throw new ValidationException(ErrorMessages.noAdminRights);
    }

}
