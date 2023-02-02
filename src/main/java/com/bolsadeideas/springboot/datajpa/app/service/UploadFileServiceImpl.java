package com.bolsadeideas.springboot.datajpa.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UploadFileServiceImpl implements IUploadFileService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final static String UPLOADS_FOLDER = "uploads";

    @Override
    public Resource load(String filename) throws MalformedURLException {
        Path pathFoto = getPath(filename);
        log.info("PATHFOTO: " + pathFoto);
        Resource recurso = null;

        recurso = new UrlResource(pathFoto.toUri());
        if (!recurso.exists() || !recurso.isReadable()) {
            throw new RuntimeException("Error: No se puede cargar la imagen: " + pathFoto.toString());
        }

        return recurso;
    }

    @Override
    public String copy(MultipartFile file) throws IOException {
        String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path rootPath = getPath(uniqueFileName);

        log.info("ROOTPATH: " + rootPath);
        Files.copy(file.getInputStream(), rootPath);

        return uniqueFileName;
    }

    @Override
    public boolean delete(String fileName) {
        Path rootPath = getPath(fileName);
        File archivo = rootPath.toFile();
        if(archivo.exists() && archivo.canRead()){
            if(archivo.delete()){
                log.info("Metodo:guardar: Foto " + fileName + " eliminada con éxito");
                return true;
            }
        }
        return false;
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(Paths.get(UPLOADS_FOLDER).toFile());
    }

    @Override
    public void init() throws IOException {
        Files.createDirectories(Paths.get(UPLOADS_FOLDER));
    }

    public Path getPath(String fileName) {
        return Paths.get(UPLOADS_FOLDER).resolve(fileName).toAbsolutePath();
    }
}
