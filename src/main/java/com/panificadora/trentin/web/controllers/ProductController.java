package com.panificadora.trentin.web.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.panificadora.trentin.entities.Categoria;
import com.panificadora.trentin.entities.Produto;
import com.panificadora.trentin.service.CategoriaService;
import com.panificadora.trentin.service.ProdutoService;

@Controller
@RequestMapping("/produtos")
public class ProductController {

	@Autowired
	private ProdutoService productService;
	
	@Autowired
	private CategoriaService categoriaService;

	@GetMapping("/cadastrar")
	public String cadastrar(Produto produto) { 	
		return "produto/cadastro";
	}

	@GetMapping("/listar")
	public String listar(ModelMap model) {
		model.addAttribute("produtos", productService.buscarTodos());
		return "/produto/lista";
	}

	@PostMapping("/salvar")
	public String salvar(Produto produto, RedirectAttributes attr) {
	    productService.salvar(produto);
	    attr.addFlashAttribute("success", "Produto inserido com sucesso.");
	    return "redirect:/produtos/cadastrar";
	}

	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("produto", productService.buscarPorId(id));
		return "produto/cadastro";
	}

	@PostMapping("/editar")
	public String editar(Produto produto, RedirectAttributes attr) {
		productService.editar(produto);
		attr.addFlashAttribute("success", "Registro atualizado com sucesso.");
		return "redirect:/produtos/cadastrar";
	}
	
	@ModelAttribute("categorias")
	public List<Categoria> listaDeCategorias() {
		return categoriaService.buscarTodos();
	}

	 
}
