package com.github.juhnowski.MeterVT;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.server.handler.ExceptionHandlingWebHandler;

@Component
public class AppStartupRunner implements ApplicationRunner {
    private static final Logger LOG =
      LoggerFactory.getLogger(AppStartupRunner.class);

    @Value("${xls.original}")
    private String strOriginal;

    @Value("${xls.copied.prefix}")
    private String strCopiedPrefix;
    @Value("${xls.copied.postfix}")
    private String strCopiedPostfix;

    @Value("${idx.from}")
    private Integer idxFrom;

    @Value("${idx.to}")
    private Integer idxTo;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOG.info("Application started with option names : {}", 
          args.getOptionNames());
        LOG.info("Generate test files");

        File original = new File(strOriginal);

        IntStream stream = IntStream.range(idxFrom, idxTo); 
        stream.forEach(i ->{
            try{
                File copied = new File(strCopiedPrefix+i+strCopiedPostfix);
                try (
                    InputStream in = new BufferedInputStream(
                        new FileInputStream(original));
                    OutputStream out = new BufferedOutputStream(
                        new FileOutputStream(copied))) {
                
                        byte[] buffer = new byte[1024];
                        int lengthRead;
                        while ((lengthRead = in.read(buffer)) > 0) {
                            out.write(buffer, 0, lengthRead);
                            out.flush();
                        }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }); 
        
        LOG.info("Generation test files - finished");


    }


}