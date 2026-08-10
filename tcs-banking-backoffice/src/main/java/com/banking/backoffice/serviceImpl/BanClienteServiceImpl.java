package com.banking.backoffice.serviceImpl;

import com.banking.core.infraestrutura.exceptions.GenericException;
import com.banking.backoffice.model.BanCliente;
import com.banking.backoffice.repository.BanClienteRepository;
import com.banking.backoffice.servicioInterface.BanClienteServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BanClienteServiceImpl implements BanClienteServiceInterface {

    @Autowired
    private BanClienteRepository banClienteRepository;

    @Override
    public BanCliente crearCliente(BanCliente cliente) throws GenericException {
        banClienteRepository.save(cliente);
        return cliente;
    }

    @Override
    public Optional<BanCliente> findByIdentificacion(String identificacion) {
        return banClienteRepository.findByIdentificacion(identificacion);
    }

    @Override
    public List<BanCliente> findAll() {
        return banClienteRepository.findAll();
    }

    @Override
    public void eliminarClientePorIdentificacion(String identificacion) {
        Optional<BanCliente> cliente = banClienteRepository.findByIdentificacion(identificacion.trim());
        cliente.ifPresent(banClienteRepository::delete);
    }

    @Override
    public BanCliente actualizarCliente(BanCliente cliente) {
        // save() hace update si el ID ya existe
        return banClienteRepository.save(cliente);
    }

}
