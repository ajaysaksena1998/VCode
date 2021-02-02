package com.ajay.springcloud.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ajay.springcloud.dto.Coupon;
import com.ajay.springcloud.model.Product;
import com.ajay.springcloud.repos.ProductRepo;
import com.ajay.springcloud.restClients.CouponClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping("/productapi")
@RefreshScope
public class ProductRestController {

	@Autowired
	private ProductRepo repo;
	
	@Autowired
	CouponClient couponClient;
	
	@Value("${com.ajay.springcloud.prop}")
	private String prop;

	@HystrixCommand(fallbackMethod = "sendErrorResponse")
	@RequestMapping(value = "/products", method = RequestMethod.POST)
	public Product create(@RequestBody Product product) {
		
		Coupon coupon = couponClient.getCoupon(product.getCouponCode());
		product.setPrice(product.getPrice().subtract(coupon.getDiscount()));
		return repo.save(product);

	}
	
	@RequestMapping(value="/prop",method=RequestMethod.GET)
	public String getProp() {
		return this.prop;
	}
	
	public Product sendErrorResponse(Product product) {
		return product;
		
	}

}
