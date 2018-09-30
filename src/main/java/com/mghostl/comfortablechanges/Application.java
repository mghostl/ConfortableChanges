package com.mghostl.comfortablechanges;

import com.mghostl.comfortablechanges.dao.RatesMarshaller;
import com.mghostl.comfortablechanges.db.RatesStorage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan
public class Application {

    public static void main(String[] args) throws FileNotFoundException {
       ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
       RatesMarshaller ratesMarshaller = (RatesMarshaller) context.getBean("ratesMarshaller");
       RatesStorage simpleRatesStorage = (RatesStorage) context.getBean("simpleRatesStorage");
       ratesMarshaller.fromXML(ResourceUtils.getFile("classpath:rates.xml"))
               .ifPresent(result -> simpleRatesStorage.addRates("Example", result));


    }

}
