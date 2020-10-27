package com.co.spring.boot.backend.apirest.models.controllers;

import com.co.spring.boot.backend.apirest.models.entity.Cliente;
import com.co.spring.boot.backend.apirest.models.services.IClienteService;
import com.co.spring.boot.backend.apirest.models.services.ISubirFoto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@RestController
@RequestMapping(path = "api/")
@CrossOrigin(origins = {"http://localhost:4200"})
public class ClienteRestController {

    @Autowired
    IClienteService service; /*estan interfaz se va a la clase ClienteServiceImpl que la está implementando y se trae esos metodos para aca*/

    @Autowired
    ISubirFoto subirFotoService; /*estan interfaz se va a la clase SubirFotoImpl que la está implementando y se trae esos metodos para aca*/

    private Logger log = LoggerFactory.getLogger(ClienteRestController.class);

    @GetMapping(path = "/clientes")
    public ResponseEntity index() {
        return new ResponseEntity<>(service.findAllClient(), HttpStatus.OK);
    }


    @GetMapping(path = "/clientes/pagina/{page}")
    public ResponseEntity index(@PathVariable Integer page) {
        Pageable paginacion = PageRequest.of(page, 4);
        return new ResponseEntity<>(service.findAllClient(paginacion), HttpStatus.OK);
    }

    /**
     * forma 2
     */
    /*@GetMapping(path = "/buscarCliente")
    public ResponseEntity buscarCliente(@RequestBody Cliente cl){
        System.out.println("id:"+cl.getId());
        return new ResponseEntity<>(service.buscarClientePorId(Long.valueOf(cl.getId())), HttpStatus.OK) ;
        //return new ResponseEntity<>(id, HttpStatus.OK) ;
    }*/
    @GetMapping(path = "/buscarCliente/{id}")
    public ResponseEntity buscarCliente(@PathVariable Long id) {
        System.out.println("id:" + id);
        Cliente cl = null;
        Map<String, String> response = new HashMap<>();

        try {
            cl = service.buscarClientePorId(id);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error Accediendo a la base de datos ");
            response.put("resultado", "Error: " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }


        if (cl == null) { // no se encontro el cliente
            response.put("mensaje", "El cliente con Id: " + id + ", no fue encontrado");
            response.put("resultado", "Error");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(cl, HttpStatus.OK);
        //return new ResponseEntity<>(id, HttpStatus.OK) ;
    }


    @PostMapping(path = "/guardarCliente")
    public ResponseEntity guardarCliente(@Valid @RequestBody Cliente cl, BindingResult errores) { //@Valid BindingResult errores -> manejan los errores desde la entidad
        Cliente cliente = null;
        cl.setFechaCreacion(new Date());
        Map<String, Object> response = new HashMap<>();

        /*Errores de la entidad, tratados con el BindingResult errores*/
        List erroresEntidad = new ArrayList();
        erroresEntidad = buscarErroresEntidad(errores);

        if (erroresEntidad.size() > 0) {
            response.put("mensaje", "Error de validaciones");
            response.put("resultado", erroresEntidad);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        /*-------------------------------------------------------------------*/


        try {
            cliente = service.guardarCliente(cl);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error insertando el cliente en base de datos");
            response.put("resultado", "Error: " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

        }

        response.put("mensaje", "Cliente guardado con éxito");
        response.put("resultado", cliente);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    /**
     * Metodo para buscar errores cuando, cuando se envia un request desde el front,
     * son solo validaciones a nivel de entity
     *
     * @param errores
     * @return
     */
    private List buscarErroresEntidad(BindingResult errores) {
        List listaErrores = new ArrayList();
        if (errores.hasErrors()) {
            for (FieldError e : errores.getFieldErrors()) {
                listaErrores.add("el campo '" + e.getField() + "' " + e.getDefaultMessage());
            }
        }
        return listaErrores;
    }


    @DeleteMapping(path = "/eliminarCliente/{id}")
    public ResponseEntity eliminarCliente(@PathVariable Long id) {
        Map<String, String> response = new HashMap();
        try {
            service.eliminarCliente(id);
        } catch (DataAccessException e) {
            response.put("resultado", "Error: " + e.getMostSpecificCause().getMessage());
            response.put("mensaje", "Error borrando el cliente en base de datos");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);

    }


    /**
     * forma 2
     */
    /*@PostMapping(path = "/eliminarCliente")
    public ResponseEntity eliminarCliente(@RequestBody Cliente cl){
        Map<String, String> response = new HashMap();
        response.put("resultado","ok");
        response.put("mensaje","Cliente "+cl.getId()+" borrado con éxito");
        service.eliminarCliente(cl.getId());
        return new ResponseEntity<>(response, HttpStatus.OK) ;

    }*/
    @PutMapping(path = "/actualizarCliente")
    public ResponseEntity actualizarCliente(@Valid @RequestBody Cliente cl, BindingResult errores) {

        Cliente cl2 = new Cliente();
        Cliente cl3 = null;
        Map<String, Object> response = new HashMap();

        ResponseEntity rr = buscarCliente(cl.getId());

        if (rr.getStatusCodeValue() == 200) { //si sale bien, asignamos el resultado a la variable
            cl2 = (Cliente) rr.getBody();
        } else {
            return rr;
        }

        /*Errores de la entidad, tratados con el BindingResult errores*/
        List erroresEntidad = new ArrayList();
        erroresEntidad = buscarErroresEntidad(errores);

        if (erroresEntidad.size() > 0) {
            response.put("mensaje", "Error de validaciones");
            response.put("resultado", erroresEntidad);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        /*-------------------------------------------------------------------*/


        cl2.setEmail(cl.getEmail());
        cl2.setNombre(cl.getNombre());
        cl2.setApellido(cl.getApellido());
        cl2.setFechaNacimiento(cl.getFechaNacimiento());
        cl2.setEdad(cl.getEdad());

        try {
            cl3 = service.guardarCliente(cl2);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error Actualizando el cliente en base de datos");
            response.put("resultado", "Error: " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "Cliente Actualizado con éxito");
        response.put("resultado", cl3);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    /**
     * Metodo para guardar foto en BD y en carpeta de proyecto
     *
     * @param archivo
     * @param id
     * @return
     */
    @PostMapping(path = "/subirFoto")
    public ResponseEntity subirImagen(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id) {
        Map<String, Object> response = new HashMap();
        response.put("mensaje", "Cliente Actualizado con éxito");
        response.put("resultado", "Imagen subida con exito");
        Cliente cl = new Cliente();


        try {
            cl = service.buscarClientePorId(id);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error Accediendo a la base de datos ");
            response.put("resultado", "Error: " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }


        if (cl == null) { // no se encontro el cliente
            response.put("mensaje", "El cliente con Id: " + id + ", no fue encontrado");
            response.put("resultado", "Error");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        if (!archivo.isEmpty()) { // si no es vacio
            /*String nombreArchivo = archivo.getOriginalFilename();*/

            Path rutaArchivo = null;
            try {
                rutaArchivo = subirFotoService.copiarFoto(archivo);
            } catch (IOException e) {
                response.put("mensaje", "Error subiendo la imagen: ");
                response.put("resultado", "Error: " + e.getCause().getMessage());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            cl.setRutaFoto(rutaArchivo.toAbsolutePath().toString());

            service.guardarCliente(cl);
            response.put("mensaje", "Imagen subida correctamente");
            response.put("resultado", cl);
            return new ResponseEntity<>(response, HttpStatus.OK);

        }
        response.put("mensaje", "Error recibiendo el archivo");
        response.put("resultado", "El archivo no pudo ser procesado");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

    }

    /**
     * mostrando foto en el frontEnd
     */
    @GetMapping("/obtenerFoto/{nombreFoto}")
    public ResponseEntity obtenerFoto(@PathVariable String nombreFoto) {

        Resource recurso = null;
        try {
            recurso = subirFotoService.cargarFoto(nombreFoto);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpHeaders cabecera = new HttpHeaders();
        cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachmnet; filename=" + recurso.getFilename());
        return new ResponseEntity(recurso, cabecera, HttpStatus.OK);
    }

}
