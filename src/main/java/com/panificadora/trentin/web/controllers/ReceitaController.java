package com.panificadora.trentin.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.panificadora.trentin.domain.Receita;
import com.panificadora.trentin.domain.UF;
import com.panificadora.trentin.domain.UnidadeDeMedida;
import com.panificadora.trentin.service.ReceitaService;

@Controller
@RequestMapping("/receitas")
public class ReceitaController {

	@Autowired
	private ReceitaService receitaService;

	@GetMapping("/cadastrar")
	public String cadastrar(Receita receita) {
		return "/receita/cadastro";
	}

	@GetMapping("/listar")
	public String listar(ModelMap model) {
		model.addAttribute("receitas", receitaService.buscarTodos());
		return "receita/lista";
	}

	@PostMapping("/salvar")
	public String salvar(Receita receita, RedirectAttributes attr) {
		receitaService.salvar(receita);
		attr.addFlashAttribute("success", "Receita inserido com sucesso.");
		return "redirect:/receitas/cadastrar";
	}

	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("receita", receitaService.buscarPorId(id));
		return "receita/cadastro";
	}

	@PostMapping("/editar")
	public String editar(Receita receita, RedirectAttributes attr) {
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


