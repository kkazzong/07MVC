package com.model2.mvc.web.product;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.CookieGenerator;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;

@Controller
@RequestMapping("/product/*") //  /product/~~~ 모든거
public class ProductController {
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	@Value("#{commonProperties['pageUnit']}")
	private int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	private int pageSize;
	
	public ProductController() {
		System.out.println(">> productController default constructor <<");
	}
	
//	@RequestMapping("addProduct.do")
	@RequestMapping(value="addProduct", method=RequestMethod.POST)
	public ModelAndView addProduct(@ModelAttribute("product") Product product) throws Exception {
		
		System.out.println("@ addProduct @");
		System.out.println(product);
		
		product.setManuDate(product.getManuDate().replaceAll("-", ""));
		productService.addProduct(product);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/product/addProduct.jsp");
		
		return modelAndView;
	}
	
//	@RequestMapping("listProduct.do")
//	@RequestMapping(value="listProduct/{menu}")
	@RequestMapping(value="listProduct")
	public ModelAndView listProduct(/*@PathVariable String menu,*/ @ModelAttribute("search") Search search) throws Exception {
		
		System.out.println("@ listProduct @");
//		System.out.println(menu);
		System.out.println(search);
		
		if(search.getCurrentPage() == 0) {
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		if(search.getSearchCondition()!=null &&search.getSearchCondition().equals("1")) {
			byte[] keyword = search.getSearchKeyword().getBytes();
			for(byte bytes : keyword) {
				System.out.println("searchKeyword 확인 : "+bytes);
				if(bytes >= 48 && bytes <=57) { //숫자일때
					search.setSearchKeyword("");
					search.setSearchKeywordPrice("");
				}
			}
		} else if(search.getSearchCondition() != null && search.getSearchCondition().equals("2")){
			byte[] keyword = search.getSearchKeyword().getBytes();
			for(byte bytes : keyword) {
				System.out.println("searchKeyword 확인 : "+bytes);
				if(bytes < 48 | bytes > 57) { //문자일때
					search.setSearchKeyword("");
					search.setSearchKeywordPrice("");
				}
			}
		}

		Map<String, Object> map = productService.getProductList(search);
		
		Page resultPage = new Page(search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		
		System.out.println(resultPage);
		
		ModelAndView modelAndView = new ModelAndView();
//		modelAndView.addObject("menu", menu);
		modelAndView.addObject("list", map.get("productList"));
		modelAndView.addObject("resultPage", resultPage);
		modelAndView.setViewName("/product/listProduct.jsp");
		
		return modelAndView;
	}
	
//	@RequestMapping("getProduct.do")
//	@RequestMapping(value="getProduct/{prodNo}/{menu}")
	@RequestMapping(value="getProduct")
	public ModelAndView getProduct(@RequestParam("prodNo") int prodNo,
														 @RequestParam("menu") String menu,
														/*@PathVariable String menu,
														@PathVariable int prodNo,*/
														/*Cookie cookie,*/
														 HttpServletResponse response, 
														 @CookieValue(value="history",required=false) Cookie cookie
														 ) throws Exception {
		
		System.out.println("@ getProduct  @");
		
		Product product = productService.getProduct(prodNo);
		
		
		if(cookie == null) {
			System.out.println("쿠키없음");
			Cookie history = new Cookie("history", prodNo+",");
			System.out.println("쿠키의 path :" +history.getPath());
//			history.setPath("/");
			response.addCookie(history);
		} else {
			System.out.println("쿠키있음");
			String value = prodNo+",";
//			for(int i = 0; i < cookies.length; i++) {
//				if(cookies[i].getName().equals("history")) {
//					value = cookies[i].getValue()+prodNo+",";
			value = cookie.getValue()+prodNo+",";
			System.out.println("쿠키의 path :" +cookie.getPath());
			System.out.println("@@@@cookie@@@:"+value);
			Cookie history = new Cookie("history", value);
			history.setPath("/");
			response.addCookie(history);
//				}
//			}
//			response.addCookie(new Cookie("history", value));
		}
		
		/*
		 * 
		 * CookieGenerator
		CookieGenerator cookieGenerator = new CookieGenerator();
		cookieGenerator.setCookieName("history");
		cookieGenerator.setCookiePath("/");
		cookieGenerator.addCookie(response, prodNo+",");
		Cookie[] cookies = request.getCookies();
		for(int i = 0; i < cookies.length; i++) {
			System.out.println(cookies[i].getName());
		}
		*/
		
		/*
		 * 
		 * Original
		if(history == null) {
			System.out.println("쿠키없움");
			Cookie cookie = new Cookie("history", prodNo+",");
			response.addCookie(cookie);
		} else {
			System.out.println("쿠키있움");
			response.addCookie(new Cookie("history", history+prodNo+","));
		}
		*/
		
		
		/*
		 * 
		 * request, response
		 * 
		 * 
		Cookie[] cookies = request.getCookies();
		if(cookies == null) {
			Cookie cookie = new Cookie("history", prodNo+",");
			cookie.setMaxAge(-1);
			response.addCookie(cookie);
		} else {
			String value = prodNo+",";
			for(int i = 0; i < cookies.length; i++) {
				if(cookies[i].getName().equals("history")) {
					value = cookies[i].getValue()+prodNo+",";
					System.out.println("@@@@cookie@@@:"+value);
				}
			}
			response.addCookie(new Cookie("history", value));
		}*/
		
		String viewName = null; 
		if(menu.equals("manage")) {
			viewName = "/product/updateProduct"; //상품수정
		} else  {	
			viewName = "/product/getProduct.jsp"; //상품상세보기
		}
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("menu", menu);
		modelAndView.addObject("product", product);
		modelAndView.setViewName(viewName);
		return modelAndView;
	}
	
	

//	@RequestMapping("updateProductView.do")
	@RequestMapping(value="updateProduct", method=RequestMethod.GET)
	public ModelAndView updateProductView(@RequestParam("prodNo") int prodNo,
																	  @RequestParam("menu") String menu
																		/*@PathVariable int prodNo,
																		@PathVariable String menu*/) throws Exception {
		
		System.out.println("@ updateProductView(GET) @");
		
		Product product = productService.getProduct(prodNo);
		
		ModelAndView modelAndView = new ModelAndView();
//		modelAndView.addObject("menu", menu);
		modelAndView.addObject("product", product);
		modelAndView.setViewName("/product/updateProduct.jsp");
		
		return modelAndView;
	}
	
//	@RequestMapping("updateProduct.do")
	@RequestMapping(value="updateProduct", method=RequestMethod.POST)
	public ModelAndView updateProduct(@ModelAttribute("product") Product product) throws Exception {

		System.out.println("@ updateProduct(POST) @");
		
		product.setManuDate(product.getManuDate().replaceAll("-", ""));
		productService.updateProduct(product);
		product = productService.getProduct(product.getProdNo());
		
		ModelAndView modelAndView = new ModelAndView();
//		modelAndView.addObject("menu", "manage");
		modelAndView.addObject("product", product);
		modelAndView.setViewName("/product/getProduct.jsp?menu=manage");
		
		return modelAndView;
	}
}
