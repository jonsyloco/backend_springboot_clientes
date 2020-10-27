package com.co.spring.boot.backend.apirest.models.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

public interface ISubirFoto {

    public Resource cargarFoto(String nombreFoto) throws MalformedURLException;

    public Path copiarFoto(MultipartFile archivo) throws IOException;

    public boolean eliminar(String nombreFoto);

    public Path getPath(String nombreFoto, String extension);
}
