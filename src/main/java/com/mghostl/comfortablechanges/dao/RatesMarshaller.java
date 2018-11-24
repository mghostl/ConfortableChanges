package com.mghostl.comfortablechanges.dao;

import com.mghostl.comfortablechanges.clients.ExchangesClient;
import com.thoughtworks.xstream.XStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
public class RatesMarshaller {
    private static final Logger LOGGER = LoggerFactory.getLogger(RatesMarshaller.class);
    private final XStream xStream;

    private final ExchangesClient exchangesClient;

    @Autowired
    public RatesMarshaller(ExchangesClient exchangesClient) {
        xStream = new XStream();
        xStream.processAnnotations(new Class[]{Rates.class, Exchange.class, Item.class});
        this.exchangesClient = exchangesClient;
    }

    public Optional<Rates> fromXML(String file) {
        try {
            return fromXML(ResourceUtils.getFile(file));
        } catch (FileNotFoundException e) {
            LOGGER.error("Couldn't parse this file: {}", e);
        }
        return Optional.empty();
    }

    @Async
    public CompletableFuture<Optional<Rates>> fromXMLHTTPSURL(String urlStr) {
        try {
            URL url = new URL(urlStr);
            return CompletableFuture.completedFuture(exchangesClient.getExchanges(url)
                    .map(reader -> (Rates) xStream.fromXML(reader)));
        } catch (MalformedURLException e) {
            LOGGER.error("Couldn't create url {}: {}", urlStr, e);
        }
        return CompletableFuture.completedFuture(Optional.empty());
    }

    Optional<Rates> fromXML(URL url) {
        return Optional.of((Rates) xStream.fromXML(url));
    }

    private Optional<Rates> fromXML(File file) {
        return Optional.of((Rates) xStream.fromXML(file));
    }
}
