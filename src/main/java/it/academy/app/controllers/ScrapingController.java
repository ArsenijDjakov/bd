package it.academy.app.controllers;

import it.academy.app.exception.IncorrectDataException;
import it.academy.app.exception.ValidationException;
import it.academy.app.models.scraping.ScrapeRequest;
import it.academy.app.models.user.User;
import it.academy.app.services.UserService;
import it.academy.app.services.scraping.ScrapingService;
import it.academy.app.validators.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
public class ScrapingController {

    @Autowired
    UserService userService;

    @Autowired
    ScrapingService scrapingService;

    @PostMapping(value = "/scrape")
    @ResponseBody
    public String scrapeShops(Authentication authentication, @RequestBody @Valid ScrapeRequest scrapeRequest) throws IncorrectDataException, IOException {
        User user = userService.findByUsername(authentication.getName());
        if (user.hasAdminRights()) {
            scrapingService.scrape(scrapeRequest.getShopId(), scrapeRequest.getCategoryId());
            return "Shop with id " + scrapeRequest.getShopId() + " scraped successfully";
        }
        throw new ValidationException(ErrorMessages.noAdminRights);
    }

}
