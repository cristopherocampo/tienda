
package com.tienda.service;

import com.tienda.domain.Categoria;
import com.tienda.repository.CategoriaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaService {
    
    //Permite crear una única instancia de CategoriaRepository, y la crea automáticamente
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Transactional(readOnly = true)
    public List<Categoria> getCategorias(boolean activo) {
        if (activo) { //Sólo activos...
            return categoriaRepository.findByActivoTrue();
        }
        return categoriaRepository.findAll();
    }
}
