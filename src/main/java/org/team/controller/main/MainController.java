package org.team.controller.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.team.domain.product.ProductCriteria;
import org.team.domain.product.ProductVO;
import org.team.service.main.MainService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/*")
@AllArgsConstructor
@Log4j
public class MainController {
	
	private MainService service;

	@GetMapping("/main")
	public void list(Model model, HttpServletRequest request) {
		log.info("***main list method***");
		
		List<ProductVO> list = service.getList();
		List<ProductVO> rank = service.getRank();
		List<ProductVO> today = service.getToday();
		model.addAttribute("list", list);
		model.addAttribute("rank", rank);
		model.addAttribute("allCnt", service.getCnt());
		model.addAttribute("allToday", today);
		model.addAttribute("allTodayCnt", service.getTodayCnt());
		
		// 세션에 검색 TOP 5 값 넣어줌
		List<ProductCriteria> searchRank = service.getSearchRank();
		HttpSession session = request.getSession();
		session.setAttribute("searchRank", searchRank);
	}
	
	@GetMapping("/search")
	public void search(@ModelAttribute("cri") ProductCriteria cri, Model model) {
		log.info("***main search method***");
		
		service.insertKeyword(cri);
		List<ProductVO> list = service.getSearchList(cri);
		model.addAttribute("list", list);
	}
	
	@RequestMapping("/product/register")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void register() {
		
	}
	
}
