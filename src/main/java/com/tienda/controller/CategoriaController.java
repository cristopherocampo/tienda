package com.tienda.controller;

import com.tienda.domain.Categoria;
import com.tienda.service.CategoriaService;
import java.util.Locale;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/listado")
    public String inicio(Model model) {
        var categorias = categoriaService.getCategorias(false);
        model.addAttribute("categorias", categorias);
        model.addAttribute("totalCategorias", categorias.size());
        
        // CORRECCIÓN 1: Objeto necesario para que el fragmento 'agregar' no de error 500
        model.addAttribute("categoria", new Categoria()); 
        
        return "categoria/listado";
    }

    @PostMapping("/guardar")
    public String guardar(Categoria categoria,
            @RequestParam("imagenFile") MultipartFile imagenFile,
            RedirectAttributes redirectAttributes) {
        
        categoriaService.save(categoria, imagenFile);
        
        redirectAttributes.addFlashAttribute("todoOk", 
            messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault()));
            
        return "redirect:/categoria/listado";
    }

    // CORRECCIÓN 2: Cambiado de @PostMapping a @GetMapping y usando @PathVariable
    // Esto debe coincidir con: th:href="@{/categoria/eliminar/}+${c.idCategoria}"
    @GetMapping("/eliminar/{idCategoria}")
    public String eliminar(@PathVariable("idCategoria") Integer idCategoria, 
                           RedirectAttributes redirectAttributes) {

        String titulo = "todoOk";
        String detalle = "mensaje.eliminado";

        try {
            categoriaService.delete(idCategoria);
        } catch (Exception e) {
            titulo = "error";
            detalle = "categoria.error02"; // Error de datos asociados
        }

        redirectAttributes.addFlashAttribute(titulo, 
            messageSource.getMessage(detalle, null, Locale.getDefault()));

        return "redirect:/categoria/listado";
    }

    @GetMapping("/modificar/{idCategoria}")
    public String modificar(@PathVariable("idCategoria") Integer idCategoria, 
                            Model model, 
                            RedirectAttributes redirectAttributes) {

        Optional<Categoria> categoriaOpt = categoriaService.getCategoria(idCategoria);

        if (categoriaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Categoría no encontrada");
            return "redirect:/categoria/listado";
        }

        model.addAttribute("categoria", categoriaOpt.get());
        
        // CORRECCIÓN 3: Quitado el / inicial para evitar errores de resolución
        return "categoria/modifica"; 
    }
}