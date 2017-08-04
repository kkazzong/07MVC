package com.model2.mvc.web.purchase;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;

@Controller
public class PurchaseController {

	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	@Value("#{commonProperties['pageSize']}")
	private int pageSize;
	
	@Value("#{commonProperties['pageUnit']}")
	private int pageUnit;
	
	public PurchaseController() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping("addPurchaseView.do")
	public String addPurchaseView(@RequestParam("prodNo") int prodNo,
												Model model) throws Exception {
		System.out.println("addPurchaseView.do");
//		Product product = new Product();
		
//		int prodNo = Integer.parseInt(request.getParameter("prodNo"));

//		System.out.println(":::prodNo : "+prodNo);
		
//		ProductService service = new ProductServiceImpl();
//		product = service.getProduct(prodNo);
		Product product = productService.getProduct(prodNo);
		
		System.out.println(product);
		
//		request.setAttribute("product", product);
		
		model.addAttribute("product", product);
//		return "forward:/purchase/addPurchaseView.jsp";
		
		return "/purchase/addPurchaseView.jsp";
	}

	@RequestMapping("addPurchase.do")
	public String addPurchase(@RequestParam("buyerId") String buyerId,
											   @RequestParam("prodNo") int prodNo,
											   @ModelAttribute("purchase") Purchase purchase,
											   HttpSession session,
											   Model model) throws Exception {
		
//		Purchase purchase = new Purchase();
		
//		String buyerId = request.getParameter("buyerId");
//		int prodNo = Integer.parseInt(request.getParameter("prodNo"));
		
//		System.out.println(":::buyerId : "+buyerId+" :::prodNo :"+prodNo);
		
//		HttpSession session = request.getSession();
//		if(session == null || session.getAttribute("user") == null) {
			
//			System.out.println("!!!! SESSION NULL !!!!");
//			return "redirect:/user/loginView.jsp";
			
//		} else {
			
//			User user = (User)session.getAttribute("user");
//			if(user.getUserId().equals(buyerId)) {
//				purchase.setBuyer(user);
//				System.out.println(user);
//			}
//		}
		
//		ProductService productService = new ProductServiceImpl();
//		Product product = productService.getProduct(prodNo);
		User user = (User)session.getAttribute("user");
		purchase.setBuyer(user);
		
		Product product = productService.getProduct(prodNo);
		purchase.setPurchaseProd(product);
//		int saleCount = Integer.parseInt(request.getParameter("saleCount"));
//		System.out.println("saleCount :::::"+saleCount);
//		int total = product.getProdStock() - saleCount;
//		if(total >= 0) {
//			product.setProdStock(product.getProdStock() - saleCount);
//			if(total == 0) {
//			purchase.setTranCode("1");
//			}
//		} else {
//			return "redirect:/purchase/addPurchase.jsp";
//		}
//		purchase.setPurchaseProd(product);
//		purchase.setPaymentOption(request.getParameter("paymentOption"));
//		purchase.setReceiverName(request.getParameter("receiverName"));
//		purchase.setReceiverPhone(request.getParameter("receiverPhone"));
//		purchase.setReceiverAddr(request.getParameter("receiverAddr"));
//		purchase.setReceiverRequest(request.getParameter("receiverRequest"));
//		purchase.setSaleCount(saleCount);
//		purchase.setReceiverDate(request.getParameter("receiverDate"));
		
//		PurchaseService service = new PurchaseServiceImpl();
//		purchase = service.addPurchase(purchase);
		purchase = purchaseService.addPurchase(purchase);
		System.out.println(purchase);
		
//		request.setAttribute("purchase", purchase);
		
		
//		return "forward:/purchase/addPurchase.jsp";
		return "/purchase/addPurchase.jsp";
	}

	@RequestMapping("listPurchase.do")
	public String listPurchase(@ModelAttribute("search") Search search,
											  HttpSession session,
											  Model model) throws Exception {
		
//		Search search = new Search();
		
		int currentPage = 1;
//		if(request.getParameter("currentPage")!= null) {
//			currentPage = Integer.parseInt(request.getParameter("currentPage"));
//		}
		if(search.getCurrentPage() == 0) {
			search.setCurrentPage(1);
		}
		
//		search.setCurrentPage(currentPage);
		
//		int pageUnit = Integer.parseInt(getServletContext().getInitParameter("pageUnit"));
//		int pageSize = Integer.parseInt(getServletContext().getInitParameter("pageSize"));
		search.setPageSize(pageSize);
		
		User user = (User)session.getAttribute("user");
		
//		String buyerId = null;
//		HttpSession session = request.getSession();
//		if(session == null || session.getAttribute("user") == null) {
//			return "redirect:/user/loginView.jsp";
//		} else {
//			User user = (User)session.getAttribute("user");
//			buyerId = user.getUserId();
//		}
//		System.out.println(":::buyerId = "+buyerId);
		
//		PurchaseService service = new PurchaseServiceImpl();
//		Map<String, Object> map = service.getPurchaseList(search, buyerId);
		Map<String, Object> map = purchaseService.getPurchaseList(search, user.getUserId());
		Page resultPage = new Page(currentPage, ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
//		request.setAttribute("list", map.get("purchaseList"));
//		request.setAttribute("search", search);
//		request.setAttribute("resultPage", resultPage);
		model.addAttribute("list", map.get("purchaseList"));
		model.addAttribute("resultPage", resultPage);
		
//		System.out.println("////////////////////////[ListPurchaseAction]///////////////////////\n");
		
//		return "forward:/purchase/listPurchase.jsp";
		return "/purchase/listPurchase.jsp";
	}
	
	@RequestMapping("listSale.do")
	public String listSale(@ModelAttribute("search") Search search,
									Model model) throws Exception {
		
//		Search search = new Search();
		
		int currentPage = 1;
//		if(request.getParameter("currentPage") != null) {
//			currentPage = Integer.parseInt(request.getParameter("currentPage"));
//		}
		if(search.getCurrentPage() == 0) {
			search.setCurrentPage(1);
		}
		
//		search.setCurrentPage(currentPage);
//		search.setSearchCondition(request.getParameter("searchCondition"));
//		search.setSearchKeyword(request.getParameter("searchKeyword"));
		
//		int pageUnit = Integer.parseInt(getServletContext().getInitParameter("pageUnit"));
//		int pageSize = Integer.parseInt(getServletContext().getInitParameter("pageSize"));
		search.setPageSize(pageSize);
		System.out.println(search);
		
//		PurchaseService service = new PurchaseServiceImpl();
//		Map<String, Object> map = service.getSaleList(search);
		
		Map<String, Object> map = purchaseService.getSaleList(search);
		
		Page resultPage = new Page(currentPage, ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
//		System.out.println(resultPage);
//		
//		request.setAttribute("list", map.get("saleList"));
//		request.setAttribute("search", search);
//		request.setAttribute("resultPage", resultPage);

		model.addAttribute("list", map.get("saleList"));
		model.addAttribute("resultPage", resultPage);
		
//		return "forward:/purchase/listSale.jsp";
		return "/purchase/listSale.jsp";
	}
	
	@RequestMapping("updateTranCodeByProd.do")
	public String updateTranCodeByProd(@RequestParam("prodNo") int prodNo,
																@RequestParam("tranCode") String tranCode,
																Model model) throws Exception {

//		int prodNo = Integer.parseInt(request.getParameter("prodNo"));
//		String tranCode = request.getParameter("tranCode"); //2
		
//		PurchaseService service = new PurchaseServiceImpl();
//		Purchase purchase = service.getPurchase2(prodNo);
//		purchase.setTranCode(tranCode);
		
		Purchase purchase = purchaseService.getPurchaseByProdNo(prodNo);
		purchase.setTranCode(tranCode);
		
//		service.updateTranCode(purchase);
		purchaseService.updateTranCode(purchase);
		
//		System.out.println(purchase);
		
		
//		return "forward:/listProduct.do?menu=manage";
		return "/listProduct.do?menu=manage";
	}
	
	@RequestMapping("updateTranCode.do")
	public String updateTranCode(@RequestParam("tranNo") int tranNo,
													@RequestParam("tranCode") String tranCode,
													Model model) throws Exception {
//		int tranNo = Integer.parseInt(request.getParameter("tranNo"));
//		String tranCode = request.getParameter("tranCode"); // 3
		
//		PurchaseService service = new PurchaseServiceImpl();
//		Purchase purchase = service.getPurchase(tranNo);
		
		Purchase purchase = purchaseService.getPurchase(tranNo);
		
		purchase.setTranCode(tranCode);
		
//		service.updateTranCode(purchase);
		purchaseService.updateTranCode(purchase);
		
		
		return "forward:/listPurchase.do";
	}
	
	@RequestMapping("updatePurchaseView.do")
	public String updatePurchaseView(@RequestParam("tranNo") int tranNo,
															Model model) throws Exception {
		
//		HttpSession session = request.getSession();
//		if(session == null || session.getAttribute("user") == null) {
//			System.out.println("¼¼¼Ç ³Î~~~");
//			return "redirect:/user/loginView.jsp";
//		} 
		
//		int tranNo = Integer.parseInt(request.getParameter("tranNo"));
//		System.out.println("::tranNo : "+tranNo);
		
//		PurchaseService service = new PurchaseServiceImpl();
//		Purchase purchase = service.getPurchase(tranNo);
//		System.out.println(purchase);
		Purchase purchase = purchaseService.getPurchase(tranNo);
		
//		request.setAttribute("purchase", purchase);
		
		model.addAttribute("purchase", purchase);
		
//		return "forward:/purchase/updatePurchaseView.jsp";
		return "/purchase/updatePurchaseView.jsp";
	}
	
	@RequestMapping("updatePurchase.do")
	public String updatePurchase(@RequestParam("tranNo") int tranNo,
													@ModelAttribute("purchase") Purchase purchase,
													Model model) throws Exception {
		
//		int tranNo = Integer.parseInt(request.getParameter("tranNo"));
//		System.out.println(tranNo);
		
//		Purchase purchase = new Purchase();
//		purchase.setTranNo(tranNo);
//		purchase.setReceiverName(request.getParameter("receiverName"));
//		purchase.setReceiverPhone(request.getParameter("receiverPhone"));
//		purchase.setReceiverRequest(request.getParameter("receiverRequest"));
//		purchase.setReceiverAddr(request.getParameter("receiverAddr"));

//		purchase.setReceiverDate(request.getParameter("receiverDate"));
		
		
//		PurchaseService service = new PurchaseServiceImpl();
//		service.updatePurchase(purchase);
		purchaseService.updatePurchase(purchase);
		model.addAttribute("purchase", purchaseService.getPurchase(tranNo));
		
//		purchase = service.getPurchase(tranNo);
		
//		request.setAttribute("purchase", purchase);
		
		
		return "forward:/purchase/addPurchase.jsp";
	}
}
