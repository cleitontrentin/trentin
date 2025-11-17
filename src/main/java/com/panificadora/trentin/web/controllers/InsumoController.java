package com.panificadora.trentin.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.panificadora.trentin.domain.Insumo;
import com.panificadora.trentin.service.InsumoService;

@Controller
@RequestMapping("/insumos")
public class InsumoController {
	
	@Autowired
	private InsumoService service;

	@GetMapping("/cadastrar")
	public String cadastrar(Insumo insumo) {
		return "/insumo/cadastro";
	}
	
	@GetMapping("/listar")
	public String listar(ModelMap model) {
		model.addAttribute("insumos", service.buscarTodos());
		return "/insumo/lista"; 
	}
	
	@PostMapping("/salvar")
	public String salvar(Insumo insumo, RedirectAttributes attr) {
		service.salvar(insumo);
		attr.addFlashAttribute("success", "Insumo inserido com sucesso.");
		return "redirect:/insumos/cadastrar";
	}
	
	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("insumo", service.buscarPorId(id));
		return "/insumo/cadastro";
	}
	
	@PostMapping("/editar")
	public String editar(Insumo insumo, RedirectAttributes attr) {
		service.editar(insumo);
		attr.addFlashAttribute("success", "Insumo editado com sucesso.");
		return "redirect:/insumos/cadastrar";
	}
	
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
		service.excluir(id);
		attr.addFlashAttribute("success", "Insumo removido com sucesso.");
		return "redirect:/insumos/listar";
	}
	
}
