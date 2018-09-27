package com.mghostl.comfortablechanges.dao;

import com.thoughtworks.xstream.XStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Optional;

@Component
public class RatesMarshaller {
    private static final Logger LOGGER = LoggerFactory.getLogger(RatesMarshaller.class);
    private final XStream xStream;

    public RatesMarshaller() {
        xStream = new XStream();
        xStream.processAnnotations(Rates.class);
        xStream.processAnnotations(Item.class);
    }

    public String toXML(Rates rates) {
        return xStream.toXML(rates);
    }

    public Optional<Rates> fromXML(String file) {
        try {
            return fromXML(ResourceUtils.getFile(file));
        } catch (FileNotFoundException e) {
            LOGGER.error("Couldn't parse this file: {}", e);
        }
        return Optional.empty();
    }

    public Optional<Rates> fromXML(File file) {
        return Optional.of((Rates) xStream.fromXML(file));
    }

    public Optional<Rates> fromXML(URL url) {
        return Optional.of((Rates) xStream.fromXML(url));
    }

}
