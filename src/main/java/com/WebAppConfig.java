package com;

import com.dao.FileDAO;
import com.dao.StorageDAO;
import com.service.impl.FileServiceImpl;
import com.service.impl.StorageServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@EnableWebMvc
@Configuration
@ComponentScan("com")
public class WebAppConfig {

    @Bean
    public StorageDAO storageDAO(){
        return new StorageDAO();
    }

    @Bean
    public FileDAO fileDAO(){
        return new FileDAO();
    }

    @Bean
    public FileServiceImpl fileService(FileDAO fileDAO) {
        return new FileServiceImpl(fileDAO);
    }

    @Bean
    public StorageServiceImpl storageService(StorageDAO storageDAO){
        return new StorageServiceImpl(storageDAO);
    }

    @Bean
    public Controller controller(StorageServiceImpl storageService,FileServiceImpl fileService){
        return new Controller(storageService,fileService);

    }
}
