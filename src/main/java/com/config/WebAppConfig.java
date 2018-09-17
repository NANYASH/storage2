package com.config;

import com.Controller;
import com.dao.FileDAO;
import com.dao.StorageDAO;
import com.dao.daoImpl.FileDAOImpl;
import com.dao.daoImpl.StorageDAOImp;
import com.service.FileService;
import com.service.StorageService;
import com.service.serviceImpl.FileServiceImpl;
import com.service.serviceImpl.StorageServiceImpl;
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
        return new StorageDAOImp();
    }

    @Bean
    public FileDAO fileDAO(){
        return new FileDAOImpl();
    }

    @Bean
    public FileService fileService(FileDAOImpl fileDAO) {
        return new FileServiceImpl(fileDAO);
    }

    @Bean
    public StorageService storageService(StorageDAOImp storageDAO){
        return new StorageServiceImpl(storageDAO);
    }

    @Bean
    public Controller controller(StorageService storageService, FileService fileService){
        return new Controller(storageService,fileService);

    }
}
