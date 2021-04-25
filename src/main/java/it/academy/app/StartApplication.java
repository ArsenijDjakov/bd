package it.academy.app;

import it.academy.app.services.scraping.ScrapingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.*;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
public class StartApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(StartApplication.class, args);
        ScrapingService scrapingService = applicationContext.getBean(ScrapingService.class);
        Date date=new Date();
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            public void run(){
                try {
                    System.out.println("Trying to scrape shops... " + new Date());
                    scrapingService.scrape(3);
                    System.out.println("Scraping finished... " + new Date());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        },date, 24*60*60*1000);
    }
}

