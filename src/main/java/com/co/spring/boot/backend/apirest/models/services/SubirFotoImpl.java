package com.co.spring.boot.backend.apirest.models.services;


import com.co.spring.boot.backend.apirest.models.Constantes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Constants;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class SubirFotoImpl implements ISubirFoto {
    private Logger log = LoggerFactory.getLogger(SubirFotoImpl.class);

    @Override
    public Resource cargarFoto(String nombreFoto) throws MalformedURLException {
        String extension = ".jpg";
        Path rutaArchivo = getPath(nombreFoto, extension);
        log.error("RUTA DEL RECURSO-Z" + rutaArchivo.toString());

        Resource recurso = new UrlResource(rutaArchivo.toUri());


        if (!recurso.exists() && !recurso.isReadable()) {
            rutaArchivo = Paths.get("src/main/resorces/static/imagenes").resolve("no-usuario.svg").toAbsolutePath();
            recurso = new UrlResource(rutaArchivo.toUri());

            log.error("Error, no se pudo cargar la imagen: " + nombreFoto);
            return recurso;
        }
        return recurso;
    }

    @Override
    public Path copiarFoto(MultipartFile archivo) throws IOException {
        /*String nombreArchivo = archivo.getOriginalFilename();*/
        String nombreArchivo = UUID.randomUUID().toString();
        String extencion = ".jpg";
        Path rutaArchivo = getPath(nombreArchivo,extencion);
        Files.copy(archivo.getInputStream(), rutaArchivo);
        return rutaArchivo;

    }

    @Override
    public boolean eliminar(String nombreFoto) {
        return false;
    }

    @Override
    public Path getPath(String nombreFoto, String extension) {
        return Paths.get(Constantes.DIRECTORIO_IMAGENES).resolve(nombreFoto + extension).toAbsolutePath();
    }
}
