package com.upgrade.ftp;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.MessageFormat;

/**
 * Created by twl-thinkpad on 2015-04-20.
 */
@Service
public class FtpSenderImpl implements FtpSender  {

    /** Camel URI, format is ftp://user@host/fileName?password=secret&passiveMode=true */
    private static final String CAMEL_FTP_PATTERN = "{0}://{1}@{2}/{3}?password={4}&passiveMode={5}";

    private final ProducerTemplate producerTemplate;

    /**
     * Constructor
     * @param producerTemplate The producer template to be be used
     */
    @Autowired
    FtpSenderImpl(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    @Override
    public void sendFile(FtpProperties ftpProperties, File file) throws RuntimeException {
        String fileName = file.getName();
        producerTemplate.sendBodyAndHeader(createFtpUri(ftpProperties), file, Exchange.FILE_NAME, fileName);
    }

    /**
     * Creates a Camel FTP URI based on the provided FTP properties
     * @param ftpProperties The properties to be used
     */
    private String createFtpUri(FtpProperties ftpProperties) {
        return MessageFormat.format(CAMEL_FTP_PATTERN,
                ftpProperties.getProtocol(),
                ftpProperties.getUserName(),
                ftpProperties.getHost(),
                ftpProperties.getRemoteDirectory(),
                ftpProperties.getPassword(),
                ftpProperties.getPassiveMode());
    }
}
