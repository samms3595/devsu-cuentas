package com.devsu.cuentas.feign;

import com.devsu.cuentas.model.Cliente;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "servicio-cliente", url = "http://servicio-clientes:9091/cliente")
public interface UsuarioFeignClient {

    @GetMapping("/client/{id}")
    Cliente obtenerClientePorClienteId(@PathVariable Long id);

    @GetMapping("/addAccount/{idCliente}/{idCuenta}")
    Map<String, Object> añadirCuenta(@PathVariable Long idCliente, @PathVariable Long idCuenta);

    @GetMapping("/delAccount/{idCliente}/{idCuenta}")
    Map<String, Object> eliminarCuenta(@PathVariable Long idCliente, @PathVariable Long idCuenta);

    }
