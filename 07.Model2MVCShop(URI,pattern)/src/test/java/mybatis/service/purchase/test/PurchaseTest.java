package mybatis.service.purchase.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductDAO;
import com.model2.mvc.service.purchase.PurchaseDAO;
import com.model2.mvc.service.purchase.PurchaseService;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:config/commonservice.xml"})
@ContextConfiguration(locations = {"classpath:config/context-common.xml",
		"classpath:config/context-aspect.xml",
		"classpath:config/context-mybatis.xml",
		"classpath:config/context-transaction.xml"})
public class PurchaseTest {
	
	@Autowired
	@Qualifier("purchaseDAOImpl")
	private PurchaseDAO purchaseDAO;
	
	@Autowired
	@Qualifier("productDAOImpl")
	private ProductDAO productDAO;
	
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
	@Test
	public void testAddPurchase() throws Exception {
		
		User buyer = new User();
		buyer.setUserId("user13");
		buyer.setUserName("SCOTT");
		buyer.setPhone("010-3034-3783");
		
		Product product = productDAO.getProduct(10082);
		System.out.println(product);
		Purchase purchase = new Purchase();
		purchase.setPurchaseProd(product);
		purchase.setBuyer(buyer);
		purchase.setPaymentOption("1");
		purchase.setReceiverName("성공");
		purchase.setReceiverPhone("010");
		purchase.setReceiverAddr("여기용");
		purchase.setReceiverDate("2017-01-31");
		purchase.setTranCode("1");
		
		Purchase addPurchase = purchaseService.addPurchase(purchase);
		
		System.out.println("===============================");
		System.out.println(addPurchase);
		System.out.println("===============================");
		
		Assert.assertNotNull(addPurchase);
	}
	
//	@Test
	public void testGetPurchaseByProdNo() throws Exception {
		
		int prodNo = 10040;
		
		Purchase purchase = purchaseService.getPurchaseByProdNo(prodNo);
		
		System.out.println("===============================");
		System.out.println(purchase);
		System.out.println("===============================");
		
		Assert.assertNotNull(purchase);
	}
	
//	@Test
	public void testGetPurchase() throws Exception {
		
		int prodNo = 10040;
		
		Purchase purchase = purchaseDAO.getPurchaseByProdNo(prodNo);
		
		int tranNo = purchase.getTranNo();
		
		Purchase getPurchase = purchaseService.getPurchase(tranNo);
		
		System.out.println("===============================");
		System.out.println(getPurchase);
		System.out.println("===============================");
		
		Assert.assertNotNull(getPurchase);
	}
	
//	@Test
	public void testUpdatePurchase() throws Exception {
		
		int tranNo = 10097;
		
		Purchase purchase = new Purchase();
//		purchase.setTranNo(tranNo);
		purchase.setReceiverAddr("주소만변경");
		
		purchaseService.updatePurchase(purchase);
		
		Purchase getPurchase = purchaseDAO.getPurchase(tranNo);
		
		System.out.println("===============================");
		System.out.println(getPurchase);
		System.out.println("===============================");
		
		Assert.assertNotNull(getPurchase);
	}
	
//	@Test
	public void testUpdateTranCode() throws Exception{
		
		//case1 : 배송하기(by prodNo)
		int tranNo = 10056;
		
		Purchase purchase = new Purchase();
		purchase.setTranNo(tranNo);
		purchase.setTranCode("2");
		
		purchaseService.updateTranCode(purchase);
		
		Purchase getPurchase = purchaseDAO.getPurchase(tranNo);
		
		System.out.println("===============================");
		System.out.println(getPurchase);
		System.out.println("===============================");
		
		Assert.assertNotNull(getPurchase);
		//case2 : 물건도착(by tranNo)
	}
	
//	@Test
	public void testGetPurchaseList() throws Exception {
		
		Search search = new Search();
		search.setCurrentPage(1);
		search.setPageSize(3);
		search.setSearchCondition("");
		
		String buyerId = "user13";
		
		List<Purchase> list = purchaseDAO.getPurchaseList(search, buyerId);
		
		System.out.println("===============================");
		System.out.println(list);
		System.out.println("===============================");
		
		Assert.assertNotNull(list);
	}
	
//	@Test
	public void testGetSaleList() throws Exception {
		
		Search search = new Search();
		search.setCurrentPage(1);
		search.setPageSize(3);
		search.setSearchCondition("2");
		search.setSearchKeyword("1000");
		search.setSearchKeywordPrice("");
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("search", search);
		map.put("userId", "");
//		List<Purchase> list = purchaseService.getSaleList(search);
//		Assert.assertNotNull(list);
//		Assert.assertEquals(purchaseDAO.getTotalCount(map), list.size());
//		
//		System.out.println("===============================");
//		System.out.println(list);
//		System.out.println("===============================");
	}
	
//	@Test
	public void testGetTotalCount() throws Exception {
		
		Search search = new Search();
		search.setCurrentPage(1);
		search.setPageSize(3);
//		search.setSearchCondition("1");
//		search.setSearchKeyword("%말랑%");
		
		String buyerId = "admin";
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("search", search);
		map.put("userId", buyerId);
		
//		int count = purchaseDAO.getTotalCount(search, buyerId);
//		Assert.assertEquals(10, count);
		int count = purchaseDAO.getTotalCount(map);
		
		System.out.println("===============================");
		System.out.println(count);
		System.out.println("===============================");
	}
}
