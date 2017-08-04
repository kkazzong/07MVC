package com.model2.mvc.service.product.test;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductDAO;
import com.model2.mvc.service.product.ProductService;

import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:config/commonservice.xml"})
@ContextConfiguration(locations = {"classpath:config/context-common.xml",
															"classpath:config/context-aspect.xml",
															"classpath:config/context-mybatis.xml",
															"classpath:config/context-transaction.xml"})
public class ProductTest {
	
	//jUnit framework를 이용한 test
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	@Autowired
	@Qualifier("productDAOImpl")
	private ProductDAO productDAO;
	
	@Test
	public void testInsertProduct() throws Exception {
		
		Product product = new Product();
	
		product.setProdName("인서트");
		product.setProdDetail("하이");
		product.setManuDate("170101");
		
		productService.addProduct(product);
	
	}
	
//	@Test
	public void testFindProduct() throws Exception {
		
		Product product = productDAO.getProduct(10012);
		
		int prodNo = product.getProdNo();
		Product findProduct = productService.getProduct(prodNo);
		
		System.out.println("===============================");
		System.out.println(findProduct);
		System.out.println("===============================");
		
		Assert.assertNotNull(product);
		Assert.assertNotNull(findProduct);
	}
	
//	@Test
	public void testUpdateProduct() throws Exception {
		
		int prodNo = 10014;
		Product product = productDAO.getProduct(prodNo);
		
		Product updateProduct = new Product();
		updateProduct.setProdNo(product.getProdNo());
		
		//transaction 확인
//		updateProduct.setProdName(null);
		
		updateProduct.setProdDetail("수정확인하겟슴다");
		
		productService.updateProduct(updateProduct);
		
		updateProduct = productDAO.getProduct(prodNo);
		
		System.out.println("===============================");
		System.out.println(updateProduct);
		System.out.println("===============================");
		
		Assert.assertNotNull(product);
		Assert.assertNotNull(updateProduct);
	}
	
//	@Test
	public void testGetProductList() throws Exception {
		
		Search search = new Search();
		search.setPageSize(3);
		search.setCurrentPage(1);
		search.setSearchCondition("");
		search.setSearchKeyword("");
		search.setSearchKeywordPrice("");
		search.setSearchSoldProd("");
		search.setSortCondition("");
		search.setSortCondition2("");
		
		int totalCount = productDAO.getCount(search);
		System.out.println("@@@@@@@@@@@@@@@@"+totalCount);
		List<Product> list = productDAO.getProductList(search);
		
		Map<String, Object> map = productService.getProductList(search);
		List<Object> getList = (List<Object>)map.get("list");
		Integer getTotalCount = (Integer)map.get("totalCount");
		Assert.assertEquals(getTotalCount.intValue(), totalCount);
		Assert.assertEquals(3,	getList.size());
		
		System.out.println("===============================");
		System.out.println("totalCount : "+getTotalCount.intValue());
		System.out.println("list : "+getList);
		System.out.println("===============================");
		
	}
	
//	@Test
	public void testGetCount() throws Exception {
		
		Search search = new Search();
		search.setPageSize(3);
		search.setCurrentPage(1);
//		search.setSearchCondition("1");
//		search.setSearchKeyword("%자%");
//		search.setSearchKeywordPrice("");
		search.setSearchSoldProd("");
		search.setSortCondition("");
		search.setSortCondition2("");
		
		int count = productDAO.getCount(search);
		Map<String, Object> map = productService.getProductList(search);
		
		Assert.assertEquals(count, ((Integer)map.get("totalCount")).intValue());
		System.out.println("===============================");
		System.out.println("totalCount : : : "+map.get("totalCount"));
		System.out.println("===============================");
		
	}
}
