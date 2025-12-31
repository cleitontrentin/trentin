package com.panificadora.trentin.web.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.panificadora.trentin.entities.Receita;
import com.panificadora.trentin.entities.UnidadeDeMedida;
import com.panificadora.trentin.service.InsumoService;
import com.panificadora.trentin.service.ReceitaService;

@Controller
@RequestMapping("/receitas")
public class ReceitaController {

	@Autowired
	private ReceitaService receitaService;

	@Autowired
	private InsumoService insumoService;

	@GetMapping("/cadastrar")
	public String cadastrar(Receita receita, ModelMap model) {
	    receita.setItens(new ArrayList<>());
	    model.addAttribute("insumosDisponiveis", insumoService.buscarTodos());
	    return "receita/cadastro";
	}


	@GetMapping("/listar")
	public String listar(ModelMap model) {
		model.addAttribute("receitas", receitaService.buscarTodos());
		return "receita/lista";
	}

	@PostMapping("/salvar")
	public String salvar(Receita receita, RedirectAttributes attr) {
	    receita.getItens().forEach(item -> item.setReceita(receita));
	    receitaService.salvar(receita);
	    attr.addFlashAttribute("success", "Receita inserida com sucesso.");
	    return "redirect:/receitas/cadastrar";
	}

	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {

	    model.addAttribute("receita", receitaService.buscarPorIdComItens(id));
	    model.addAttribute("insumosDisponiveis", insumoService.buscarTodos());

	    return "receita/cadastro";
	}


	@PostMapping("/editar")
	public String editar(Receita receita, RedirectAttributes attr) {
	    receita.getItens().forEach(item -> item.setReceita(receita));
	    receitaService.editar(receita);
	    attr.addFlashAttribute("success", "Registro atualizado com sucesso.");
	    return "redirect:/receitas/cadastrar";
	}

	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, ModelMap model) {
		receitaService.excluir(id);
		model.addAttribute("success", "Departamento excluído com sucesso.");
		return listar(model);
	}

	@ModelAttribute("UnidadeDeMedidas")
	public UnidadeDeMedida[] getUnidadeDeMedidas() {
		return UnidadeDeMedida.values();
	}



}
